package de.tharms.guiprog_ea_3.main;

import de.tharms.guiprog_ea_3.controller.ArgumentController;
import de.tharms.guiprog_ea_3.controller.PolyhedronController;
import de.tharms.guiprog_ea_3.model.Polyhedron;
import de.tharms.guiprog_ea_3.view.Output;
import de.tharms.guiprog_ea_3.controller.ViewerController;
import javafx.application.Application;

/**
 * Startklasse des STL-Viewers. Liest eine STL-Datei, berechnet Oberfläche und Volumen
 * eines Polyeders und gibt diese Informationen aus.
 */
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
        Output.greetUser();

        ArgumentController argsController = new ArgumentController(args);

        Polyhedron polyhedron = PolyhedronController.createPolyhedronFromSTL(argsController.getFilepath());

        Output.printSurfaceArea(polyhedron.getSurfaceArea());

        Output.printVolume(polyhedron.getVolume());

        Output.partiallyPrintSortedList(PolyhedronController.sortFacesBySize(polyhedron));

        Application.launch(ViewerController.class, argsController.getFilepath());


        //PolyhedronController.calculateSurfaceAreaUsingThreads(polyhedron);
        //PolyhedronController.calculateSurfaceAreaUsingThreads2(polyhedron);
    }
}
