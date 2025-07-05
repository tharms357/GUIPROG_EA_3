package de.tharms.guiprog_ea_3.model;

public enum ServerCommands
{
    ROTATE(Constants.COMMAND_ROTATE),
    TRANSLATE(Constants.COMMAND_TRANSLATE),
    EXIT(Constants.COMMAND_EXIT);

    private final String command;

    ServerCommands(String command)
    {
        this.command = command;
    }

    public String getCommand()
    {
        return command;
    }
}
