package com.greenlock.hackbot;

import com.google.gson.reflect.TypeToken;
import com.greenlock.hackbot.discord.ConsoleMessage;
import com.greenlock.hackbot.handlers.ExecCommandHandler;
import com.greenlock.hackbot.handlers.*;
import com.greenlock.hackbot.parsers.exec.ScriptContext;
import com.greenlock.hackbot.output.MessageOutputHandler;
import com.greenlock.hackbot.output.ConsoleOutputHandler;
import com.greenlock.hackbot.output.OutputHandler;
import com.greenlock.hackbot.storage.Directory;
import com.greenlock.hackbot.storage.FileContainer;
import com.greenlock.hackbot.storage.MountedDirectory;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by LukeSmalley on 11/3/2016.
 */
public class HackBot {

    private IDiscordClient client = null;
    private MainConfiguration configuration = null;
    private String botMention = null;
    private FileContainer storage = null;
    private Map<String, Map<String, String>> variableContexts = new HashMap<>();
    private Map<String, CommandHandler> commandHandlers = new HashMap<>();

    private HackBot(IDiscordClient client, MainConfiguration configuration, FileContainer storage) {
        this.client = client;
        this.configuration = configuration;
        this.storage = storage;

        commandHandlers.put("exec", new ExecCommandHandler(this));
        commandHandlers.put("chat", new ChatCommandHandler(this));
        commandHandlers.put("hbinfo", new HBInfoCommandHandler());
        commandHandlers.put("hbstop", new HBStopCommandHandler(this));
        commandHandlers.put("print", new PrintCommandHandler());
        commandHandlers.put("set", new SetCommandHandler());
        commandHandlers.put("unset", new UnsetCommandHandler());
        commandHandlers.put("var", new VarCommandHandler());
        commandHandlers.put("goto", new GoToCommandHandler());
        commandHandlers.put("verbose", new VerboseCommandHandler());
        commandHandlers.put("file", new FileCommandHandler());
        commandHandlers.put("dir", new DirCommandHandler());
        commandHandlers.put("str", new StrCommandHandler());
        commandHandlers.put("if", new IfCommandHandler());
    }

    public static void main(String[] args) {
        try {
            QuickLog.initialize();
        } catch (IOException ex) {
            System.out.println("(CRITICAL) Failed to initialize the QuickLog service due to IOException: " + ex.getMessage());
            return;
        }

        if (!new File("./config.json").isFile()) {
            try {
                QuickGson.serializeToPrettyJsonFile(new File("./config.json"), new MainConfiguration());
                QuickLog.severe("Created main configuration file. Please specify the client token in this file and restart.");
            } catch (IOException ex) {
                QuickLog.severe("(CRITICAL) Failed to create the main configuration file.");
                QuickLog.exception(ex);
            }
            return;
        }

        MainConfiguration configuration = null;
        try {
            configuration = QuickGson.deserializeFromJsonFile(new File("./config.json"), MainConfiguration.class);
        } catch (IOException ex) {
            QuickLog.severe("(CRITICAL) Failed to load the main configuration file.");
            QuickLog.exception(ex);
            return;
        }

        if (configuration.clientToken == null) {
            QuickLog.severe("(CRITICAL) Client token is defined as 'null' in the config. You must specify a valid client token and restart.");
            return;
        }

        if (!new File("./storage").isDirectory()) {
            new File("./storage").mkdirs();
        }
        FileContainer storage = new Directory(new File("./storage"));

        IDiscordClient client = null;
        try {
            client = new ClientBuilder().withToken(configuration.clientToken).login();
            QuickLog.info("Discord has been started.");
        } catch (DiscordException ex) {
            QuickLog.severe("(CRITICAL) Failed to initialize Discord.");
            QuickLog.exception(ex);
            return;
        }

        HackBot instance = new HackBot(client, configuration, storage);
        client.getDispatcher().registerListener(instance);
        QuickLog.info("Event listeners have been registered.");

        Scanner s = new Scanner(System.in);
        while (true) {
            String input = s.nextLine();
            instance.onMessageReceived(new MessageReceivedEvent(new ConsoleMessage(input, client)));
        }
    }


    public void stop() throws IOException {
        FileContainer variableDirectory;
        try {
            variableDirectory = storage.createChild("var");
        } catch (IOException ex) {
            throw new IOException("Failed to create a directory to hold environment variables.");
        }

        for (String key : variableContexts.keySet()) {
            try {
                variableDirectory.writeFile(key, QuickGson.serializeToJson(variableContexts.get(key)));
            } catch (IOException ex) {
                throw new IOException("Failed to save environment variables for '" + key + "'.");
            }
        }
    }

