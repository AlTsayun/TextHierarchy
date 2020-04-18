package fx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import serializers.SerializersTypes;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FileDialog implements Initializable {
    private FileDialogListener listener;
    private final boolean isSaving;

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
            if(isSaving && !file.exists()){
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


            listener.sendFileInfo(tfIn.getText(), serializersType);
            Stage stage = (Stage) btnOk.getScene().getWindow();
            stage.close();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public FileDialog(FileDialogConstructorParam param) {
        this.listener = param.listener;
        this.isSaving = param.isSaving;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnOk.setDisable(!isSaving);
        tfIn.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                    if (new File(newValue).exists()){
                        tfIn.setStyle("");
                        btnOk.setDisable(false);
                    }else {
                        tfIn.setStyle("-fx-focus-color: red; -fx-text-box-border: red");
                        btnOk.setDisable(!isSaving);
                }
            }
        });
    }
}
