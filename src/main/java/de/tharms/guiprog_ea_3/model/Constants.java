package de.tharms.guiprog_ea_3.model;

import de.tharms.guiprog_ea_3.view.ColorCodes;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;

/**
 * Dieses Interface enthält alle im Programm benutzten Konstanten.
 */
public interface Constants
{
    String OUTPUT_GREET_USER = "\nWillkommen zur GUIPROG Entwicklungsarbeit!\n";

    int INDEX_ONE = 1;
    double NUMBERS_ZERO_DOT_FIVE = 0.5;
    int INDEX_ZERO = 0;
    int INDEX_TWO = 2;
    int INDEX_THREE = 3;
    int INDEX_FOUR = 4;
    int INDEX_FIVE = 5;

    int STL_BINARY_HEADER_LENGTH = 80;
    int STL_NORMAL_NUMBER_OF_DIMENSIONS = 3;
    int STL_NUMBER_OF_VERTICES = 3;
    int STL_VERTEX_NUMBER_OF_DIMENSIONS = 3;
    int ERROR_CODE_EXIT = 1;
    int TETRAHEDRON_VOLUME_FACTOR = 6;
    int EULER_CHARACTERISTIC_CLOSED_POLYHEDRON = 2;
    int SQUARED = 2;

    String OUTPUT_READING_FILE_START_INFO = "Folgende Datei wird eingelesen...";
    String FILE_NOT_FOUND = "Die angegebene Datei wurde nicht gefunden";
    String FILENAME_USER_INPUT = "Bitte Namen der Datei eingeben, welche eingelesen werden soll:";
    String DEFAULT_FILEPATH = "./src/main/resources/";
    String STL_ASCII_KEYWORD_SOLID = "solid";
    String FILENAME_VALID_FORMAT = ".stl";
    String OUTPUT_SEPARATOR = "  |";
    String INVALID_FILE_FORMAT = "Die Datei hat ein ungültiges Dateiformat";
    String OUTPUT_PARTIAL_SORTED_LIST = "Ausgabe der 3 kleinsten und größten Faces des Polyeders:";
    String OUTPUT_VERTICES = "Vertices: ";
    String OUTPUT_NORMAL = " Normale: ";
    String OUTPUT_AREA = "Flächeninhalt: ";
    String STL_ASCII_KEYWORD_OUTER_LOOP = "outer loop";
    String STL_ASCII_KEYWORD_ENDLOOP = "endloop";
    String INVALID_FACE_VERTEX = "Vertex vom Face ist ungültig";
    String STL_ASCII_KEYWORD_VERTEX = "vertex";
    String EMPTY_STRING = "";
    String STL_ASCII_SPLIT_VERTICES_REGEX = "^vertex\\s+";
    String STL_ASCII_SPLIT_REGEX = "\\s+";
    String STL_ASCII_SPLIT_NORMAL_REGEX = "^facet normal\\s+";
    String INVALID_FACE_NORMAL = "Normale vom Face ist ungültig";
    String STL_ASCII_KEYWORD_FACET_NORMAL = "facet normal";
    String STL_ASCII_KEYWORD_ENDFACET = "endfacet";
    String TRIANGLE_ILLEGAL_AMOUNT_OF_EDGES = "Ein Dreieck darf nur genau drei Edges haben";
    String FILE_READING_ERROR = "Fehler beim Einlesen der Datei";
    String OUTPUT_SURFACE_AREA = "Der Oberflächeninhalt beträgt: ";
    String OUTPUT_VOLUME = "Das Volumen beträgt: ";

