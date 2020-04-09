package fx;

import Hierarchy.HierarchyHandler;
import Hierarchy.HierarchyObject;
import fx.listComponents.ObjectComponent;
import fx.listComponents.ComponentConstructorParam;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class MainWindow {


    @FXML
    private VBox mainList;

    @FXML
    private Button btnNew;

    @FXML
    void onBtnNewClicked(ActionEvent event){
        try {
            final String[] classNameToCreate = new String[]{null};

            Parent chooseHierarchyClassMenuRoot = FXMLFileLoader.loadFXML("chooseHierarchyClassMenu",
                    ChooseHierarchyClassMenu.class,
                    new ChooseHierarchyClassMenuConstructorParam(new ChooseHierarchyClassMenuListener() {
                        @Override
                        public void sendClassName(String className) {
                            classNameToCreate[0] = className;
                        }
                    }));

            Stage chooseHierarchyClassMenuStage = new Stage();
            chooseHierarchyClassMenuStage.setTitle("Выберите класс");
            chooseHierarchyClassMenuStage.initModality(Modality.WINDOW_MODAL);
            chooseHierarchyClassMenuStage.initOwner(
                    ((Node) event.getSource()).getScene().getWindow());
            chooseHierarchyClassMenuStage.setScene(new Scene(chooseHierarchyClassMenuRoot));
            chooseHierarchyClassMenuStage.setResizable(false);
            chooseHierarchyClassMenuStage.showAndWait();

            if (classNameToCreate[0] == null) {
                throw new IOException();
            }


            HierarchyHandler hierarchyHandler = new HierarchyHandler();
            HierarchyObject hierarchyObjectToEdit = hierarchyHandler.getDefaultHierarchyObject(classNameToCreate[0]);

            Parent objectComponentPane = FXMLFileLoader.loadFXML("objectComponent",
                    ObjectComponent.class,
                    new ComponentConstructorParam(hierarchyObjectToEdit));
            mainList.getChildren().add(objectComponentPane);


        } catch (IOException e){
            log.info("classNameToCreate not chosen");
        } catch (ClassNotFoundException e){
            log.info("User have chosen unexisting class to create");
        }
    }


}
