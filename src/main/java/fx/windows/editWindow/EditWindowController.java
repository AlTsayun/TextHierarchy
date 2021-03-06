package fx.windows.editWindow;

import annotations.HierarchyAnnotation;
import hierarchy.HierarchyObject;
import fx.listComponents.Component;
import fx.listComponents.ComponentsHandler;
import fx.listComponents.LoadedHandlerResponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;

public class EditWindowController implements Initializable {

    private HierarchyObject hierarchyObjectToEdit;

    private EditWindowListener listener;

    Map<Field, Component> fieldComponentMap;

    @FXML
    private Label lbTitle;

    @FXML
    private VBox editList;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSave;

    @FXML
    void onBtnCancelClick(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onBtnSaveClick(ActionEvent event) {

        try {
            Set<Field> fields = fieldComponentMap.keySet();
            for (Field f : fields) {
                f.set(hierarchyObjectToEdit, fieldComponentMap.get(f).getValue());
            }

            listener.saveHierarchyObject(hierarchyObjectToEdit);
            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.close();
        }catch (Exception e){
            btnSave.setStyle("-fx-focus-color: red");
        }
    }


    public EditWindowController(EditWindowConstructorParam param) {
        hierarchyObjectToEdit = param.hierarchyObject;
        listener = param.listener;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ComponentsHandler componentsHandler = new ComponentsHandler();
        LoadedHandlerResponse response = componentsHandler.loadComponents(hierarchyObjectToEdit);
        fieldComponentMap = response.fieldComponentMap;
        editList.getChildren().setAll(response.panes);
        lbTitle.setText(hierarchyObjectToEdit.getClass().getAnnotation(HierarchyAnnotation.class).label());
    }
}
