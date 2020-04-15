package fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class FileDialog implements Initializable {
    private FileDialogListener listener;

    @FXML
    private TextField tfIn;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnOk;

    @FXML
    void onBtnCancelClicked(ActionEvent event) {

    }

    @FXML
    void onBtnOkClicked(ActionEvent event) {
        listener.sendFilePath(tfIn.getText());
    }

    public FileDialog(FileDialogConstructorParam param) {
        this.listener = param.listener;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tfIn.set
    }
}
