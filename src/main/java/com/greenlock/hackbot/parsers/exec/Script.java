package com.greenlock.hackbot.parsers.exec;

import com.greenlock.hackbot.Environment;
import com.greenlock.hackbot.handlers.ExecCommandHandler;
import com.greenlock.hackbot.output.BufferedOutputHandler;
import com.greenlock.hackbot.output.OutputHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LukeSmalley on 1/14/2017.
 */
public class Script {

    private ScriptContext context;
    private ExecCommandHandler exec;
    private List<ScriptCommand> commands;
    private int index = 0;
    private Map<String, String> variables = new HashMap<>();

    public Script(String script, List<String> args, Environment env, ExecCommandHandler exec) {
        this.context = new ScriptContext(this, args, env);
        this.exec = exec;
        commands = new Tokenizer(script).run();
    }

    public ScriptContext getContext() {
        return context;
    }

    public String captureCommand(String path, List<String> args) throws IOException {
        BufferedOutputHandler output = new BufferedOutputHandler();
        if (!exec.runPath(path, args, context, output)) {
            throw new IOException("The executable does not exist.");
        }
        return output.getBufferedText();
    }

    public boolean runCommand(String path, List<String> args, OutputHandler output) throws IOException {
        return exec.runPath(path, args, context, output);
    }

    public boolean hasNext() {
        while (index < commands.size()) {
            if (!isLabel(commands.get(index), null)) {
                return true;
            }
            index++;
        }
        return false;
    }

    public ScriptCommand next() {
        return commands.get(index++);
    }

    public boolean goTo(String label) {
        for (int i = 0; i < commands.size(); i++) {
            ScriptCommand command = commands.get(i);
            if (isLabel(command, label)) {
                index = i + 1;
                return true;
            }
        }
        return false;
    }

    private boolean isLabel(ScriptCommand command, String name) {
        if (command.getArgumentCount() == 0) {
            String cpath = command.getPath(context);
            return name == null ? cpath.endsWith(":") : cpath.equals(name + ":");
        }
        return false;
    }
}
