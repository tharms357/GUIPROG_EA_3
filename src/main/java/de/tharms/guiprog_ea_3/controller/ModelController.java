package de.tharms.guiprog_ea_3.controller;

import de.tharms.guiprog_ea_3.model.Axis;
import de.tharms.guiprog_ea_3.model.Constants;
import de.tharms.guiprog_ea_3.model.Polyhedron;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * //TODO überarbeiten
 * Steuert das 3D-Modell in der Ansicht, inklusive Laden von Polyedern, Positionierung und Benutzer-Interaktion.
 */
public class ModelController
{
    private final Group modelGroup = new Group();
    private final Group rotationGroup = new Group();
    private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
    private final Rotate rotateZ = new Rotate(0, Rotate.Z_AXIS);
    private final Translate translate = new Translate();
    private double anchorX, anchorY;
    private double anchorAngleX, anchorAngleZ;

    private MeshView meshView;
    private Polyhedron polyhedron;

    /**
     * Konstruktor, der die Transformations-Hierarchie des Modells initialisiert.
     *
     * @Vorbedingung Keine.
     * @Nachbedingung Die Transformationsgruppen für Rotation und Translation sind gesetzt.
     */
    public ModelController()
    {
        rotationGroup.getTransforms().addAll(rotateX, rotateY, rotateZ);
        modelGroup.getChildren().add(rotationGroup);
        modelGroup.getTransforms().addAll(translate);
    }

    /**
     * Lädt ein Polyeder aus einer STL-Datei und zeigt bei Fehlern einen Dialog an.
     *
     * @param filepath Pfad zur STL-Datei.
     * @Vorbedingung Der Dateipfad verweist auf eine existierende STL-Datei.
     * @Nachbedingung Das interne Polyhedron-Objekt ist gesetzt oder eine Fehlermeldung wurde angezeigt.
     */
    public void createPolyhedronFromFilepath(String filepath)
    {
        try
        {
            this.polyhedron = PolyhedronController.createPolyhedronFromSTL(filepath);
        }
        catch (IllegalArgumentException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(Constants.FILE_READING_ERROR);
            alert.setHeaderText(Constants.INVALID_STL_FILE);
            alert.setContentText(String.format(Constants.INVALID_STL_FILE_MESSAGE, filepath, e.getMessage()));
            alert.showAndWait();
        }
    }

    /**
     * Setzt Rotation und Translations des Modells auf die Ursprungswerte zurück.
     *
     * @Vorbedingung Keine.
     * @Nachbedingung Rotation und Translation sind gleich 0.
     */
    public void resetModelPosition()
    {
        rotateX.setAngle(0);
        rotateY.setAngle(0);
        rotateZ.setAngle(0);
        translate.setX(0);
        translate.setY(0);
        translate.setZ(0);
    }

    /**
     * Setzt das MeshView des Modells und wendet Standardmaterial an, falls kein Material definiert ist.
     *
     * @param meshView Das anzuzeigende {@link MeshView}-Objekt.
     * @Vorbedingung meshView darf nicht null sein.
     * @Nachbedingung Das Modell wird mit dem neuen MeshView modelGroup hinzugefügt.
     */
    public void setMesh(MeshView meshView)
    {
        this.meshView = meshView;

        if (meshView.getMaterial() == null)
        {
            setMeshDefaultValues(meshView);
        }

        rotationGroup.getChildren().clear();
        rotationGroup.getChildren().add(meshView);
    }

    /**
     * Wendet das Standardmaterial aus {@link Constants} auf das MeshView an.
     *
     * @param meshView Das {@link MeshView}, dem das Material zugewiesen wird.
     * @Vorbedingung meshView darf nicht null sein.
     * @Nachbedingung meshView hat das Default-Material gesetzt.
     */
    private void setMeshDefaultValues(MeshView meshView)
    {
        meshView.setMaterial(Constants.DEFAULT_MESHVIEW_MATERIAL);
    }

