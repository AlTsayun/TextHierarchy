package fx;

import Hierarchy.HierarchyObject;
import fx.listComponents.Component;
import fx.listComponents.ComponentsHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class EditWindow implements Initializable {

    private HierarchyObject hierarchyObjectToEdit;

    private EditWindowListener listener;

    private List<? extends Component> controllers;

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
        listener.saveHierarchyObject(hierarchyObjectToEdit);
        onBtnCancelClick(event);
    }


    public EditWindow(EditWindowConstructorParam param) {
        hierarchyObjectToEdit = param.hierarchyObject;
        listener = param.listener;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Map<Class<? extends Component>, Parent> controllersAndPanes = ComponentsHandler.getControllersAndPanesForHierarchyObject(hierarchyObjectToEdit);

        controllers = new ArrayList<Component>();
    }
}
