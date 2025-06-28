package de.tharms.guiprog_ea_3.model;

public interface Constants
{
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
    int EULER_CHARACTERISTIC_CONVEX_POLYHEDRON = 2;
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
    String INVALID_FACE_VERTEX = "Vertex vom Face ist ungülig";
    String STL_ASCII_KEYWORD_VERTEX = "vertex";
    String EMPTY_STRING = "";
    String STL_ASCII_SPLIT_VERTICES_REGEX = "^vertex\\s+";
    String STL_ASCII_SPLIT_REGEX = "\\s+";
    String STL_ASCII_SPLIT_NORMAL_REGEX = "^facet normal\\s+";
    String INVALID_FACE_NORMAL = "Normale vom Face ist ungültig";
    String STL_ASCII_KEYWORD_FACET_NORMAL = "facet normal";
    String STL_ASCII_KEYWORD_ENDFACET = "endfacet";
    String TRIANGLE_ILLEGAL_AMOUNT_OF_EDGES = "Ein Dreieck darf nur genau drei Edges haben";
    String FILE_READING_ERROR = "Fehler beim Einlesen der Datei: ";
    String OUTPUT_SURFACE_AREA = "Der Oberflächeninhalt des Polyeders beträgt: ";
    String OUTPUT_VOLUME = "Volumen: ";

    int ALLOWED_ARGS = 1;
    String INVALID_NUMBER_OF_ARGS = "Ungültige Anzahl an Argumenten übergeben";
    String NO_ARGUMENT = "Es wurde kein Argument übergeben";
    String OUTPUT_UNITS_MILLISECONDS = " ms";
    String OUTPUT_TIME_PASSED = "Benötigte Zeit: ";
}
