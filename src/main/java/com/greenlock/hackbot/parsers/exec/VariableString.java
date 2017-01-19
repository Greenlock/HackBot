package com.greenlock.hackbot.parsers.exec;

/**
 * Created by LukeSmalley on 1/11/2017.
 */
public class VariableString implements Evaluable {

    private String value;

    public VariableString(String value) {
        this.value = value;
    }

    @Override
    public String evaluate(ScriptContext script) {
        return new VariableParser(value, script).run();
    }
}
