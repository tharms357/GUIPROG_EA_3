package de.tharms.guiprog_ea_3.model;

/**
 * Definiert die vom Server unterstützten Befehle für Modelltransformationen oder Programmende.
 */
public enum ServerCommands
{
    ROTATE(Constants.COMMAND_ROTATE),
    TRANSLATE(Constants.COMMAND_TRANSLATE),
    EXIT(Constants.COMMAND_EXIT);

    private final String command;

    /**
     * Legt einen neuen ServerCommand mit dem zugehörigen String-Repräsentanten an.
     *
     * @param command Der String, mit dem dieser Befehl identifiziert wird.
     * @Vorbedingung command darf nicht null sein.
     * @Nachbedingung Das Enum-Element enthält den angegebenen String in der {@code command}-Variable.
     */
    ServerCommands(String command)
    {
        this.command = command;
    }

    public String getCommand()
    {
        return command;
    }
}
