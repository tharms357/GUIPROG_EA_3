package de.tharms.guiprog_ea_3.view;

import de.tharms.guiprog_ea_3.model.Constants;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Verwaltet die Erstellung der Benutzeroberfläche für den 3D-Viewer.
 */
public class UIController {

    private final ViewerController viewerController;
    private CheckBox showWireframe;
    private CheckBox showAxis;

    public UIController(ViewerController viewerController) {
        this.viewerController = viewerController;
    }

    /**
     * Erstellt die Menüleiste des Viewers.
     */
    public MenuBar createMenuBar() {
        Menu datei = new Menu("Datei");

        MenuItem loadSampleSTL = new MenuItem("Beispiel STL-Datei laden");
        loadSampleSTL.setOnAction(e -> {
            viewerController.setMeshView(Constants.SAMPLE_STL_FILEPATH);
            viewerController.resetModel();
            viewerController.updateDrawMode(showWireframe != null && showWireframe.isSelected());
        });

        MenuItem openFile = new MenuItem("STL-Datei öffnen...");
        openFile.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("STL-Datei auswählen");

            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                viewerController.setMeshView(selectedFile.getAbsolutePath());
                viewerController.updateDrawMode(showWireframe != null && showWireframe.isSelected());
            }
        });

        MenuItem beenden = new MenuItem("Beenden");
        beenden.setOnAction(e -> System.exit(0));

        datei.getItems().addAll(loadSampleSTL, openFile, beenden);

        Menu ansicht = new Menu("Ansicht");
        MenuItem resetModel = new MenuItem("Modell-Position zurücksetzen");
        MenuItem resetView = new MenuItem("Koordinatensystem zurücksetzen");

        resetModel.setOnAction(e -> viewerController.resetModel());

        resetView.setOnAction(e -> {
            viewerController.resetCameraView();
            if (showAxis != null) {
                showAxis.setSelected(true);
                showAxis.setVisible(true);
            }
            viewerController.updateShowAxis(true);
        });

        ansicht.getItems().addAll(resetModel, resetView);

        Menu hilfe = new Menu("Hilfe");
        MenuItem ueber = new MenuItem("Über STL Viewer");
        ueber.setOnAction(e -> {
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Über");
            info.setHeaderText("STL Viewer");
            info.setContentText("Ein einfacher 3D STL-Viewer\nAutor: Du :)");
            info.showAndWait();
        });
        hilfe.getItems().add(ueber);

        return new MenuBar(datei, ansicht, hilfe);
    }

    /**
     * Erstellt die Seitenleiste mit Anzeige- und Darstellungsoptionen.
     */
    public VBox createSidebar() {
        Label title = new Label("Modell-Informationen");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Label infoText = new Label("""
        - Modell: Felge_a.stl
        - Eckpunkte: ca. 12.000
        - Koordinatenursprung: (0,0,0)
        - Skalierung: 1.0x
        - Material: Phong
        """);
        infoText.setWrapText(true);

        showWireframe = new CheckBox("Nur Wireframe anzeigen");
        showWireframe.setOnAction(e -> viewerController.updateDrawMode(showWireframe.isSelected()));

        showAxis = new CheckBox("Koordinatensystem anzeigen");
        showAxis.setSelected(true);
        showAxis.setOnAction(e -> viewerController.updateShowAxis(showAxis.isSelected()));

        VBox sidebar = new VBox(15, title, infoText, showWireframe, showAxis);
        sidebar.setStyle("-fx-background-color: rgba(255,255,255,0.95); -fx-padding: 20; -fx-min-width: 250;");
        return sidebar;
    }
}
