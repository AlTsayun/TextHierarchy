package fx;

import Hierarchy.HierarchyHandler;
import Hierarchy.HierarchyObject;
import fx.listComponents.Component;
import fx.listComponents.ComponentsHandler;
import fx.listComponents.ObjectComponent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.SneakyThrows;

import java.util.Map;

public class MainWindow {


    @FXML
    private VBox mainList;

    @FXML
    private Button btnNew;

    @FXML
    @SneakyThrows
    void onBtnNewClicked(ActionEvent event){
        final String[] classNameToCreate = new String[] {null};

        Parent chooseHierarchyClassMenuRoot = FXMLFileLoader.loadFXML("chooseHierarchyClassMenu",
                ChooseHierarchyClassMenu.class,
                new ChooseHierarchyClassMenuConstructorParam(className -> classNameToCreate[0] = className));

        Stage chooseHierarchyClassMenuStage = new Stage();
        chooseHierarchyClassMenuStage.setTitle("Выберите класс");
        chooseHierarchyClassMenuStage.initModality(Modality.WINDOW_MODAL);
        chooseHierarchyClassMenuStage.initOwner(
                ((Node)event.getSource()).getScene().getWindow());
        chooseHierarchyClassMenuStage.setScene(new Scene(chooseHierarchyClassMenuRoot));
        chooseHierarchyClassMenuStage.setResizable(false);
        chooseHierarchyClassMenuStage.showAndWait();

        if (classNameToCreate[0] == null){
            //trow
        }



        HierarchyHandler hierarchyHandler = new HierarchyHandler();
        HierarchyObject hierarchyObjectToEdit = hierarchyHandler.getDefaultHierarchyObject(classNameToCreate[0]);

        Parent objectComponentPane = FXMLFileLoader.loadFXML("objectComponent",
                ObjectComponent.class,
                hierarchyObjectToEdit);
        mainList.getChildren().add(objectComponentPane);

    }


}
