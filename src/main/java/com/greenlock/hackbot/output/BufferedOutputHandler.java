package com.greenlock.hackbot.output;

import com.greenlock.hackbot.QuickLog;

/**
 * Created by LukeSmalley on 1/14/2017.
 */
public class BufferedOutputHandler implements OutputHandler {

    private String buffer = "";

    public String getBufferedText() {
        return buffer;
    }

    @Override
    public void print(String line) {
        buffer += line;
    }

    @Override
    public void failure(String line, String fail, String cause, Exception ex) {
        buffer += line;
        QuickLog.severe(fail + " " + cause);
        QuickLog.exception(ex);
    }
}
