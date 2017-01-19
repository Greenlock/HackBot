package com.greenlock.hackbot.handlers;

import com.greenlock.hackbot.parsers.exec.ScriptContext;
import com.greenlock.hackbot.output.OutputHandler;

import java.util.List;

/**
 * Created by LukeSmalley on 12/23/2016.
 */
public class HBInfoCommandHandler extends CommandHandler {

    public HBInfoCommandHandler() {
        super(CommandHandlerScope.UNIVERSAL);
    }

    @Override
    protected void handle(List<String> args, ScriptContext context, OutputHandler output) {
        output.print("Greetings, humans! You are running **HackBot v1.0**!");
    }
}
