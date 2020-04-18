package fx;

import annotations.HierarchyAnnotation;
import hierarchy.HierarchyHandler;
import hierarchy.HierarchyObject;
import fx.listComponents.MainMenuComponent;
import fx.listComponents.ObjectComponent;
import fx.listComponents.ComponentConstructorParam;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import serializers.SerializersHandler;
import serializers.SerializersTypes;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Slf4j
public class MainWindow implements Initializable {

    @FXML
    private ListView<MainMenuComponent> lvMain;
    @FXML
    private Button btnNew;

    @FXML
    private Button btnLoad;

    @FXML
    private Button btnSave;

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
            showModalWindow((Parent) loaderResponse.loadedObject, "Выберите класс");

            if (classNameToCreate[0] == null) {
                //todo: come up with an exception
                throw new IOException();
            }


            HierarchyHandler hierarchyHandler = new HierarchyHandler();
            HierarchyObject hierarchyObjectToEdit = hierarchyHandler.getDefaultHierarchyObject(classNameToCreate[0]);

            loaderResponse = FXMLFileLoader.loadFXML("objectComponent",
                    ObjectComponent.class,
                    new ComponentConstructorParam(null, hierarchyObjectToEdit));


            lvMain.getItems().add((MainMenuComponent) loaderResponse.controller);
//           mainList.getChildren().add((Parent) loaderResponse.loadedObject);


        } catch (IOException e){
            log.info("classNameToCreate not chosen");
        } catch (ClassNotFoundException e){
            log.info("User have chosen unexisting class to create");
        }
    }

    private void showModalWindow(Parent loadedObject, String title) {
        Parent root = (Parent) loadedObject;

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.showAndWait();
    }

    @FXML
    void onBtnDeleteClicked(ActionEvent event) {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete this object?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            MainMenuComponent item = lvMain.getSelectionModel().getSelectedItem();
            log.info("Removing: " + item.toString());
            lvMain.getItems().remove(item);
            item.delete();
        }


    }

    @FXML
    void onBtnEditClicked(ActionEvent event) {
        MainMenuComponent item = lvMain.getSelectionModel().getSelectedItem();
        log.info("Editing: " + item.toString());
        item.onBtnEditClicked(new ActionEvent());
    }


    @FXML
    void onBtnSaveClicked(ActionEvent event) {
        FXMLFileLoaderResponse<Object, Object> loaderResponse = FXMLFileLoader.loadFXML("fileDialog",
                FileDialog.class,
                new FileDialogConstructorParam(new FileDialogListener() {
                    @Override
                    @SneakyThrows
                    public void sendFileInfo(String path, SerializersTypes serializersType) {
                        try {
                            SerializersHandler serializersHandler = new SerializersHandler(serializersType);
                            HierarchyObject[] objects = lvMain.getItems().stream().map((component ->{
                                return (HierarchyObject) component;
                            })).toArray(HierarchyObject[]::new);
                            serializersHandler.write(objects, path);

                        } catch (IOException e) {Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Cannot save file!");
                            alert.showAndWait();
                        }
                    }
                }));
        showModalWindow((Parent) loaderResponse.loadedObject, "Configure file saving");
    }

    @FXML
    void onBtnLoadClicked(ActionEvent event) {
        FXMLFileLoaderResponse<Object, Object> loaderResponse = FXMLFileLoader.loadFXML("fileDialog",
                FileDialog.class,
                new FileDialogConstructorParam(new FileDialogListener() {
                    @SneakyThrows
                    @Override
                    public void sendFileInfo(String path, SerializersTypes serializersType) {
                        try {
                            SerializersHandler serializersHandler = new SerializersHandler(serializersType);

                            List<HierarchyObject> hierarchyObjects = serializersHandler.read(path);

                            lvMain.setItems(FXCollections.observableArrayList(hierarchyObjects.stream().map((hierarchyObject) ->{
                                FXMLFileLoaderResponse<Object, Object> loaderResponse = FXMLFileLoader.loadFXML("objectComponent",
                                        ObjectComponent.class,
                                        new ComponentConstructorParam(null, hierarchyObject));
                                return (MainMenuComponent) loaderResponse.controller;
                            }).collect(Collectors.toList())));

                        } catch (IOException e) {Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Cannot load file!");
                            alert.showAndWait();
                        }
                    }
                }));
        showModalWindow((Parent) loaderResponse.loadedObject, "Configure file loading");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lvMain.setCellFactory(lv ->{
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
