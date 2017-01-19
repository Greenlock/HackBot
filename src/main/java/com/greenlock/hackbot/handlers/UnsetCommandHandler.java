package com.greenlock.hackbot.handlers;

import com.greenlock.hackbot.parsers.exec.ScriptContext;
import com.greenlock.hackbot.output.OutputHandler;

import java.util.List;

/**
 * Created by LukeSmalley on 1/3/2017.
 */
public class UnsetCommandHandler extends CommandHandler {

    public UnsetCommandHandler() {
        super(CommandHandlerScope.UNIVERSAL);
    }

    @Override
    protected void handle(List<String> args, ScriptContext context, OutputHandler output) {
        if (args.size() < 1) {
            if (context.verbose) {
                output.print("unset: Not enough arguments were given. You must provide a variable name.");
            }
            return;
        }
        context.deleteVariable(args.get(0));
    }
}