    public boolean hasCommandHandler(String name) {
        return commandHandlers.containsKey(name.toLowerCase());
    }

    public void runCommandHandler(String name, List<String> args, ScriptContext context, OutputHandler output) {
        commandHandlers.get(name.toLowerCase()).invoke(args, context, output);
    }


    @EventSubscriber
    public void onReady(ReadyEvent event) {
        botMention = "<@" + client.getOurUser().getID() + ">";
        QuickLog.info("HackBot has been initialized.");
    }

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!(event.getMessage() instanceof ConsoleMessage)) {
            QuickLog.msg(event.getMessage());
        }

        OutputHandler output = event.getMessage() instanceof ConsoleMessage
                ? new ConsoleOutputHandler()
                : new MessageOutputHandler(event.getMessage());

        Map<String, String> variables;
        try {
            variables = buildVariableContextForMessage(event.getMessage());
        } catch (IOException ex) {
            output.print("Could not process message due to an internal error: " + ex.getMessage());
            return;
        }

        FileContainer workingDirectory;
        try {
            workingDirectory = buildWorkingDirectoryForMessage(event.getMessage());
        } catch (IOException ex) {
            output.print("Could not process message due to an internal error: " + ex.getMessage());
            return;
        }

        List<String> args = Collections.singletonList(event.getMessage().getContent().startsWith(botMention)
                ? event.getMessage().getContent().substring(botMention.length())
                : event.getMessage().getContent());

        if (event.getMessage().getGuild() == null) {
            runCommandHandler("exec", args, new ScriptContext(null, args, new Environment(event.getMessage(), variables, workingDirectory)), output);
        } else {
            if (event.getMessage().getContent().startsWith(botMention)) {
                runCommandHandler("exec", args, new ScriptContext(null, args, new Environment(event.getMessage(), variables, workingDirectory)), output);
            } else {
                runCommandHandler("chat", args, new ScriptContext(null, args, new Environment(event.getMessage(), variables, workingDirectory)), output);
            }
        }
    }

    private FileContainer buildWorkingDirectoryForMessage(IMessage message) throws IOException {
        if (message.getGuild() == null) {
            try {
                MountedDirectory m = new MountedDirectory(storage.createChild("users/" + message.getAuthor().getID()));
                m.mount("~", storage.getChild("users/" + message.getAuthor().getID()));
                return m;
            } catch (IOException ex) {
                throw new IOException("Failed to create directory for user '" + message.getAuthor().getName() + "'.");
            }
        } else {
            if (message.getContent().startsWith(botMention)) {
                MountedDirectory m;

                try {
                    m = new MountedDirectory(storage.createChild("guilds/" + message.getGuild().getID()));
                } catch (IOException ex) {
                    throw new IOException("Failed to create directory for guild '" + message.getGuild().getName() + "'.");
                }

                try {
                    m.mount("~", storage.createChild("users/" + message.getAuthor().getID()));
                } catch (IOException ex) {
                    throw new IOException("Failed to create directory for user '" + message.getAuthor().getName() + "'.");
                }

                return m;
            } else {
                try {
                    return storage.createChild("guilds/" + message.getGuild().getID());
                } catch (IOException ex) {
                    throw new IOException("Failed to create directory for guild '" + message.getGuild().getName() + "'.");
                }
            }
        }
    }

    private Map<String, String> buildVariableContextForMessage(IMessage message) throws IOException {
        FileContainer variableDirectory;
        try {
            variableDirectory = storage.createChild("var");
        } catch (IOException ex) {
            throw new IOException("Failed to create a directory to hold environment variables.");
        }

        String key = message.getGuild() == null ? "user." + message.getAuthor().getID() : "guild." + message.getGuild().getID();
        if (!variableContexts.containsKey(key)) {
            try {
                variableContexts.put(key, QuickGson.deserializeFromJson(variableDirectory.createFile(key, "{}"), new TypeToken<Map<String, String>>(){}.getType()));
            } catch (IOException ex) {
                throw new IOException("Failed to load environment variables for " +
                        (message.getGuild() == null ? "user '" + message.getAuthor().getName() + "'." : "guild '" + message.getGuild().getName() + "'."));
            }
        }
        return variableContexts.get(key);
    }
}
