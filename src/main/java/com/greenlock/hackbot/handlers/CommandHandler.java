package com.greenlock.hackbot.handlers;

import com.greenlock.hackbot.parsers.exec.ScriptContext;
import com.greenlock.hackbot.discord.ConsoleMessage;
import com.greenlock.hackbot.output.OutputHandler;

import java.util.List;

/**
 * Created by LukeSmalley on 12/18/2016.
 */
public abstract class CommandHandler {

    public enum CommandHandlerScope {
        UNIVERSAL, CHANNEL_ONLY, CONSOLE_ONLY
    }

    private CommandHandlerScope scope;

    protected CommandHandler(CommandHandlerScope scope) {
        this.scope = scope;
    }

    protected abstract void handle(List<String> args, ScriptContext context, OutputHandler output);

    public void invoke(List<String> args, ScriptContext context, OutputHandler output) {
        if (context.getEnvironment().getMessage() instanceof ConsoleMessage) {
            if (scope == CommandHandlerScope.CHANNEL_ONLY) {
                output.print("This command can only be used from a Discord client.");
                return;
            }
        } else if (!(context.getEnvironment().getMessage() instanceof ConsoleMessage)) {
            if (scope == CommandHandlerScope.CONSOLE_ONLY) {
                output.print("This command can only be used from the bot's console interface.");
                return;
            }
        }

        handle(args, context, output);
    }
}

