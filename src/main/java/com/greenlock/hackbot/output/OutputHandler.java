package com.greenlock.hackbot.output;

/**
 * Created by LukeSmalley on 1/13/2017.
 */
public interface OutputHandler {
    void print(String line);
    void failure(String line, String fail, String cause, Exception ex);
}
