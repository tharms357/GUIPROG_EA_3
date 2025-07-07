package de.tharms.guiprog_ea_3.controller;

import de.tharms.guiprog_ea_3.model.Axis;
import de.tharms.guiprog_ea_3.model.Constants;
import de.tharms.guiprog_ea_3.model.Polyhedron;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;

import java.io.File;


/**
 * Steuert die Benutzeroberfläche des STL-Viewers, einschließlich Menüleiste und Seitenleiste.
 */
public class InteractionController
{
    private double anchorX, anchorY;
    private double cameraAngleX, cameraAngleZ;
    private double objectAngleX, objectAngleY, objectAngleZ;
    private double objectTranslateX, objectTranslateY;

    private Group axesGroup = new Group();

    private CheckBox showWireframe;
    private CheckBox showAxis;

    private VBox polyhedronDetails;

    public void addMouseControl(SubScene subScene, ModelController modelController,
                                CameraController cameraController)
    {
        subScene.setOnMousePressed(mouseEvent -> mouseIsPressed(
                mouseEvent, modelController, cameraController));
        subScene.setOnMouseDragged(mouseEvent -> mouseIsDragged(
                mouseEvent, modelController, cameraController));
        subScene.setOnScroll(scrollEvent -> mouseIsScrolled(
                scrollEvent, cameraController));
    }

    private void mouseIsPressed(MouseEvent mouseEvent, ModelController modelController,
                                CameraController cameraController)
    {
        anchorX = mouseEvent.getSceneX();
        anchorY = mouseEvent.getSceneY();
        cameraAngleX =  cameraController.getCameraRotateX().getAngle();
        cameraAngleZ = cameraController.getCameraRotateY().getAngle();
        objectAngleX = modelController.getRotateX().getAngle();
        objectAngleY = modelController.getRotateY().getAngle();
        objectAngleZ = modelController.getRotateZ().getAngle();
        objectTranslateX = modelController.getTranslate().getX();
        objectTranslateY = modelController.getTranslate().getY();
    }

    private void mouseIsDragged(MouseEvent mouseEvent, ModelController modelController,
                                CameraController cameraController)
    {
        double deltaX = mouseEvent.getSceneX() - anchorX;
        double deltaY = mouseEvent.getSceneY() - anchorY;

        if (mouseEvent.isPrimaryButtonDown() && mouseEvent.isShiftDown())
        {
            cameraController.rotateCamera(Axis.X,
                    -deltaY * Constants.X_DEFAULT_ROTATION_FACTOR, cameraAngleX);
            cameraController.rotateCamera(Axis.Z,
                    deltaX * Constants.Z_DEFAULT_ROTATION_FACTOR, cameraAngleZ);
        }
        else if (mouseEvent.isSecondaryButtonDown())
        {
            modelController.translateObject(Axis.X,
                    deltaX * Constants.X_DEFAULT_TRANSLATION_FACTOR, objectTranslateX);
            modelController.translateObject(Axis.Y,
                    -deltaY * Constants.Y_DEFAULT_TRANSLATION_FACTOR, objectTranslateY);
        }
        else if (mouseEvent.isPrimaryButtonDown())
        {
            modelController.rotateObject(Axis.X,
                    deltaY * Constants.X_DEFAULT_ROTATION_FACTOR, objectAngleX);
            modelController.rotateObject(Axis.Z,
                    deltaX * Constants.Y_DEFAULT_ROTATION_FACTOR, objectAngleZ);
        }
    }

    public void mouseIsScrolled(ScrollEvent scrollEvent,
                                CameraController cameraController)
    {
        double zoom = cameraController.getCameraTranslate().getZ() +
                scrollEvent.getDeltaY() * Constants.DEFAULT_SCROLLING_FACTOR;

        cameraController.getCameraTranslate().setZ(
                Math.max(Constants.CAMERA_MAX_DISTANCE,
                Math.min(Constants.CAMERA_MIN_DISTANCE, zoom)));
    }

    /**
     * Erstellt die Menüleiste mit Datei-, Ansicht- und Hilfe-Menüs.
     *
     * @param viewerController Der Controller zur Anbindung von Aktionen.
     * @return Eine {@link MenuBar} mit den definierten Menüs.
     * @Vorbedingung viewerController darf nicht null sein.
     * @Nachbedingung Die Menüleiste ist einsatzbereit.
     */
    public MenuBar createMenuBar(ViewerController viewerController)
    {
        Menu fileMenu = createFileMenu(viewerController);
        Menu viewMenu = createViewMenu(viewerController);
        Menu helpMenu = createHelpMenu();

        return new MenuBar(fileMenu, viewMenu, helpMenu);
    }

