package fx.windows.loadFileDialog;

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

public class LoadFileDialogController implements Initializable {
    private LoadFileDialogListener listener;

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


            listener.sendFileInfo(tfIn.getText(), serializersType);
            Stage stage = (Stage) btnOk.getScene().getWindow();
            stage.close();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public LoadFileDialogController(LoadFileDialogConstructorParam param) {
        this.listener = param.listener;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnOk.setDisable(true);
        tfIn.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                    if (new File(newValue).exists()){
                        tfIn.setStyle("");
                        btnOk.setDisable(false);
                    }else {
                        tfIn.setStyle("-fx-focus-color: red; -fx-text-box-border: red");
                        btnOk.setDisable(true);
                }
            }
        });
    }
}
