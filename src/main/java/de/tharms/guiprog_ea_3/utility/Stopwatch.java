package de.tharms.guiprog_ea_3.utility;

/**
 * Diese Klasse stellt eine Stoppuhr zur Verfügung, um die Bearbeitungszeit von Prozessen zu messen.
 * Das Singleton-Designmuster gewährleistet, dass immer nur ein einziges Objekt der Klasse erzeugt wird.
 */
public class Stopwatch
{
    //Singleton-Instanz der Stopwatch-Klasse
    private static Stopwatch instance = null;

    //Konstruktor der Stopwatch-Klasse
    private Stopwatch()
    {}

    /**
     * Diese Methode liefert die einzige Instanz der Stopwatch-Klasse.
     * @Vorbedingung  Die Klasse darf keine bestehende Instanz von {@code Stopwatch} haben, um eine neue zu erstellen.
     * @Nachbedingung  Eine Singleton-Instanz der Klasse {@code Stopwatch} wird zurückgegeben.
     * Falls bereits eine Instanz existiert, wird diese zurückgegeben.
     * Falls noch keine Instanz existiert, wird eine neue Instanz erstellt.
     * @return Die Singleton-Instanz der Stopwatch.
     */
    public static Stopwatch getInstance()
    {
        if(instance == null)
        {
            //Erstellen der Instanz, falls diese noch nicht existiert
            instance = new Stopwatch();
        }
        return instance;
    }

    //Deklarieren der Startzeit
    private long startTime;

    /**
     * Diese Methode initialisiert die Startzeit der Stoppuhr, indem sie die aktuelle Systemzeit in Millisekunden erfasst.
     * @Vorbedingung  keine
     * @Nachbedingung  "startTime" bekommt die aktuelle Systemzeit zugewiesen.
     */
    public void start()
    {
        //Initialisieren der Startzeit
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Diese Methode dient zur Berechnung der seit dem Start der Stoppuhr vergangenen Zeit.
     * @Vorbedingung  Die Stoppuhr wurde mit der Methode {@link #start()} gestartet.
     * @Nachbedingung  Die seit Start der Stoppuhr vergangene Zeit wird in Millisekunden zurückgegeben.
     * @return Die vergangene Zeit in Millisekunden seit dem Start der Stoppuhr.
     */
    public long stop()
    {
        //Rückgabe der seit Start der Stoppuhr vergangenen Zeit
        return (System.currentTimeMillis() - startTime);
    }
}
