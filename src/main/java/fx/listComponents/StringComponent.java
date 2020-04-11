package fx.listComponents;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StringComponent implements Component {

    private final String value;
    private String lblText;
    @FXML
    private Label label;

    @FXML
    private TextField tfValue;

    @Override
    public Object getValue() throws IOException {
        return tfValue.getText();
    }

    public StringComponent(ComponentConstructorParam param) {
        this.lblText = param.lblText;
        this.value = (String) param.value;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        label.setText(lblText);
        tfValue.setText(value);
    }
}
