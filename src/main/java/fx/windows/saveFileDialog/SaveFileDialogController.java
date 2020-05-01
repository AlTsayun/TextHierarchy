package fx.windows.saveFileDialog;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import plugins.PluginsLoader;
import serializers.SerializersTypes;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class SaveFileDialogController implements Initializable {
    private SaveFileDialogListener listener;
    private final String defaultPluginName = "None";

    @FXML
    private ComboBox<String> cmbPlugins;

    @FXML
    private ToggleGroup tgFileType;

    @FXML
    private RadioButton rbBinary;

    @FXML
    private RadioButton rbJson;

    @FXML
    private RadioButton rbText;

    @FXML
    private TextField tfIn;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnOk;

    @FXML
    void onBtnCancelClicked(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onBtnOkClicked(ActionEvent event) {
        try {
            SerializersTypes serializersType;

            if(tgFileType.getSelectedToggle() == rbText) {serializersType = SerializersTypes.text;}
       else if(tgFileType.getSelectedToggle() == rbBinary) {serializersType = SerializersTypes.binary;}
       else if(tgFileType.getSelectedToggle() == rbJson) {serializersType = SerializersTypes.json;}
       else throw new IllegalStateException("No rb selected!");

            File file = new File(tfIn.getText());
            if(!file.exists()){
                Alert createFileConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                createFileConfirmation.setTitle("File doesn't exist");
                createFileConfirmation.setHeaderText("Create a file?");
                createFileConfirmation.showAndWait().ifPresent((response) ->{
                    if (response == ButtonType.OK){
                        try {
                            file.createNewFile();
                        } catch (IOException e) {

                            Alert createFileError = new Alert(Alert.AlertType.ERROR);
                            createFileError.setTitle("File doesn't exist");
                            createFileError.setHeaderText("Cannot create a file");
                            createFileError.showAndWait();
                        }
                    }
                });
            }

            String selectedPluginName = cmbPlugins.getSelectionModel().getSelectedItem();
            if(selectedPluginName.equals(defaultPluginName)){
                selectedPluginName = null;
            }

            listener.sendFileInfo(tfIn.getText(), serializersType, selectedPluginName);
            Stage stage = (Stage) btnOk.getScene().getWindow();
            stage.close();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public SaveFileDialogController(SaveFileDialogConstructorParam param) {
        this.listener = param.listener;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Show red when file doesn't exist
        tfIn.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue,
                                String newValue) {
                    if (new File(newValue).exists()){
                        tfIn.setStyle("");
                    }else {
                        tfIn.setStyle("-fx-focus-color: red; -fx-text-box-border: red");
                }
            }
        });

        //Configure combobox with plugins
        List<String> pluginsNames;
        try {
            PluginsLoader pluginsLoader = new PluginsLoader();
             pluginsNames = pluginsLoader.getPluginsNames();
        }catch (IOException | MissingResourceException e){
            Alert createFileError = new Alert(Alert.AlertType.ERROR);
            createFileError.setTitle("Problem while loading plugins");
            createFileError.setHeaderText("Cannot load plugins folder properly");
            createFileError.showAndWait();
            pluginsNames = new ArrayList<>();
        }

        pluginsNames.add(defaultPluginName);
        cmbPlugins.setItems(FXCollections.observableList(pluginsNames));
        cmbPlugins.getSelectionModel().select(defaultPluginName);

    }
}