    /**
     * Erstellt die Seitenleiste mit Polyeder-Details und Anzeigeoptionen.
     *
     * @param viewerController Der Controller zur Anbindung von Aktionen.
     * @return Ein {@link VBox}-Container für die Seitenleiste.
     * @Vorbedingung viewerController darf nicht null sein.
     * @Nachbedingung Die Seitenleiste ist einsatzbereit.
     */
    public VBox createSidebar(ViewerController viewerController)
    {
        polyhedronDetails = createDefaultPolyhedronDetails();
        showWireframe = createShowWireframeCheckBox(viewerController);
        showAxis = createShowAxisCheckBox();

        VBox sidebar = new VBox(Constants.SIDEBAR_VBOX_SIZE, polyhedronDetails, showAxis, showWireframe);
        sidebar.setStyle(Constants.SIDEBAR_VBOX_STYLE);
        return sidebar;
    }

    private CheckBox createShowWireframeCheckBox(ViewerController viewerController)
    {
        CheckBox showWireframe = new CheckBox(Constants.CHECKBOX_SHOW_WIREFRAME_ONLY);
        showWireframe.setOnAction(actionEvent -> {
            updateDrawMode(viewerController.getModelController().getMeshView());
        });
        return showWireframe;
    }

    private CheckBox createShowAxisCheckBox()
    {
        CheckBox showAxis = new CheckBox(Constants.CHECKBOX_SHOW_COORDINATE_SYSTEM);
        showAxis.setSelected(true);
        showAxis.setOnAction(actionEvent -> {
            updateShowAxis();
        });
        return showAxis;
    }


    /**
     * Erstellt das Datei-Menü mit Optionen zum Laden einer Beispiel-STL, Öffnen einer beliebigen Datei
     * und Beenden des Programms.
     *
     * @param viewerController Der {@link ViewerController}, an den die Menüaktionen gebunden werden.
     * @return Ein {@link Menu} mit den Einträgen zum Laden der Beispiel-STL, Öffnen einer Datei und Beenden.
     * @Vorbedingung   viewerController darf nicht null sein.
     * @Nachbedingung  Das Datei-Menü wurde erstellt und ist sofort einsatzbereit.
     */
    private Menu createFileMenu(ViewerController viewerController)
    {
        Menu file = new Menu(Constants.MENU_FILE);

        MenuItem loadSample = createLoadSampleItem(viewerController);
        MenuItem openFile = createOpenFileItem(viewerController);
        MenuItem quitProgram = new MenuItem(Constants.MENU_QUIT_PROGRAM);
        quitProgram.setOnAction(actionEvent -> {
            System.exit(0);
        });


        file.getItems().addAll(loadSample, openFile, quitProgram);

        return file;
    }

    /**
     * Aktualisiert den Drawmode des MeshViews basierend auf der Wireframe-CheckBox.
     *
     * @param meshView Das zu aktualisierende {@link MeshView}-Objekt.
     * @Vorbedingung meshView darf nicht null sein.
     * @Nachbedingung DrawMode ist auf {@link DrawMode#LINE} oder {@link DrawMode#FILL} gesetzt.
     */
    public void updateDrawMode(MeshView meshView)
    {
        if (meshView != null && getShowWireframe() != null)
        {
            if (getShowWireframe().isSelected())
            {
                meshView.setDrawMode(DrawMode.LINE);
            }
            else
            {
                meshView.setDrawMode(DrawMode.FILL);
            }
        }
    }

