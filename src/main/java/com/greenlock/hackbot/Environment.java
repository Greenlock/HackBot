package com.greenlock.hackbot;

import com.greenlock.hackbot.storage.FileContainer;
import sx.blah.discord.handle.obj.IMessage;

import java.util.Map;

/**
 * Created by LukeSmalley on 12/27/2016.
 */
public class Environment {

    private IMessage message;
    private Map<String, String> variables;
    private FileContainer workingDirectory;

    public Environment(IMessage message, Map<String, String> variables, FileContainer workingDirectory) {
        this.message = message;
        this.variables = variables;
        this.workingDirectory = workingDirectory;
    }


    public IMessage getMessage() {
        return message;
    }

    public FileContainer getWorkingDirectory() {
        return workingDirectory;
    }


    public boolean hasVariable(String name) {
        return variables.containsKey(name);
    }

    public String getVariable(String name) {
        return variables.get(name);
    }

    public void setVariable(String name, String value) {
        variables.put(name, value);
    }

    public void deleteVariable(String name) {
        variables.remove(name);
    }


    /*public void sendResponse(String reply) {
        try {
            outputPipe.print(reply);
            if (!(message instanceof ConsoleMessage) && !(outputPipe instanceof BufferedOutputHandler)) {
                QuickLog.reply(message, reply);
            }
        } catch (Exception ex) {
            try { Thread.sleep(3000); } catch (InterruptedException iex) {*/ /* Ignore Interruption. */ /*}
            try {
                outputPipe.print(reply);
                if (!(message instanceof ConsoleMessage) && !(outputPipe instanceof BufferedOutputHandler)) {
                    QuickLog.reply(message, "(SECOND-ATTEMPT) " + reply);
                }
            } catch (Exception rex) {
                QuickLog.replyFailure(message, reply, rex);
            }
        }
    }*/
}