    int ALLOWED_ARGS = 1;
    String INVALID_NUMBER_OF_ARGS = "Ungültige Anzahl an Argumenten übergeben";
    String NO_ARGUMENT = "Es wurde kein Argument übergeben";
    String OUTPUT_UNITS_MILLISECONDS = " ms";
    String OUTPUT_TIME_PASSED = "Benötigte Zeit zum %s: ";
    String SAMPLE_STL_FILEPATH = "./src/main/resources/xyz_cube.stl";
    String EXCEPTION_POLYHEDRON_FACES_IS_NULL = "Faces im Polyeder sind null";
    String STL_ASCII_SPLIT_NAME_REGEX = "^\\s*solid\\s+(name\\s+)?";
    String DEFAULT_PROGRAM_TITLE = "STL-Viewer";
    String SEPERATOR = ": ";
    String UNITS_CM_2 = " cm²";
    String UNITS_CM_3 = " cm³";
    int NUMBERS_ZERO = 0;
    int NUMBERS_ONE = 1;
    int NUMBERS_SIX = 6;
    int NUMBERS_THREE = 3;
    int TRANSLATE_DEFAULT_VALUE = -800;
    int CAMERA_DEFAULT_DISTANCE = 400;
    double X_DEFAULT_ROTATION_FACTOR = 0.1;
    double Y_DEFAULT_ROTATION_FACTOR = 0.1;
    double Z_DEFAULT_ROTATION_FACTOR = 0.1;
    double DEFAULT_SCROLLING_FACTOR = 0.5;
    int CAMERA_MAX_DISTANCE = -750;
    int CAMERA_MIN_DISTANCE = -10;
    double CAMERA_DEFAULT_X_ANGLE = 235;
    double CAMERA_DEFAULT_Y_ANGLE = 0;
    double TRANSLATE_DEFAULT_Z = -200;
    double CAMERA_DEFAULT_Z = -400;
    double CAMERA_NEAR_CLIP = 0.1;
    double CAMERA_FAR_CLIP = 10000;
    String INVALID_STL_FILE = "Ungültige .stl-Datei";
    String INVALID_STL_FILE_MESSAGE = "Die Datei %n\"%s\"%n konnte nicht als gültige .stl-Datei " +
            "gelesen werden.%n%nFehlermeldung: %s";
    Material DEFAULT_MESHVIEW_MATERIAL = new PhongMaterial(Color.DARKGRAY);

    String AREA_CALCULATION_PARALLEL = "parallelen Berechnen des Oberflächeninhalts";
    String AREA_CALCULATION_SERIAL = "sequentiellen Berechnen des Oberflächeninhalts";
    String VOLUME_CALCULATION = "Berechnen des Volumens";
    String CHECKBOX_SHOW_WIREFRAME_ONLY = "Nur Wireframe anzeigen";
    String CHECKBOX_SHOW_COORDINATE_SYSTEM = "Koordinatensystem anzeigen";
    int SIDEBAR_VBOX_SIZE = 15;
    String SIDEBAR_VBOX_STYLE = "-fx-background-color: rgba(255,255,255,0.95); -fx-padding: 20; -fx-min-width: 250;";
    String MENU_FILE = "Datei";
    String MENU_QUIT_PROGRAM = "Beenden";
    String MENU_OPEN_STL_FILE = "STL-Datei öffnen";
    String MENU_SELECT_STL_FILE = "STL-Datei auswählen";
    String MENU_LOAD_EXAMPLE_STL_FILE = "Beispiel STL-Datei laden";
    String MENU_VIEW = "Ansicht";
    String MENU_RESET_MODEL_POSITION = "Modell-Position zurücksetzen";
    String MENU_RESET_COORDINATE_SYSTEM = "Koordinatensystem zurücksetzen";
    String MENU_HELP = "Hilfe";
    String MENU_ABOUT_STL_VIEWER = "Über STL-Viewer";
    String MENU_STL_VIEWER_DETAILS = "Dieser STL-Viewer ist das Projekt der Entwicklungsarbeit aus dem Modul \"GUIPROG\"";
    String POLYHEDRON_DETAILS_VBOX_STYLE = """
        -fx-background-color: rgba(255, 255, 255, 0.9);
        -fx-padding: 15;
        -fx-border-color: lightgray;
        -fx-border-radius: 10;
        -fx-background-radius: 10;
    """;
    String SIDEBAR_POLYHEDRON_INFORMATION = "Polyeder-Informationen";
    String SIDEBAR_POLYHEDRON_INFORMATION_STYLE = "-fx-font-weight: bold; -fx-font-size: 16px;";
    String SIDEBAR_NO_POLYHEDRON_LOADED = "Noch kein Modell geladen";
    String SIDEBAR_NAME_POLYHEDRON = "Name des Polyeders";
    String SIDEBAR_NUMBER_OF_FACES = "Anzahl der Faces";
    String SIDEBAR_SURFACE_AREA = "Oberflächeninhalt";
    String SIDEBAR_VOLUME = "Volumen";
    int TEXTFIELD_MARGIN = 5;
    String STL_VIEWER_ICON_FILEPATH = "/stl_viewer_icon.png";
    int STL_VIEWER_WINDOW_WIDTH = 1200;
    int STL_VIEWER_WINDOW_HEIGHT = 800;
    int STL_VIEWER_SUBSCENE_WIDTH = 1000;
    int STL_VIEWER_SUBSCENE_HEIGHT = 700;
    int COORDINATE_AXIS_LENGTH = 200;
    double COORDINATE_AXIS_DIAMETER = 0.2;
    int COORDINATE_AXIS_Z_ROTATE = 90;
    int GRID_SIZE = 100;
    double GRID_THICKNESS = 0.05;
    int GRID_STEP = 10;
    int NUMBERS_TWO = 2;

