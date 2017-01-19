package com.greenlock.hackbot.handlers;

import com.greenlock.hackbot.HackBot;
import com.greenlock.hackbot.parsers.exec.ScriptCommand;
import com.greenlock.hackbot.parsers.exec.Script;
import com.greenlock.hackbot.parsers.exec.ScriptContext;
import com.greenlock.hackbot.output.OutputHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LukeSmalley on 12/21/2016.
 */
public class ExecCommandHandler extends CommandHandler {

    private HackBot bot;

    public ExecCommandHandler(HackBot bot) {
        super(CommandHandlerScope.UNIVERSAL);
        this.bot = bot;
    }

    @Override
    protected void handle(List<String> args, ScriptContext context, OutputHandler output) {
        if (args.isEmpty()) {
            return;
        }
        runScript(args.get(0), args.size() > 1 ? args.subList(1, args.size()) : new ArrayList<>(), context,  output);
    }

    private Map<String, String> getMetadata(String input) {
        String[] lines = input.split("\n");
        Map<String, String> metadata = new HashMap<>();

        metadata.put("!", "exec");
        if (lines[0].startsWith("#!")) {
            metadata.put("!", lines[0].substring(2).trim());
        }

        for (String line : lines) {
            if (line.startsWith("#$")) {
                metadata.put(line.substring(2).split("=")[0],
                        line.substring(line.split("=")[0].length() + 1));
            }
        }

        return metadata;
    }

    private void runScript(String input, List<String> args, ScriptContext context, OutputHandler output) {
        Map<String, String> metadata = getMetadata(input);

        //TODO: Handle metadata here, before execution.

        if (!metadata.get("!").equalsIgnoreCase("exec")) {
            List<String> interpreterArgs = new ArrayList<>();
            interpreterArgs.add(input);
            interpreterArgs.addAll(args);
            try {
                if (!runPath(metadata.get("!"), interpreterArgs, context, output)) {
                    output.print("exec: The executable '" + metadata.get("!") + "' could not be found!");
                }
            } catch (IOException ex) {
                output.failure("exec: An error prevented '" + metadata.get("!") + "' from being loaded: " + ex.getMessage(),
                        "Failed to run the executor script '" + metadata.get("!") + "'.",
                        "The file could not be read.", ex);
            }
            return;
        }

        Script script = new Script(input, args, context.getEnvironment(), this);
        while (script.hasNext()) {
            ScriptCommand command = script.next();
            String path = command.getPath(script.getContext());
            List<String> cargs = command.getArguments(script.getContext());
            try {
                if (!runPath(path, cargs, script.getContext(), output)) {
                    output.print("exec: The executable '" + path + "' could not be found!");
                    return;
                }
            } catch (IOException ex) {
                output.failure("exec: An error prevented '" + path + "' from being loaded: " + ex.getMessage(),
                        "Failed to run the executable '" + path + "'.",
                        "The file could not be read.", ex);
                return;
            }
        }
    }

    public boolean runPath(String path, List<String> args, ScriptContext context, OutputHandler output) throws IOException {
        if (path.equalsIgnoreCase("exec")) {
            runScript(args.get(0), args.size() > 1 ? args.subList(1, args.size()) : new ArrayList<>(), context, output);
        } else if (context.getEnvironment().getWorkingDirectory().hasFile(path)) {
            runScript(context.getEnvironment().getWorkingDirectory().getFile(path), args.size() > 1 ? args.subList(1, args.size()) : new ArrayList<>(), context, output);
        } else if (bot.hasCommandHandler(path)) {
            bot.runCommandHandler(path, args, context, output);
        } else {
            return false;
        }
        return true;
    }
}
