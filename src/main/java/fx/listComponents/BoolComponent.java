package fx.listComponents;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BoolComponent implements Component{

    private boolean value;
    private String lblText;

    @Override
    public Object getValue() throws IOException {
        return cbValue.isSelected();
    }
    @FXML
    private Label label;

    @FXML
    private CheckBox cbValue;

    public BoolComponent(ComponentConstructorParam param) {
        this.lblText = param.lblText;
        this.value = (boolean) param.value;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbValue.setSelected(value);
        label.setText(lblText);
    }
}
