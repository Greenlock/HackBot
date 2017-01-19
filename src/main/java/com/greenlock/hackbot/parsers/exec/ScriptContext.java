package com.greenlock.hackbot.parsers.exec;

import com.greenlock.hackbot.Environment;
import com.greenlock.hackbot.output.OutputHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LukeSmalley on 1/17/2017.
 */
public class ScriptContext {

    private Script script;
    private List<String> args;
    private Environment env;
    private Map<String, String> variables = new HashMap<>();
    public boolean verbose = true;

    public ScriptContext(Script script, List<String> args, Environment env) {
        this.script = script;
        this.args = args;
        this.env = env;
    }

    public List<String> getArguments() {
        return args;
    }

    public Environment getEnvironment() {
        return env;
    }

    public boolean hasVariable(String name) {
        return variables.containsKey(name) || env.hasVariable(name);
    }

    public String getVariable(String name) {
        return variables.containsKey(name) ? variables.get(name) : (env.hasVariable(name) ? env.getVariable(name) : "");
    }

    public void deleteVariable(String name) {
        if (variables.containsKey(name)) {
            variables.remove(name);
        } else if (env.hasVariable(name)) {
            env.deleteVariable(name);
        }
    }

    public void setGlobalVariable(String name, String value) {
        env.setVariable(name, value);
    }

    public void setLocalVariable(String name, String value) {
        variables.put(name, value);
    }

    public String captureCommand(String path, List<String> args) throws IOException {
        return script.captureCommand(path, args);
    }

    public boolean runCommand(String path, List<String> args, OutputHandler output) throws IOException {
        return script.runCommand(path, args, output);
}

    public boolean goTo(String name) {
        return script.goTo(name);
    }
}
