package de.tharms.guiprog_ea_3.model;

/**
 * Repräsentiert einen Befehl, der vom Server an den Client gesendet werden kann,
 * und enthält Aktionstyp, Achse und Wert für Transformationen.
 */
public class Command
{
    public ServerCommands action;
    public Axis axis;
    public double value;

    /**
     * Erzeugt einen neuen Command mit den angegebenen Parametern.
     *
     * @param action Der vom Server gewünschte Befehlstyp ({@link ServerCommands}).
     * @param axis Die Achse, auf die sich die Aktion bezieht ({@link Axis}).
     * @param value Der Wert, um den rotiert oder verschoben wird.
     * @Vorbedingung action und axis dürfen nicht null sein.
     * @Nachbedingung Der Command ist mit den übergebenen Feldern initialisiert.
     */
    public Command(ServerCommands action, Axis axis, double value)
    {
        this.action = action;
        this.axis = axis;
        this.value = value;
    }

    /**
     * Standard-Konstruktor für Serialisierung und Deserialisierung.
     *
     * @Vorbedingung Keine.
     * @Nachbedingung Ein Command-Objekt ist erstellt, Felder sind auf Default-Werte gesetzt.
     */
    public Command()
    {
    }
}
