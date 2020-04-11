package fx.listComponents;

import Annotations.HierarchyAnnotation;
import Hierarchy.HierarchyObject;
import fx.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ObjectComponent implements Component, MainMenuComponent{

    private HierarchyObject value;

    @FXML
    private Label lblName;

    @FXML
    private Button btnEdit;

    @FXML
    public void onBtnEditClicked(ActionEvent event) {
        final HierarchyObject[] hierarchyObjectToEdit = {value};

        FXMLFileLoaderResponse<Object, Object> loaderResponse = FXMLFileLoader.loadFXML("editWindow",
                EditWindow.class,
                new EditWindowConstructorParam(hierarchyObjectToEdit[0], new EditWindowListener() {
                    @Override
                    public void saveHierarchyObject(HierarchyObject hierarchyObject) {
                        hierarchyObjectToEdit[0] = hierarchyObject;
                    }
                }));
        Parent editWindowRoot = (Parent) loaderResponse.loadedObject;
        Stage editWindowStage = new Stage();
        editWindowStage.setTitle("Редактирование объекта " + lblName.getText());
        editWindowStage.initModality(Modality.APPLICATION_MODAL);
        editWindowStage.setScene(new Scene(editWindowRoot));
        editWindowStage.setResizable(false);
        editWindowStage.showAndWait();
    }

    @Override
    public void delete() {

    }

    public ObjectComponent(ComponentConstructorParam param) {
        this.value = (HierarchyObject) param.value;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblName.setText(value.getClass().getAnnotation(HierarchyAnnotation.class).label());
    }

    @Override
    public Object getValue() throws IOException {
        return value;
    }
}

