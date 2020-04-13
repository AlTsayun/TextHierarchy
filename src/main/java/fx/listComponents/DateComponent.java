package fx.listComponents;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DateComponent implements Component{

    private LocalDate value;
    private String lblText;
    @FXML
    private Label label;

    @FXML
    private DatePicker dpValue;

    @Override
    public Object getValue() throws IOException {
        return dpValue.getValue();
    }

    public DateComponent(ComponentConstructorParam param) {
        this.lblText = param.lblText;
        this.value = (LocalDate) param.value;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        label.setText(lblText);
        dpValue.setValue(value);
    }
}
