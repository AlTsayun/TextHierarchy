package fx.listComponents;

import javafx.fxml.Initializable;

import java.io.IOException;

public interface Component extends Initializable {
    Object getValue() throws IOException;
}
