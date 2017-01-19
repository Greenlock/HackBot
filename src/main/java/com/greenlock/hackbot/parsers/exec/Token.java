package com.greenlock.hackbot.parsers.exec;

import java.util.ArrayList;

/**
 * Created by LukeSmalley on 1/11/2017.
 */
public class Token extends ArrayList<Evaluable> implements Evaluable {

    @Override
    public String evaluate(ScriptContext script) {
        String token = "";
        for (Evaluable e : this) {
            token += e.evaluate(script);
        }
        return token;
    }
}
