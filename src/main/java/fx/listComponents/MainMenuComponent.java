package fx.listComponents;

import javafx.event.ActionEvent;

public interface MainMenuComponent extends Component {

    void onBtnEditClicked(ActionEvent event);
    void delete();
}
