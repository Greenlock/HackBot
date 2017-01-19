package com.greenlock.hackbot.handlers;

import com.greenlock.hackbot.QuickLog;
import com.greenlock.hackbot.output.OutputHandler;
import com.greenlock.hackbot.parsers.exec.ScriptContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LukeSmalley on 1/18/2017.
 */
public class IfCommandHandler extends CommandHandler {

    public IfCommandHandler() {
        super(CommandHandlerScope.UNIVERSAL);
    }

    @Override
    protected void handle(List<String> args, ScriptContext context, OutputHandler output) {
        if (args.size() < 2) {
            if (context.verbose) {
                output.print("if: Not enough arguments were provided. You must provide a true or false value and a command to be executed.");
            }
            return;
        }
        if (args.get(0).equalsIgnoreCase("not")) {
            if (args.size() < 3) {
                if (context.verbose) {
                    output.print("if-not: Not enough arguments were provided. You must provide a true or false value and a command to be executed.");
                }
                return;
            }

            if (!args.get(1).trim().equalsIgnoreCase("true")) {
                List<String> invokedArgs = args.size() > 3 ? args.subList(3, args.size()) : new ArrayList<>();
                try {
                    context.runCommand(args.get(2), invokedArgs, output);
                } catch (IOException ex) {
                    output.failure("if: The conditional command could not be executed.",
                            "Failed to run conditional command",
                            "The command could not be found or loaded.", ex);
                }
            }
        } else {
            if (args.get(0).trim().equalsIgnoreCase("true")) {
                List<String> invokedArgs = args.size() > 2 ? args.subList(2, args.size()) : new ArrayList<>();
                try {
                    context.runCommand(args.get(1), invokedArgs, output);
                } catch (IOException ex) {
                    output.failure("if: The conditional command could not be executed.",
                            "Failed to run conditional command",
                            "The command could not be found or loaded.", ex);
                }
            }
        }
    }
}
