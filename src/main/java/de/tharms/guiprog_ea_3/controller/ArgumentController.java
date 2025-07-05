package de.tharms.guiprog_ea_3.controller;

import de.tharms.guiprog_ea_3.model.Constants;

/**
 * Steuert die Validierung und Verarbeitung der Programmargumente für den STL-Viewer.
 */
public class ArgumentController
{
    String filepath;

    /**
     * Initialisiert einen {@link ArgumentController} mit den übergebenen Programmargumenten.
     * Validiert die Anzahl der Argumente und setzt den Dateipfad.
     *
     * @param args Array der Programmargumente.
     * @Vorbedingung Das args-Array darf nicht leer sein.
     * @Nachbedingung Der Dateipfad wurde auf Basis des ersten Arguments gesetzt oder es wurde eine IllegalArgumentException geworfen.
     */
    public ArgumentController(String[] args)
    {
        if (args.length == 0)
        {
            throw new IllegalArgumentException(Constants.NO_ARGUMENT);
        }
        else if (args.length != Constants.ALLOWED_ARGS)
        {
            throw new IllegalArgumentException(Constants.INVALID_NUMBER_OF_ARGS);
        }
        else
        {
            filepath = Constants.DEFAULT_FILEPATH + args[Constants.INDEX_ZERO];
        }
    }

    public String getFilepath()
    {
        return filepath;
    }
}
