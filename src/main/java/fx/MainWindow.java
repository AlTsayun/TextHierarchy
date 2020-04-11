package fx;

import Annotations.HierarchyAnnotation;
import Hierarchy.HierarchyHandler;
import Hierarchy.HierarchyObject;
import fx.listComponents.MainMenuComponent;
import fx.listComponents.ObjectComponent;
import fx.listComponents.ComponentConstructorParam;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

@Slf4j
public class MainWindow implements Initializable {

    @FXML
    private ListView<MainMenuComponent> mainList;
    @FXML
    private Button btnNew;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnEdit;

    @FXML
    void onBtnNewClicked(ActionEvent event){
        try {
            final String[] classNameToCreate = new String[]{null};

            FXMLFileLoaderResponse<Object, Object> loaderResponse = FXMLFileLoader.loadFXML("chooseHierarchyClassMenu",
                    ChooseHierarchyClassMenu.class,
                    new ChooseHierarchyClassMenuConstructorParam(new ChooseHierarchyClassMenuListener() {
                        @Override
                        public void sendClassName(String className) {
                            classNameToCreate[0] = className;
                        }
                    }));
            Parent chooseHierarchyClassMenuRoot = (Parent) loaderResponse.loadedObject;

            Stage chooseHierarchyClassMenuStage = new Stage();
            chooseHierarchyClassMenuStage.setTitle("Выберите класс");
            chooseHierarchyClassMenuStage.initModality(Modality.WINDOW_MODAL);
            chooseHierarchyClassMenuStage.initOwner(
                    ((Node) event.getSource()).getScene().getWindow());
            chooseHierarchyClassMenuStage.setScene(new Scene(chooseHierarchyClassMenuRoot));
            chooseHierarchyClassMenuStage.setResizable(false);
            chooseHierarchyClassMenuStage.showAndWait();

            if (classNameToCreate[0] == null) {
                //todo: come up with an exception
                throw new IOException();
            }


            HierarchyHandler hierarchyHandler = new HierarchyHandler();
            HierarchyObject hierarchyObjectToEdit = hierarchyHandler.getDefaultHierarchyObject(classNameToCreate[0]);

            loaderResponse = FXMLFileLoader.loadFXML("objectComponent",
                    ObjectComponent.class,
                    new ComponentConstructorParam(null, hierarchyObjectToEdit));


            mainList.getItems().add((MainMenuComponent) loaderResponse.controller);
//           mainList.getChildren().add((Parent) loaderResponse.loadedObject);


        } catch (IOException e){
            log.info("classNameToCreate not chosen");
        } catch (ClassNotFoundException e){
            log.info("User have chosen unexisting class to create");
        }
    }

    @FXML
    void onBtnDeleteClicked(ActionEvent event) {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete this object?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            MainMenuComponent item = mainList.getSelectionModel().getSelectedItem();
            log.info("Removing: " + item.toString());
            mainList.getItems().remove(item);
            item.delete();
        }


    }

    @FXML
    void onBtnEditClicked(ActionEvent event) {
        MainMenuComponent item = mainList.getSelectionModel().getSelectedItem();
        log.info("Editing: " + item.toString());
        item.onBtnEditClicked(new ActionEvent());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainList.setCellFactory(lv ->{
            ListCell<MainMenuComponent> cell = new ListCell<MainMenuComponent>(){
                @Override
                @SneakyThrows
                public void updateItem(MainMenuComponent item, boolean empty){
                    super.updateItem(item, empty);
                    if(item == null){
                        setText(null);
                    } else {
                        setText(item.getValue().getClass().getAnnotation(HierarchyAnnotation.class).label());
                    }
                }
            };


            MenuItem editItem = new MenuItem();
            editItem.textProperty().bind(Bindings.format("Edit"));
            editItem.setOnAction(event -> {
                MainMenuComponent item = cell.getItem();
                item.onBtnEditClicked(new ActionEvent());
            });

            MenuItem deleteItem = new MenuItem();
            deleteItem.textProperty().bind(Bindings.format("Delete"));
            deleteItem.setOnAction(event -> onBtnDeleteClicked(new ActionEvent()));
            ContextMenu componentContextMenu = new ContextMenu();

            componentContextMenu.getItems().setAll(editItem, deleteItem);

            MenuItem createItem = new MenuItem("Create new");
            createItem.setOnAction(event -> onBtnNewClicked(new ActionEvent()));
            ContextMenu notSelectedItemMenu = new ContextMenu();
            notSelectedItemMenu.getItems().setAll(createItem);


            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    lv.setContextMenu(notSelectedItemMenu);
                } else {
                    cell.setContextMenu(componentContextMenu);
                }
            });

            cell.setPrefHeight(40);

            return cell;

        });
    }
}
