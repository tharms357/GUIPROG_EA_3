package de.tharms.guiprog_ea_3.main;

import de.tharms.guiprog_ea_3.controller.ArgumentController;
import de.tharms.guiprog_ea_3.controller.PolyhedronController;
import de.tharms.guiprog_ea_3.model.Polyhedron;
import de.tharms.guiprog_ea_3.utility.STLReader;
import de.tharms.guiprog_ea_3.view.Output;

public class Main
{
    /**
     * Hauptmethode, die das Programm startet.
     * Liest eine STL-Datei ein, berechnet und gibt die Oberfläche, sortierte Flächen und das Volumen des Polyeders aus.
     *
     * @param args Argumente der Kommandozeile
     *
     * @Vorbedingung Die Eingabedatei muss existieren und ein gültiges STL-Format besitzen.
     * @Nachbedingung Die Oberfläche, eine sortierte Liste der Flächen nach Größe und das Volumen werden ausgegeben.
     */
    public static void main(String[] args)
    {
        ArgumentController argsController = new ArgumentController(args);

        Polyhedron polyhedron = STLReader.createPolyhedronFromSTL(argsController.getFilepath());

        PolyhedronController.calculateAreaUsingThreads(polyhedron);

        Output.printSurfaceArea(PolyhedronController.calculateSurfaceArea(polyhedron));

        Output.partiallyPrintSortedList(PolyhedronController.sortFacesBySize(polyhedron));

        Output.printVolume(PolyhedronController.calculateVolume(polyhedron));

        PolyhedronController.isConvex(polyhedron);
    }
}
