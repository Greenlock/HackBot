package com.greenlock.hackbot.parsers.exec;

/**
 * Created by LukeSmalley on 1/11/2017.
 */
public class LiteralString implements Evaluable {

    private String value;

    public LiteralString(String value) {
        this.value = value;
    }

    @Override
    public String evaluate(ScriptContext script) {
        return value;
    }
}
