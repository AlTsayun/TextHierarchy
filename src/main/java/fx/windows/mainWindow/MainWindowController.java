package fx.windows.mainWindow;

import annotations.HierarchyAnnotation;
import fx.*;
import fx.windows.chooseHierarchyClassMenu.*;
import fx.windows.loadFileDialog.*;
import fx.windows.saveFileDialog.*;
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
import plugins.Plugin;
import plugins.PluginsLoader;
import serializers.SerializersHandler;
import serializers.SerializersTypes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class MainWindowController implements Initializable {

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
                    ChooseHierarchyClassMenuController.class,
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

    @FXML
    void onBtnDeleteClicked(ActionEvent event) {
        MainMenuComponent item;
        if ((item = lvMain.getSelectionModel().getSelectedItem()) == null){
            Alert saveFileInfo = new Alert(Alert.AlertType.INFORMATION);
            saveFileInfo.setTitle("No item selected");
            saveFileInfo.setHeaderText("No item selected");
            saveFileInfo.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete this object?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                log.info("Removing: " + item.toString());
                lvMain.getItems().remove(item);
                item.delete();
            }
        }

    }

    @FXML
    void onBtnEditClicked(ActionEvent event) {
        MainMenuComponent item;
        if ((item = lvMain.getSelectionModel().getSelectedItem()) == null){
            Alert saveFileInfo = new Alert(Alert.AlertType.INFORMATION);
            saveFileInfo.setTitle("No item selected");
            saveFileInfo.setHeaderText("No item selected");
            saveFileInfo.showAndWait();
        } else {
            log.info("Editing: " + item.toString());
            item.onBtnEditClicked(new ActionEvent());
        }
    }

    @FXML
    void onBtnSaveClicked(ActionEvent event) {
        FXMLFileLoaderResponse<Object, Object> loaderResponse = FXMLFileLoader.loadFXML("saveFileDialog",
                SaveFileDialogController.class,
                new SaveFileDialogConstructorParam(new SaveFileDialogListener() {
                    @Override
                    @SneakyThrows
                    public void sendFileInfo(String path, SerializersTypes serializersType, String pluginName) {
                        try {
                            SerializersHandler serializersHandler = new SerializersHandler(serializersType);
                            HierarchyObject[] objects = lvMain.getItems().stream().map((new Function<MainMenuComponent, Object>() {
                                @Override
                                @SneakyThrows
                                public Object apply(MainMenuComponent component){
                                    return (HierarchyObject) component.getValue();
                                }
                            })).toArray(HierarchyObject[]::new);

                            byte[] data = serializersHandler.write(objects);
                            String extension = "";

                            if(pluginName != null){
                                PluginsLoader pluginsLoader = new PluginsLoader();
                                Plugin plugin = (Plugin) pluginsLoader.loadPlugin(pluginName).getConstructor().newInstance();
                                data = plugin.convert(data);
                                extension = plugin.getFileExtension();
                            }
                            try (FileOutputStream stream = new FileOutputStream(path + extension)) {
                                stream.write(data);
                            }


                            Alert saveFileInfo = new Alert(Alert.AlertType.INFORMATION);
                            saveFileInfo.setTitle("Done!");
                            saveFileInfo.setHeaderText("File successfully saved");
                            saveFileInfo.showAndWait();
                        } catch (IOException e) {
                            Alert saveFileError = new Alert(Alert.AlertType.ERROR);
                            saveFileError.setTitle("Error");
                            saveFileError.setHeaderText("Cannot save file!");
                            saveFileError.showAndWait();
                            e.printStackTrace();
                        }
                    }
                }));
        showModalWindow((Parent) loaderResponse.loadedObject, "Configure file saving");
    }

    @FXML
    void onBtnLoadClicked(ActionEvent event) {
        FXMLFileLoaderResponse<Object, Object> loaderResponse = FXMLFileLoader.loadFXML("loadFileDialog",
                LoadFileDialogController.class,
                new LoadFileDialogConstructorParam(new LoadFileDialogListener() {
                    @Override
                    public void sendFileInfo(String path, SerializersTypes serializersType) {
                        try {
                            byte[] data = Files.readAllBytes(Path.of(path));

                            PluginsLoader pluginsLoader = new PluginsLoader();
                            String fileExtension = path.substring(path.lastIndexOf("."));
                            try {
                                Plugin plugin = pluginsLoader.getPluginForFileExtension(fileExtension);
                                data = plugin.revert(data);
                            } catch (IllegalArgumentException e) {
                                log.info("No plugin applied on loading file \"" + path + "\"");
                            }


                            List<HierarchyObject> hierarchyObjects = new ArrayList<>();
                            try {
                                SerializersHandler serializersHandler = new SerializersHandler(serializersType);
                                hierarchyObjects = serializersHandler.read(data);
                            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                                throw new IOException(e);
                            }


                            lvMain.setItems(FXCollections.observableArrayList(hierarchyObjects.stream().map((hierarchyObject) ->{
                                FXMLFileLoaderResponse<Object, Object> loaderResponse = FXMLFileLoader.loadFXML("objectComponent",
                                        ObjectComponent.class,
                                        new ComponentConstructorParam(null, hierarchyObject));
                                return (MainMenuComponent) loaderResponse.controller;
                            }).collect(Collectors.toList())));

                            Alert loadedFileInfo = new Alert(Alert.AlertType.INFORMATION);
                            loadedFileInfo.setTitle("Done!");
                            loadedFileInfo.setHeaderText("File successfully loaded");
                            loadedFileInfo.showAndWait();


                        } catch (IOException e) {
                            Alert loadFileError = new Alert(Alert.AlertType.ERROR);
                            loadFileError.setTitle("Error");
                            loadFileError.setHeaderText("Cannot load file!");
                            loadFileError.showAndWait();
                            e.printStackTrace();
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

    private void showModalWindow(Parent loadedObject, String title) {
        Parent root = (Parent) loadedObject;

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.showAndWait();
    }

}
