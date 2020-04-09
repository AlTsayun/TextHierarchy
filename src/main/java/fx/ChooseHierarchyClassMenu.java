package fx;

import Hierarchy.HierarchyHandler;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChooseHierarchyClassMenu implements Initializable {

    private ChooseHierarchyClassMenuListener listener;

    @FXML
    private ListView<String> lwClasses;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnCreate;

    @FXML
    void onBtnCancelClick(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onBtnCreateClick(ActionEvent event) {
            String selectedClassLabel = lwClasses.getSelectionModel().getSelectedItem();
            listener.sendClassName(selectedClassLabel);
            onBtnCancelClick(event);
    }

    public ChooseHierarchyClassMenu(ChooseHierarchyClassMenuConstructorParam param) {
        this.listener = param.listener;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HierarchyHandler hierarchyHandler = new HierarchyHandler();
        lwClasses.setItems(FXCollections.observableArrayList(new ArrayList<String>(hierarchyHandler.getAllClassesNames())));
    }
}
