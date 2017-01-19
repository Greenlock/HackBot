package com.greenlock.hackbot.handlers;

import com.greenlock.hackbot.output.OutputHandler;
import com.greenlock.hackbot.parsers.exec.ScriptContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LukeSmalley on 1/18/2017.
 */
public class DirCommandHandler extends CommandHandler {

    public DirCommandHandler() {
        super(CommandHandlerScope.UNIVERSAL);
    }

    @Override
    protected void handle(List<String> args, ScriptContext context, OutputHandler output) {
        if (args.size() < 1) {
            if (context.verbose) {
                output.print("dir: Not enough arguments provided. You must specify a subcommand.");
            }
            return;
        }
        String subcommand = args.get(0);
        if (subcommand.equalsIgnoreCase("exists")) {
            exists(subList(args, 1), context, output);
        } else if (subcommand.equalsIgnoreCase("create")) {
            create(subList(args, 1), context, output);
        } else if (subcommand.equalsIgnoreCase("delete")) {
            delete(subList(args, 1), context, output);
        } else {
            if (context.verbose) {
                output.print("dir: '" + args.get(0) + "' is not a valid subcommand.");
            }
        }
    }

    private List<String> subList(List<String> list, int index) {
        return index < list.size() ? list.subList(index, list.size()) : new ArrayList<>();
    }


    private void exists(List<String> args, ScriptContext context, OutputHandler output) {
        if (args.size() < 1) {
            if (context.verbose) {
                output.print("dir: Not enough arguments provided. You must specify a directory name.");
            }
            return;
        }
        if (context.getEnvironment().getWorkingDirectory().hasChild(args.get(0))) {
            output.print("true");
        } else {
            output.print("false");
        }
    }

    private void create(List<String> args, ScriptContext context, OutputHandler output) {
        if (args.size() < 1) {
            if (context.verbose) {
                output.print("dir: Not enough arguments provided. You must specify a directory name.");
            }
            return;
        }
        try {
            context.getEnvironment().getWorkingDirectory().createChild(args.get(0));
        } catch (IOException ex) {
            output.failure("dir: Failed to create directory '" + args.get(0) + "'.",
                    "Failed to create directory.",
                    "An exception occurred.", ex);
        }
    }

    private void delete(List<String> args, ScriptContext context, OutputHandler output) {
        if (args.size() < 1) {
            if (context.verbose) {
                output.print("dir: Not enough arguments provided. You must specify a directory name.");
            }
            return;
        }
        if (context.getEnvironment().getWorkingDirectory().hasChild(args.get(0))) {
            try {
                context.getEnvironment().getWorkingDirectory().getChild(args.get(0)).delete();
            } catch (IOException ex) {
                output.failure("dir: Failed to delete directory '" + args.get(0) + "'.",
                        "Failed to delete directory.",
                        "An exception occurred.", ex);
            }
        } else {
            if (context.verbose) {
                output.print("dir: The directory '" + args.get(0) + "' does not exist.");
            }
        }
    }
}
