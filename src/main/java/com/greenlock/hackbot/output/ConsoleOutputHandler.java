package com.greenlock.hackbot.output;

import com.greenlock.hackbot.QuickLog;

/**
 * Created by LukeSmalley on 1/17/2017.
 */
public class ConsoleOutputHandler implements OutputHandler {

    private static ConsoleOutputHandler instance = null;

    public static ConsoleOutputHandler get() {
        if (instance == null) {
            instance = new ConsoleOutputHandler();
        }
        return instance;
    }

    @Override
    public void print(String line) {
        QuickLog.info(line);
    }

    @Override
    public void failure(String line, String fail, String cause, Exception ex) {
        QuickLog.severe(line);
        QuickLog.severe(fail + " " + cause);
        QuickLog.exception(ex);
    }
}
