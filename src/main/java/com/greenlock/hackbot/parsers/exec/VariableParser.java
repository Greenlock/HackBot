package com.greenlock.hackbot.parsers.exec;

import sun.awt.shell.ShellFolderColumnInfo;

import java.util.List;

/**
 * Created by LukeSmalley on 1/12/2017.
 */
public class VariableParser extends StringIterator<String> {

    private ScriptContext context;
    private String parsedString = "";
    private String substring = "";
    private int depth = 0;


    private Runnable defaultState = new Runnable() {
        @Override
        public void run() {
            if (currentEquals('$')) {
                if (canAdvance()) {
                    advance();
                    if (currentEquals('(')) {
                        enter(commandSubstitutionState);
                    } else if (currentEquals('{')) {
                        enter(variableState);
                    } else {
                        parsedString += '$';
                        parsedString += current();
                    }
                } else {
                    parsedString += current();
                }
            } else if (currentEquals('\\')) {
                if (canAdvance()) {
                    advance();
                    if (currentEquals('$')) {
                        parsedString += '\\';
                        parsedString += current();
                    }
                } else {
                    parsedString += current();
                }
            } else {
                parsedString += current();
            }
        }
    };

    private Runnable commandSubstitutionState = new Runnable() {
        @Override
        public void run() {
            if (currentEquals('(')) {
                depth++;
                substring += current();
            } else if (currentEquals(')')) {
                if (depth >= 1) {
                    depth--;
                    substring += current();
                } else {
                    String expression = new VariableParser(substring, context).run();
                    Script subscript = new Script(expression, context.getArguments(), context.getEnvironment(), null);
                    while (subscript.hasNext()) {
                        ScriptCommand command = subscript.next();
                        String cpath = command.getPath(context);
                        List<String> cargs = command.getArguments(context);
                        try {
                            parsedString += context.captureCommand(cpath, cargs);
                        } catch (Exception ex) { }
                    }
                    leave();
                }
            } else if (currentEquals('\\')) {
                if (canAdvance()) {
                    advance();
                    if (currentEquals(')')) {
                        substring += '\\';
                        substring += current();
                    }
                } else {
                    substring += current();
                }
            } else {
                substring += current();
            }
        }
    };

    private Runnable variableState = new Runnable() {
        @Override
        public void run() {
            if (currentEquals('{')) {
                depth++;
                substring += current();
            } else if (currentEquals('}')) {
                if (depth >= 1) {
                    depth--;
                    substring += current();
                } else {
                    String expression = new VariableParser(substring, context).run();
                    if (isNumeric(expression) && Integer.parseInt(expression) < context.getArguments().size()) {
                        parsedString += context.getArguments().get(Integer.parseInt(expression));
                    } else if (context.hasVariable(expression)) {
                        parsedString += context.getVariable(expression);
                    }
                    leave();
                }
            } else if (currentEquals('\\')) {
                if (canAdvance()) {
                    advance();
                    if (currentEquals('}')) {
                        substring += '\\';
                        substring += current();
                    }
                } else {
                    substring += current();
                }
            } else {
                substring += current();
            }
        }
    };

    private boolean isNumeric(String num) {
        if (num.contains("-")) {
            return false;
        }
        try {
            Integer.parseInt(num);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }


    public VariableParser(String input, ScriptContext context) {
        super(input);
        this.context = context;
        enter(defaultState);
    }

    @Override
    public void onFinish() {
        result = parsedString;
    }
}
