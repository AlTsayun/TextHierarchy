package fx.listComponents;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class IntegerComponent implements Component {

    private int value;
    private String lblText;

    @FXML
    private Label label;

    @FXML
    private TextField tfValue;

    public IntegerComponent(ComponentConstructorParam param) {
        this.lblText = param.lblText;
        this.value = (Integer) param.value;
    }

    @Override
    public Object getValue() throws IOException {
        try {
            return Integer.parseInt(tfValue.getText());
        } catch (NumberFormatException e){
            throw new IOException();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        label.setText(lblText);
        tfValue.setText(Integer.toString(value));// force the field to be numeric only
        tfValue.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                try {
                    Integer.parseInt(newValue);
                    tfValue.setStyle("");
                } catch (NumberFormatException e){
                    tfValue.setStyle("-fx-focus-color: red; -fx-text-box-border: red");
                }
            }
        });
    }
}
