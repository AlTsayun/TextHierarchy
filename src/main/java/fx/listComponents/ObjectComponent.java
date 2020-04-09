package fx.listComponents;

import Annotations.HierarchyAnnotation;
import Hierarchy.HierarchyObject;
import fx.EditWindow;
import fx.EditWindowConstructorParam;
import fx.EditWindowListener;
import fx.FXMLFileLoader;
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

import java.net.URL;
import java.util.ResourceBundle;

public class ObjectComponent extends Component implements Initializable {

    private HierarchyObject value;

    @FXML
    private Label lblName;

    @FXML
    private Button btnEdit;

    @FXML
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
                ((Node) event.getSource()).getScene().getWindow());
        editWindowStage.setScene(new Scene(editWindowRoot));
        editWindowStage.setResizable(false);
        editWindowStage.showAndWait();
    }

    public ObjectComponent(ComponentConstructorParam param) {
        this.value = (HierarchyObject) param.value;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblName.setText(value.getClass().getAnnotation(HierarchyAnnotation.class).label());
    }
}

