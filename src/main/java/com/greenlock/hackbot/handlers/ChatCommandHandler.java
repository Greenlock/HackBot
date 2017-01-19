package com.greenlock.hackbot.handlers;

import com.greenlock.hackbot.HackBot;
import com.greenlock.hackbot.parsers.exec.ScriptContext;
import com.greenlock.hackbot.output.OutputHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created by LukeSmalley on 12/27/2016.
 */
public class ChatCommandHandler extends CommandHandler {

    private HackBot bot;

    public ChatCommandHandler(HackBot bot) {
        super(CommandHandlerScope.UNIVERSAL);
        this.bot = bot;
    }

    @Override
    protected void handle(List<String> args, ScriptContext context, OutputHandler output) {
        if (args.size() < 0) {
            if (context.verbose) {
                output.print("chat: Not enough arguments were provided. You must specify a chat message.");
            }
            return;
        }
        if (context.getEnvironment().getWorkingDirectory().hasFile("chat.sh")) {
            try {
                bot.runCommandHandler("exec", context);
                context.runCommand("chat.sh", args, output);
            } catch (IOException ex) {
                if (context.verbose) {
                    output.failure("chat: An error prevented 'chat.sh' from being executed: " + ex.getMessage(),
                            "Failed to handle chat message.",
                            "The 'chat.sh' file failed to execute.", ex);
                }
            }
        }
    }
}