    String STL_FILE_FORMAT = "Die Datei liegt im %s vor";
    String ASCII_STL_FILE_FORMAT = "ASCII-Format";
    String BINARY_STL_FILE_FORMAT = "Binary-Format";
    String SOCKET_CREATION_FAILURE = "Fehler beim Erstellen des Sockets: ";
    String INVALID_FLOAT_VALUE = "Fehlerhafter Wert angegeben: ";
    int TCP_PORT = 1234;
    String TCP_HOSTNAME = "localhost";
    String CLIENT_CONSOLE_INFO = "Befehl oder exit eingeben";
    String COMMAND_EXIT = "exit";
    String CLIENT_INPUT_MESSAGE_SPLIT_REGEX = "\\s+";
    String INVALID_COMMAND = "Der eingegebene Befehl ist ungültig";
    String CONNECTION_CLOSED = "Verbindung wird geschlossen";
    String SERVER_RESPONSE_MESSAGE = "Antwort des Servers: ";
    String SERVER_RUNNING_MESSAGE = "Server läuft auf Port ";
    String SERVER_CONNECTION_FROM = "Verbindung von ";
    String COMMAND_EXECUTED = "Befehl wurde ausgeführt";
    String COMMAND_ROTATE = "ROTATE";
    String COMMAND_TRANSLATE = "TRANSLATE";
    double X_DEFAULT_TRANSLATION_FACTOR = 0.1;
    double Y_DEFAULT_TRANSLATION_FACTOR = 0.1;
    double CAMERA_DEFAULT_Y = 50;
    String CLIENT_START_INFO = "Client gestartet:\nVerbunden mit Server " + ColorCodes.BLUE
            + "%s:%s" + ColorCodes.RESET;
    String COMMAND_SYNTAX = "Befehlssyntax: " + ColorCodes.PURPLE +
            "\n[Aktion (translate; rotate)]\n[Achse (X; Y; Z)]\n[Wert (Zahl)]" + ColorCodes.RESET;
    int STL_VIEWER_WINDOW_MIN_WIDTH = 900;
    int STL_VIEWER_WINDOW_MIN_HEIGHT = 700;
    String PROGRAM_INSTRUCTIONS = "Kurzanleitung";
    String STL_VIEWER_INSTRUCTIONS = "Kurzanleitung für den STL-Viewer";
    String POLYEDER_EULER_INFORMATION = "Die Euler-Charakteristik des Polyeders beträgt " + ColorCodes.GREEN + "%s"
            + ColorCodes.RESET + ", womit es sich um einen %s Polyeder handelt.";
    String POLYEDER_EULER_CLOSED = "geschlossenen";
    String POLYEDER_EULER_OPEN = "offenen";
    String EXCEPTION_ROUND_PLACES_LOWER_THAN_ZERO = "Der gerundete Wert darf nicht kleiner als Null sein";
    int ROUND_VALUE_PLACES = 3;
    String PROGRAM_INSTRUCTIONS_TEXT = "1. Über das Menü 'Datei' lässt sich eine STL-Datei öffnen: " +
            "'STL-Datei öffnen' lädt eigene Dateien, 'Beispiel STL-Datei laden' zeigt ein Beispielmodell an.\n" +
            "2. Modell und Koordinatensystem lassen sich mit der Maus steuern:\n" +
            " - Linksklick dreht das Modell\n" +
            " - Rechtsklick verschiebt das Modell\n" +
            " - Scrollrad zoomt heran und heraus\n" +
            " - Shift + Linksklick bewegt die Kamera im Raum\n" +
            "3. Die Ansicht lässt sich in der Seitenleiste nach Bedarf anpassen.\n" +
            "4. Unter 'Polyeder-Informationen' in der Seitenleiste erscheinen Details zum aktuell geladenen Modell.\n" +
            "5. Im Menü 'Ansicht' lässt sich das Koordinatensystem oder die Modellposition jederzeit zurücksetzen.\n";
    double NUMBERS_ZERO_DOUBLE = 0.0;
}
