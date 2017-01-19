package com.greenlock.hackbot.handlers;

import com.greenlock.hackbot.HackBot;
import com.greenlock.hackbot.parsers.exec.ScriptContext;
import com.greenlock.hackbot.output.OutputHandler;

import java.io.IOException;
import java.util.List;

/**
 * Created by LukeSmalley on 12/24/2016.
 */
public class HBStopCommandHandler extends CommandHandler {

    private HackBot bot;

    public HBStopCommandHandler(HackBot bot) {
        super(CommandHandlerScope.CONSOLE_ONLY);
        this.bot = bot;
    }

    @Override
    protected void handle(List<String> args, ScriptContext context, OutputHandler output) {
        try {
            bot.stop();
            System.exit(0);
        } catch (IOException ex) {
            output.failure("An error is preventing HackBot from shutting down correctly.",
                    "Failed to stop HackBot.",
                    "Variable serialization failed.", ex);
        }
    }
}