    /**
     * Registriert Maussteuerung für Rotation und Translation des Modells in einer SubScene.
     *
     * @param scene Die {@link SubScene}, auf der die Events behandelt werden.
     * @Vorbedingung scene darf nicht null sein.
     * @Nachbedingung Maus-Events bewegen das Modell entsprechend.
     */
    public void addMouseControl(SubScene scene)
    {
        scene.setOnMousePressed(mouseEvent -> mouseIsPressed(mouseEvent));
        scene.setOnMouseDragged(mouseEvent -> mouseIsDragged(mouseEvent));
    }

    /**
     * Speichert aktuelle Mausposition und Winkel bei gedrückter Maustaste.
     *
     * @param mouseEvent Das {@link MouseEvent} mit den aktuellen Koordinaten.
     * @Vorbedingung mouseEvent darf nicht null sein.
     * @Nachbedingung anchorX/Y und anchorAngleX/Z sind gespeichert.
     */
    private void mouseIsPressed(MouseEvent mouseEvent)
    {
        anchorX = mouseEvent.getSceneX();
        anchorY = mouseEvent.getSceneY();
        anchorAngleX = rotateX.getAngle();
        anchorAngleZ = rotateZ.getAngle();
    }

    /**
     * Bewegt das Modell durch Mausverschiebung: Drehen mit linker, bewegen mit rechter Maustaste.
     *
     * @param mouseEvent Das {@link MouseEvent} mit aktueller Position und Taste.
     * @Vorbedingung mouseEvent darf nicht null sein.
     * @Nachbedingung Das Modell wird der Eingabe enstsprechend gedreht oder rotiert.
     */
    private void mouseIsDragged(MouseEvent mouseEvent)
    {
        double deltaX = mouseEvent.getSceneX() - anchorX;
        double deltaY = mouseEvent.getSceneY() - anchorY;

        if (mouseEvent.getButton() == MouseButton.PRIMARY && !mouseEvent.isShiftDown())
        {
            rotateZ.setAngle(anchorAngleZ + deltaX * Constants.Y_DEFAULT_ROTATION_FACTOR);
            rotateX.setAngle(anchorAngleX + deltaY * Constants.X_DEFAULT_ROTATION_FACTOR);
        }
        else if (mouseEvent.getButton() == MouseButton.SECONDARY)
        {
            translate.setX(translate.getX() + deltaX);
            translate.setY(translate.getY() - deltaY);
            anchorX = mouseEvent.getSceneX();
            anchorY = mouseEvent.getSceneY();
        }
    }

    public MeshView getMeshView()
    {
        return meshView;
    }

    public Polyhedron getPolyhedron()
    {
        return polyhedron;
    }

    public Group getModelGroup()
    {
        return modelGroup;
    }

    public Rotate getRotateX() {
        return rotateX;
    }

    public Rotate getRotateY() {
        return rotateY;
    }

    public Rotate getRotateZ() {
        return rotateZ;
    }

    public Translate getTranslate() {
        return translate;
    }

    public void rotateObject(Axis axis, double value)
    {
        switch (axis)
        {
            case X:
                rotateX.setAngle(rotateX.getAngle() + value);
                break;
            case Y:
                rotateY.setAngle(rotateY.getAngle() + value);
                break;
            case Z:
                rotateZ.setAngle(rotateZ.getAngle() + value);
                break;
            default:
                break;
        }
    }

    public void rotateObject(Axis axis, double value, double anchor)
    {
        switch (axis)
        {
            case X:
                rotateX.setAngle(anchor + value);
                break;
            case Y:
                rotateY.setAngle(anchor + value);
                break;
            case Z:
                rotateZ.setAngle(anchor + value);
                break;
            default:
                break;
        }
    }

    public void translateObject(Axis axis, double value)
    {
        switch (axis)
        {
            case X:
                translate.setX(translate.getX() + value);
                break;
            case Y:
                translate.setY(translate.getY() + value);
                break;
            case Z:
                translate.setZ(translate.getZ() + value);
                break;
            default:
                break;
        }
    }

    public void translateObject(Axis axis, double value, double anchor)
    {
        switch (axis)
        {
            case X:
                translate.setX(anchor + value);
                break;
            case Y:
                translate.setY(anchor + value);
                break;
            case Z:
                translate.setZ(anchor + value);
                break;
            default:
                break;
        }
    }
}