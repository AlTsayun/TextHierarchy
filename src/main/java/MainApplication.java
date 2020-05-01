import fx.windows.mainWindow.MainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;


//--module-path "D:\Programs\Java\javafx-sdk-11.0.2\lib" --add-modules=javafx.controls,javafx.fxml
public class MainApplication extends Application{
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception{
        URL resource = MainWindowController.class.getResource("mainWindow.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        stage.setTitle("TextHierarchy");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
