package com.greenlock.hackbot.handlers;

import com.greenlock.hackbot.parsers.exec.ScriptContext;
import com.greenlock.hackbot.output.OutputHandler;

import java.util.List;

/**
 * Created by LukeSmalley on 1/17/2017.
 */
public class VerboseCommandHandler extends CommandHandler {

    public VerboseCommandHandler() {
        super(CommandHandlerScope.UNIVERSAL);
    }

    @Override
    protected void handle(List<String> args, ScriptContext context, OutputHandler output) {
        if (args.size() > 0) {
            String arg = args.get(0);
            if (arg.equalsIgnoreCase("true") || arg.equalsIgnoreCase("yes") || arg.equalsIgnoreCase("on")) {
                context.verbose = true;
            } else if (arg.equalsIgnoreCase("false") || arg.equalsIgnoreCase("no") || arg.equalsIgnoreCase("off")) {
                context.verbose = false;
            } else {
                context.verbose = !context.verbose;
            }
        } else {
            context.verbose = !context.verbose;
        }
    }
}
