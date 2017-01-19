# HackBot
A shell interpreter bot for Discord, meant to resemble the iconic "HackEgo" bot of the esoteric IRC channel

This project started out as a full Bash implementation, but then got downsized to more realistic expectations.

The current shell is accessed in chat by tagging the bot in a message or by sending it a DM. Command tokens are space-delimited. Surrounding arguments in double-quotes allows spaces to be used in an argument. The Discord markup for code segments (\` and \`\`\` sequences) are also treated as sequences with spaces included. Variables and script arguments are inserted using `${variable}`. Command output can be inserted using `$(command)`.

All commands are run inside a directory unique to each guild. Paths are delimited with forward-slashes, but do not begin with '/' like typical ext paths. Each user also gets a private directory, accessible with `~/`. DM'ed commands are run inside this directory as well, and in that context the root directory and user folder are the same.

Commands implemented thus far include basic operations for files, directories, strings, variables, and condition execution. Refer to the handler classes in `com.greenlock.hackbot.handlers` to see what commands are available.
