package de.tharms.guiprog_ea_3.model;

public class Command
{
    public ServerCommands action;
    public Axis axis;
    public float value;

    public Command(ServerCommands action, Axis axis, float value)
    {
        this.action = action;
        this.axis = axis;
        this.value = value;
    }

    public Command()
    {
    }
}
