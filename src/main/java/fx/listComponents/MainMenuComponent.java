package fx.listComponents;

import hierarchy.HierarchyObject;
import javafx.event.ActionEvent;

import java.io.IOException;

public interface MainMenuComponent extends Component {

//    Object getValue()  throws IOException;
    void onBtnEditClicked(ActionEvent event);
    void delete();
}
