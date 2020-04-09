package fx.listComponents;

import Hierarchy.HierarchyObject;
import fx.EditWindow;
import fx.EditWindowConstructorParam;
import fx.EditWindowListener;
import fx.FXMLFileLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ObjectComponent extends Component implements MainListComponent {

    private HierarchyObject value;

    @FXML
    private Label lblName;

    @FXML
    private Button btnEdit;

    @FXML
    @Override
    public void onBtnEditClicked(ActionEvent event) {
        final HierarchyObject[] hierarchyObjectToEdit = {value};
        Parent editWindowRoot = FXMLFileLoader.loadFXML("editWindow",
                EditWindow.class,
                new EditWindowConstructorParam(hierarchyObjectToEdit[0], new EditWindowListener() {
                    @Override
                    public void saveHierarchyObject(HierarchyObject hierarchyObject) {
                        hierarchyObjectToEdit[0] = hierarchyObject;
                    }
                }));
        Stage editWindowStage = new Stage();
        editWindowStage.setTitle("");
        editWindowStage.initModality(Modality.WINDOW_MODAL);
        editWindowStage.initOwner(
                ((Node)event.getSource()).getScene().getWindow());
        editWindowStage.setScene(new Scene(editWindowRoot));
        editWindowStage.setResizable(false);
        editWindowStage.showAndWait();
    }

    public ObjectComponent(HierarchyObject value) {
        this.value = value;
    }
}

