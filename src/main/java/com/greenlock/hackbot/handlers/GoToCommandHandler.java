package com.greenlock.hackbot.handlers;

import com.greenlock.hackbot.parsers.exec.ScriptContext;
import com.greenlock.hackbot.output.OutputHandler;

import java.util.List;

/**
 * Created by LukeSmalley on 1/17/2017.
 */
public class GoToCommandHandler extends CommandHandler {

    public GoToCommandHandler() {
        super(CommandHandlerScope.UNIVERSAL);
    }

    @Override
    protected void handle(List<String> args, ScriptContext context, OutputHandler output) {
        if (args.size() < 1) {
            if (context.verbose) {
                output.print("goto: Not enough arguments were given. You must provide a label name.");
            }
            return;
        }
        if (!context.goTo(args.get(0))) {
            if (context.verbose) {
                output.print("goto: Could not find label '" + args.get(0) + "'.");
            }
        }
    }
}
