package com.greenlock.hackbot.handlers;

import com.greenlock.hackbot.output.OutputHandler;
import com.greenlock.hackbot.parsers.exec.ScriptContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LukeSmalley on 1/18/2017.
 */
public class StrCommandHandler extends CommandHandler {

    public StrCommandHandler() {
        super(CommandHandlerScope.UNIVERSAL);
    }

    @Override
    protected void handle(List<String> args, ScriptContext context, OutputHandler output) {
        if (args.size() < 1) {
            if (context.verbose) {
                output.print("str: Not enough arguments provided. You must specify a subcommand.");
            }
            return;
        }
        String subcommand = args.get(0);
        if (subcommand.equalsIgnoreCase("contains")) {
            contains(subList(args, 1), context, output);
        } else if (subcommand.equalsIgnoreCase("equals")) {
            equals(subList(args, 1), context, output);
        } else if (subcommand.equalsIgnoreCase("equalscaseless")) {
            equalscaseless(subList(args, 1), context, output);
        } else if (subcommand.equalsIgnoreCase("startswith")) {
            startswith(subList(args, 1), context, output);
        } else if (subcommand.equalsIgnoreCase("endswith")) {
            endswith(subList(args, 1), context, output);
        } else if (subcommand.equalsIgnoreCase("substring")) {
            substring(subList(args, 1), context, output);
        } else {
            if (context.verbose) {
                output.print("str: '" + args.get(0) + "' is not a valid subcommand.");
            }
        }
    }

    private List<String> subList(List<String> list, int index) {
        return index < list.size() ? list.subList(index, list.size()) : new ArrayList<>();
    }


    private void contains(List<String> args, ScriptContext context, OutputHandler output) {
        if (args.size() < 1) {
            if (context.verbose) {
                output.print("str: Not enough arguments provided. You must specify a string value and an operand.");
            }
            return;
        }
        if (args.get(0).contains(args.get(1))) {
            output.print("true");
        } else {
            output.print("false");
        }
    }

    private void equals(List<String> args, ScriptContext context, OutputHandler output) {
        if (args.size() < 1) {
            if (context.verbose) {
                output.print("str: Not enough arguments provided. You must specify a string value and an operand.");
            }
            return;
        }
        if (args.get(0).equals(args.get(1))) {
            output.print("true");
        } else {
            output.print("false");
        }
    }

    private void equalscaseless(List<String> args, ScriptContext context, OutputHandler output) {
        if (args.size() < 1) {
            if (context.verbose) {
                output.print("str: Not enough arguments provided. You must specify a string value and an operand.");
            }
            return;
        }
        if (args.get(0).equalsIgnoreCase(args.get(1))) {
            output.print("true");
        } else {
            output.print("false");
        }
    }

    private void startswith(List<String> args, ScriptContext context, OutputHandler output) {
        if (args.size() < 1) {
            if (context.verbose) {
                output.print("str: Not enough arguments provided. You must specify a string value and an operand.");
            }
            return;
        }
        if (args.get(0).startsWith(args.get(1))) {
            output.print("true");
        } else {
            output.print("false");
        }
    }

    private void endswith(List<String> args, ScriptContext context, OutputHandler output) {
        if (args.size() < 1) {
            if (context.verbose) {
                output.print("str: Not enough arguments provided. You must specify a string value and an operand.");
            }
            return;
        }
        if (args.get(0).endsWith(args.get(1))) {
            output.print("true");
        } else {
            output.print("false");
        }
    }

    private void substring(List<String> args, ScriptContext context, OutputHandler output) {
        if (args.size() < 1) {
            if (context.verbose) {
                output.print("str: Not enough arguments provided. You must specify a string value and a starting index. An ending index (exclusive) may be optionally provided.");
            }
            return;
        }
        if (args.size() < 3) {
            try {
                output.print(args.get(0).substring(Integer.parseInt(args.get(1))));
            } catch (Exception ex) {
                output.failure("str: Failed to perform substring operation.",
                        "Failed to perform substring.",
                        "An exception occurred.", ex);
            }
        } else {
            try {
                output.print(args.get(0).substring(Integer.parseInt(args.get(1)), Integer.parseInt(args.get(2))));
            } catch (Exception ex) {
                output.failure("str: Failed to perform substring operation.",
                        "Failed to perform substring.",
                        "An exception occurred.", ex);
            }
        }
    }
}
