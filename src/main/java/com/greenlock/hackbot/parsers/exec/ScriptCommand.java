package com.greenlock.hackbot.parsers.exec;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LukeSmalley on 12/20/2016.
 */
public class ScriptCommand {

    private Token path;
    private List<Token> args = new ArrayList<>();

    public ScriptCommand(List<Token> tokens) {
        this.path = tokens.get(0);
        if (tokens.size() > 1) {
            this.args = tokens.subList(1, tokens.size());
        }
    }

    public String getPath(ScriptContext script) {
        return path.evaluate(script);
    }

    public int getArgumentCount() {
        return args.size();
    }

    public List<String> getArguments(ScriptContext script) {
        List<String> parsedArgs = new ArrayList<>();
        for (Token t : args) {
            parsedArgs.add(t.evaluate(script));
        }
        return parsedArgs;
    }
}
