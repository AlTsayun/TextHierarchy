package fx;

import Hierarchy.HierarchyHandler;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.util.ArrayList;

public class ChooseHierarchyClassMenu {

    private ChooseHierarchyClassMenuListener listener;

    @FXML
    private ListView<String> lwClasses;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnCreate;

    @FXML
    void onBtnCancelClick(ActionEvent event) {

    }

    @FXML
    void onBtnCreateClick(ActionEvent event) {

            String selectedClassLabel = lwClasses.getSelectionModel().getSelectedItem();
            listener.sendClassName(selectedClassLabel);
    }

    public ChooseHierarchyClassMenu(ChooseHierarchyClassMenuConstructorParam param) {
        this.listener = param.listener;
    }

    public void init(){
        HierarchyHandler hierarchyHandler = new HierarchyHandler();
        lwClasses.setItems(FXCollections.observableArrayList(new ArrayList<String>(hierarchyHandler.getAllClassesNames())));
    }
}
