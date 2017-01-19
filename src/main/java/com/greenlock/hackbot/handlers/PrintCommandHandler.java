package com.greenlock.hackbot.handlers;

import com.greenlock.hackbot.parsers.exec.ScriptContext;
import com.greenlock.hackbot.output.OutputHandler;

import java.util.List;

/**
 * Created by LukeSmalley on 1/2/2017.
 */
public class PrintCommandHandler extends CommandHandler {

    public PrintCommandHandler() {
        super(CommandHandlerScope.UNIVERSAL);
    }

    @Override
    protected void handle(List<String> args, ScriptContext context, OutputHandler output) {
        String s = "";
        for (String arg : args) {
            if (s.length() > 0) {
                s += " ";
            }
            s += arg;
        }
        if (!s.equals("")) {
            output.print(s);
        }
    }
}
