package com.greenlock.hackbot.handlers;

import com.greenlock.hackbot.parsers.exec.ScriptContext;
import com.greenlock.hackbot.output.OutputHandler;

import java.util.List;

/**
 * Created by LukeSmalley on 1/15/2017.
 */
public class VarCommandHandler extends CommandHandler {

    public VarCommandHandler() {
        super(CommandHandlerScope.UNIVERSAL);
    }

    @Override
    protected void handle(List<String> args, ScriptContext context, OutputHandler output) {
        if (args.size() < 2) {
            if (context.verbose) {
                output.print("var: Not enough arguments were given. You must provide a variable name and a value.");
            }
            return;
        }
        context.setLocalVariable(args.get(0), args.get(1));
    }
}