package com.greenlock.hackbot.handlers;

import com.greenlock.hackbot.output.OutputHandler;
import com.greenlock.hackbot.parsers.exec.ScriptContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LukeSmalley on 1/18/2017.
 */
public class FileCommandHandler extends CommandHandler {

    public FileCommandHandler() {
        super(CommandHandlerScope.UNIVERSAL);
    }

    @Override
    protected void handle(List<String> args, ScriptContext context, OutputHandler output) {
        if (args.size() < 1) {
            if (context.verbose) {
                output.print("file: Not enough arguments provided. You must specify a subcommand.");
            }
            return;
        }
        String subcommand = args.get(0);
        if (subcommand.equalsIgnoreCase("read")) {
            read(subList(args, 1), context, output);
        } else if (subcommand.equalsIgnoreCase("write")) {
            write(subList(args, 1), context, output);
        } else if (subcommand.equalsIgnoreCase("append")) {
            append(subList(args, 1), context, output);
        } else if (subcommand.equalsIgnoreCase("create")) {
            create(subList(args, 1), context, output);
        } else if (subcommand.equalsIgnoreCase("delete")) {
            delete(subList(args, 1), context, output);
        } else if (subcommand.equalsIgnoreCase("exists")) {
            exists(subList(args, 1), context, output);
        } else {
            if (context.verbose) {
                output.print("file: '" + args.get(0) + "' is not a valid subcommand.");
            }
        }
    }

    private List<String> subList(List<String> list, int index) {
        return index < list.size() ? list.subList(index, list.size()) : new ArrayList<>();
    }


    private void read(List<String> args, ScriptContext context, OutputHandler output) {
        if (args.size() < 1) {
            if (context.verbose) {
                output.print("file: Not enough arguments provided. You must specify a file name.");
            }
            return;
        }
        if (context.getEnvironment().getWorkingDirectory().hasFile(args.get(0))) {
            try {
                output.print(context.getEnvironment().getWorkingDirectory().getFile(args.get(0)));
            } catch (IOException ex) {
                output.failure("file: Failed to read file '" + args.get(0) + "'.",
                        "Failed to read file.",
                        "An exception occurred.", ex);
            }
        } else {
            if (context.verbose) {
                output.print("file: The file '" + args.get(0) + "' does not exist.");
            }
        }
    }

    private void write(List<String> args, ScriptContext context, OutputHandler output) {
        if (args.size() < 2) {
            if (context.verbose) {
                output.print("file: Not enough arguments provided. You must specify a file name and a value to be written.");
            }
            return;
        }
        try {
            context.getEnvironment().getWorkingDirectory().writeFile(args.get(0), args.get(1));
        } catch (IOException ex) {
            output.failure("file: Failed to write to file '" + args.get(0) + "'.",
                    "Failed to write file.",
                    "An exception occurred.", ex);
        }
    }

    private void append(List<String> args, ScriptContext context, OutputHandler output) {
        if (args.size() < 2) {
            if (context.verbose) {
                output.print("file: Not enough arguments provided. You must specify a file name and a value to be written.");
            }
            return;
        }
        try {
            context.getEnvironment().getWorkingDirectory().appendFile(args.get(0), args.get(1));
        } catch (IOException ex) {
            output.failure("file: Failed to append to file '" + args.get(0) + "'.",
                    "Failed to append file.",
                    "An exception occurred.", ex);
        }
    }

    private void create(List<String> args, ScriptContext context, OutputHandler output) {
        if (args.size() < 2) {
            if (context.verbose) {
                output.print("file: Not enough arguments provided. You must specify a file name and a value to be written.");
            }
            return;
        }
        try {
            output.print(context.getEnvironment().getWorkingDirectory().createFile(args.get(0), args.get(1)));
        } catch (IOException ex) {
            output.failure("file: Failed to create file '" + args.get(0) + "'.",
                    "Failed to create file.",
                    "An exception occurred.", ex);
        }
    }

    private void delete(List<String> args, ScriptContext context, OutputHandler output) {
        if (args.size() < 1) {
            if (context.verbose) {
                output.print("file: Not enough arguments provided. You must specify a file name.");
            }
            return;
        }
        try {
            context.getEnvironment().getWorkingDirectory().deleteFile(args.get(0));
        } catch (IOException ex) {
            output.failure("file: Failed to delete file '" + args.get(0) + "'.",
                    "Failed to delete file.",
                    "An exception occurred.", ex);
        }
    }

    private void exists(List<String> args, ScriptContext context, OutputHandler output) {
        if (args.size() < 1) {
            if (context.verbose) {
                output.print("file: Not enough arguments provided. You must specify a file name.");
            }
            return;
        }
        if (context.getEnvironment().getWorkingDirectory().hasFile(args.get(0))) {
            output.print("true");
        } else {
            output.print("false");
        }
    }
}
