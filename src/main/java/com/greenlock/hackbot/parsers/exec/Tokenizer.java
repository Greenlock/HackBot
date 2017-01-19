package com.greenlock.hackbot.parsers.exec;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LukeSmalley on 1/7/2017.
 */
public class Tokenizer extends StringIterator<List<ScriptCommand>> {

    private List<ScriptCommand> commands = new ArrayList<>();
    private List<Token> commandTokens = new ArrayList<>();
    private Token token = new Token();
    private String subtoken = "";


    private void pushToSubtoken(char c) {
        subtoken += c;
    }

    private void pushToSubtoken(String c) {
        subtoken += c;
    }

    private void pushSubtokenAsEvaluable() {
        if (subtoken.length() > 0) {
            token.add(new VariableString(subtoken));
            subtoken = "";
        }
    }

    private void pushSubtokenAsLiteral() {
        if (subtoken.length() > 0) {
            token.add(new LiteralString(subtoken));
            subtoken = "";
        }
    }

    private void pushToken() {
        if (!token.isEmpty()) {
            commandTokens.add(token);
            token = new Token();
        }
    }

    private void pushCommand() {
        if (!commandTokens.isEmpty()) {
            commands.add(new ScriptCommand(commandTokens));
            commandTokens = new ArrayList<>();
        }
    }


    private Runnable defaultState = new Runnable() {
        @Override
        public void run() {
            if (currentEquals('"')) {
                pushSubtokenAsEvaluable();
                enter(doubleQuoteState);
            } else if (currentBegins("```")) {
                pushSubtokenAsEvaluable();
                while (canAdvance()) {
                    advance();
                    if (currentEquals('\n')) {
                        break;
                    }
                }
                enter(multilineCodeState);
            } else if (currentEquals('`')) {
                pushSubtokenAsEvaluable();
                enter(codeState);
            } else if (currentEquals('\\')) {
                if (canAdvance()) {
                    advance();
                    if (currentEquals('`') || currentEquals('"')) {
                        pushToSubtoken(current());
                    } else {
                        pushToSubtoken('\\');
                        pushToSubtoken(current());
                    }
                } else {
                    pushToSubtoken(current());
                }
            } else if (currentEquals(' ') || currentEquals('\t')) {
                pushSubtokenAsEvaluable();
                pushToken();
            } else if (currentEquals('\n')) {
                pushSubtokenAsEvaluable();
                pushToken();
                pushCommand();
            } else if (currentEquals('#')) {
                pushSubtokenAsEvaluable();
                enter(commentState);
            } else {
                pushToSubtoken(current());
            }
        }
    };

    private Runnable doubleQuoteState = new Runnable() {
        @Override
        public void run() {
            if (currentEquals('"')) {
                pushSubtokenAsEvaluable();
                leave();
            } else if (currentEquals('\\')) {
                if (canAdvance()) {
                    advance();
                    if (currentEquals('"') || currentEquals('`')) {
                        pushToSubtoken(current());
                    } else {
                        pushToSubtoken('\\');
                        pushToSubtoken(current());
                    }
                } else {
                    pushToSubtoken(current());
                }
            } else if (currentEquals('\n')) {
                pushSubtokenAsEvaluable();
                pushToken();
                pushCommand();
                leave();
            } else {
                pushToSubtoken(current());
            }
        }
    };

    private Runnable codeState = new Runnable() {
        @Override
        public void run() {
            if (currentEquals('`')) {
                pushSubtokenAsLiteral();
                leave();
            } else {
                pushToSubtoken(current());
            }
        }
    };

    private Runnable multilineCodeState = new Runnable() {
        @Override
        public void run() {
            if (currentBegins("\n```")) {
                pushSubtokenAsLiteral();
                leave();
            } else {
                pushToSubtoken(current());
            }
        }
    };

    private Runnable commentState = new Runnable() {
        @Override
        public void run() {
            if (currentEquals('\n')) {
                leave();
            }
        }
    };


    public Tokenizer(String specimen) {
        super(specimen);
        result = commands;
        enter(defaultState);
    }

    @Override
    public void onFinish() {
        pushSubtokenAsEvaluable();
        pushToken();
        pushCommand();
    }
}
