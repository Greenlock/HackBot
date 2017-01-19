package com.greenlock.hackbot.output;

import com.greenlock.hackbot.QuickLog;
import com.greenlock.hackbot.discord.ConsoleMessage;
import sx.blah.discord.handle.obj.IMessage;

/**
 * Created by LukeSmalley on 1/13/2017.
 */
public class MessageOutputHandler implements OutputHandler {

    private IMessage message;

    public MessageOutputHandler(IMessage message) {
        this.message = message;
    }

    @Override
    public void print(String line) {
        try {
            message.getChannel().sendMessage(line);
            QuickLog.reply(message, line);
        } catch (Exception ex) {
            try { Thread.sleep(3000); } catch (InterruptedException iex) { }
            try {
                message.getChannel().sendMessage(line);
                QuickLog.reply(message, "(SECOND-ATTEMPT) " + line);
            } catch (Exception rex) {
                QuickLog.replyFailure(message, line, rex);
            }
        }
    }

    @Override
    public void failure(String line, String fail, String cause, Exception e) {
        try {
            message.getChannel().sendMessage(line);
            QuickLog.reply(message, line);
            QuickLog.severe(fail + " " + cause);
            QuickLog.exception(e);
        } catch (Exception ex) {
            try { Thread.sleep(3000); } catch (InterruptedException iex) { }
            try {
                message.getChannel().sendMessage(line);
                QuickLog.reply(message, "(SECOND-ATTEMPT) " + line);
                QuickLog.severe(fail + " " + cause);
                QuickLog.exception(e);
            } catch (Exception rex) {
                QuickLog.replyFailure(message, line, rex);
                QuickLog.severe(fail + " " + cause);
                QuickLog.exception(e);
            }
        }
    }
}