    /**
     * Erstellt den Menüpunkt zum Öffnen einer Datei im Datei-Menü.
     *
     * @param viewerController Der Controller zur Anbindung der Aktion.
     * @return Ein {@link MenuItem} für das Öffnen einer STL-Datei.
     * @Vorbedingung viewerController darf nicht null sein.
     * @Nachbedingung Menüpunkt ist funktional verbunden.
     */
    private MenuItem createOpenFileItem(ViewerController viewerController)
    {
        MenuItem openFile = new MenuItem(Constants.MENU_OPEN_STL_FILE);
        openFile.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(Constants.MENU_SELECT_STL_FILE);

            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null)
            {
                viewerController.setMeshView(selectedFile.getAbsolutePath());
            }
        });

        return openFile;
    }

    /**
     * Erstellt den Menüpunkt zum Laden einer Beispiel-STL im Datei-Menü.
     *
     * @param viewerController Der Controller zur Anbindung der Aktion.
     * @return Ein {@link MenuItem} für das Laden eines Beispiel-STL.
     * @Vorbedingung viewerController darf nicht null sein.
     * @Nachbedingung Menüpunkt ist funktional verbunden.
     */
    private MenuItem createLoadSampleItem(ViewerController viewerController)
    {
        MenuItem loadSample = new MenuItem(Constants.MENU_LOAD_EXAMPLE_STL_FILE);
        loadSample.setOnAction(actionEvent -> {
            viewerController.setMeshView(Constants.SAMPLE_STL_FILEPATH);
            viewerController.getModelController().resetModelPosition();
        });

        return loadSample;
    }

    /**
     * Erstellt das Ansicht-Menü mit Aktionen zum Zurücksetzen der Ansicht.
     *
     * @param viewerController Der Controller zur Anbindung der Aktion.
     * @return Ein {@link Menu} mit Ansichtseinstellungen.
     * @Vorbedingung viewerController darf nicht null sein.
     * @Nachbedingung Menü ist funktional verbunden.
     */
    private Menu createViewMenu(ViewerController viewerController)
    {
        Menu view = new Menu(Constants.MENU_VIEW);

        MenuItem resetModel = new MenuItem(Constants.MENU_RESET_MODEL_POSITION);
        resetModel.setOnAction(actionEvent -> {
            if (viewerController.getModelController() != null)
            {
                viewerController.getModelController().resetModelPosition();
            }
        });

        MenuItem resetView = new MenuItem(Constants.MENU_RESET_COORDINATE_SYSTEM);
        resetView.setOnAction(actionEvent -> {
            if (viewerController.getCameraController() != null)
            {
                viewerController.getCameraController().resetView();
            }

            showAxis.setSelected(true);
            showAxis.setVisible(true);
            updateShowAxis();
        });

        view.getItems().addAll(resetModel, resetView);

        return view;
    }

    /**
     * Erstellt das Hilfe-Menü mit einer Info-Box.
     *
     * @return Ein {@link Menu} für Hilfe-Informationen.
     * @Nachbedingung Menü ist funktional verbunden.
     */
    public Menu createHelpMenu()
    {
        Menu helpMenu = new Menu(Constants.MENU_HELP);

        MenuItem about = createInfoMenuItem();
        MenuItem instructions = createInstructionMenuItem();

        helpMenu.getItems().addAll(about, instructions);

        return helpMenu;
    }

    private MenuItem createInstructionMenuItem()
    {
        MenuItem instructions = new MenuItem(Constants.PROGRAM_INSTRUCTIONS);

        instructions.setOnAction(actionEvent -> {
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle(Constants.PROGRAM_INSTRUCTIONS);
            dialog.setHeaderText(Constants.STL_VIEWER_INSTRUCTIONS);

            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

            TextArea content = new TextArea(Constants.PROGRAM_INSTRUCTIONS_TEXT);
            content.setEditable(false);
            content.setWrapText(true);
            content.setPrefWidth(450);
            content.setPrefHeight(230);

            dialog.getDialogPane().setContent(content);
            dialog.showAndWait();
        });

        return instructions;
    }

    public MenuItem createInfoMenuItem()
    {
        MenuItem about = new MenuItem(Constants.MENU_ABOUT_STL_VIEWER);
        about.setOnAction(actionEvent -> {
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle(Constants.MENU_ABOUT_STL_VIEWER);
            info.setHeaderText(null);
            info.setContentText(Constants.MENU_STL_VIEWER_DETAILS);
            info.showAndWait();
        });

        return about;
    }

    /**
     * Erstellt eine Standard-Details-Ansicht, wenn kein Polyeder geladen ist.
     *
     * @return Ein {@link VBox} mit Platzhalterinformationen.
     * @Vorbedingung Keine speziellen Vorbedingungen.
     * @Nachbedingung Der VBox-Container enthält Titel und Platzhalter.
     */
    private VBox createDefaultPolyhedronDetails()
    {
        VBox polyhedronDetails = new VBox();
        polyhedronDetails.setStyle(Constants.POLYHEDRON_DETAILS_VBOX_STYLE);

        Label title = new Label(Constants.SIDEBAR_POLYHEDRON_INFORMATION);
        title.setStyle(Constants.SIDEBAR_POLYHEDRON_INFORMATION_STYLE);

        Label placeholder = new Label(Constants.SIDEBAR_NO_POLYHEDRON_LOADED);

        polyhedronDetails.getChildren().addAll(title, placeholder);

        return polyhedronDetails;
    }

    /**
     * Aktualisiert die Details-Ansicht mit Informationen des geladenen Polyeders.
     *
     * @param polyhedron Das geladene {@link Polyhedron}-Objekt.
     * @Vorbedingung polyhedron darf nicht null sein.
     * @Nachbedingung Die polyhedronDetails VBox enthält aktuelle Informationen.
     */
    public void updatePolyhedronDetails(Polyhedron polyhedron)
    {
        Label title = new Label(Constants.SIDEBAR_POLYHEDRON_INFORMATION);
        title.setStyle(Constants.SIDEBAR_POLYHEDRON_INFORMATION_STYLE);

        polyhedronDetails.getChildren().clear();
        polyhedronDetails.getChildren().addAll(
                title,
                createTextField(
                        Constants.SIDEBAR_NAME_POLYHEDRON, polyhedron.getName()),
                createTextField(
                        Constants.SIDEBAR_NUMBER_OF_FACES, String.valueOf(polyhedron.getFaces().size())),
                createTextField(
                        Constants.SIDEBAR_SURFACE_AREA, polyhedron.getSurfaceArea() + Constants.UNITS_CM_2),
                createTextField(
                        Constants.SIDEBAR_VOLUME, polyhedron.getVolume() + Constants.UNITS_CM_3));
    }

    /**
     * Aktualisiert die Sichtbarkeit des Koordinatensystems basierend auf der Checkbox.
     *
     * @Vorbedingung uiController ist initialisiert.
     * @Nachbedingung axesGroup ist sichtbar oder unsichtbar gemäß Auswahl.
     */
    public void updateShowAxis()
    {
        if (showAxis != null)
        {
            axesGroup.setVisible(showAxis.isSelected());
        }
    }

    /**
     * Erstellt ein nicht-editierbares Textfeld mit Label.
     *
     * @param labelText     Der Text für das Label.
     * @param textFieldText Der anzuzeigende Wert im TextField.
     * @return Ein {@link Node} bestehend aus Label und TextField.
     * @Vorbedingung labelText und textFieldText dürfen nicht null sein.
     * @Nachbedingung Das Node ist einsatzbereit.
     */
    private Node createTextField(String labelText, String textFieldText)
    {
        Text label = new Text(labelText);
        TextField textField = new TextField(textFieldText);
        textField.setEditable(false);

        VBox.setMargin(textField, new Insets(0, 0, Constants.TEXTFIELD_MARGIN, 0));

        return new VBox(label, textField);
    }

    /**
     * Erzeugt das Koordinatensystem mit Achsen und Grid.
     *
     * @return Eine {@link Group} mit X-, Y-, Z-Achsen und Grid.
     * @Vorbedingung Keine speziellen Vorbedingungen.
     * @Nachbedingung Das Koordinatensystem und Grid sind erstellt.
     */
    public Group createCoordinateAxes()
    {
        double axisLength = Constants.COORDINATE_AXIS_LENGTH;
        Cylinder xAxis = new Cylinder(Constants.COORDINATE_AXIS_DIAMETER, axisLength);
        xAxis.setMaterial(new PhongMaterial(Color.RED));
        xAxis.setRotationAxis(Rotate.Z_AXIS);
        xAxis.setRotate(Constants.COORDINATE_AXIS_Z_ROTATE);

        Cylinder yAxis = new Cylinder(Constants.COORDINATE_AXIS_DIAMETER, axisLength);
        yAxis.setMaterial(new PhongMaterial(Color.GREEN));

        Cylinder zAxis = new Cylinder(Constants.COORDINATE_AXIS_DIAMETER, axisLength);
        zAxis.setMaterial(new PhongMaterial(Color.BLUE));
        zAxis.setRotationAxis(Rotate.X_AXIS);
        zAxis.setRotate(Constants.COORDINATE_AXIS_Z_ROTATE);

        Group grid = createGrid(Constants.GRID_SIZE, Constants.GRID_STEP);

        axesGroup = new Group(xAxis, yAxis, zAxis, grid);

        return axesGroup;
    }

    /**
     * Erstellt ein Grid von Linien in der XY-Ebene.
     *
     * @param size Die halbe Größe des Gitters.
     * @param step Der Abstand zwischen den einzelnen Linien.
     * @return Eine {@link Group} mit Gitterlinien.
     * @Vorbedingung size >= 0 und step > 0.
     * @Nachbedingung Das Gitter ist erstellt.
     */
    private static Group createGrid(int size, int step)
    {
        Group gridGroup = new Group();
        Color gridColor = Color.GRAY;
        double thickness = Constants.GRID_THICKNESS;

        for (int i = -size; i <= size; i += step)
        {
            Box vLine = new Box(thickness, size * Constants.NUMBERS_TWO, thickness);
            vLine.setMaterial(new PhongMaterial(gridColor));
            vLine.setTranslateX(i);
            vLine.setTranslateY(0);
            vLine.setTranslateZ(0);

            Box hLine = new Box(size * Constants.NUMBERS_TWO, thickness, thickness);
            hLine.setMaterial(new PhongMaterial(gridColor));
            hLine.setTranslateX(0);
            hLine.setTranslateY(i);
            hLine.setTranslateZ(0);

            gridGroup.getChildren().addAll(vLine, hLine);
        }

        return gridGroup;
    }

    public CheckBox getShowWireframe()
    {
        return showWireframe;
    }

    public CheckBox getShowAxis()
    {
        return showAxis;
    }
}
