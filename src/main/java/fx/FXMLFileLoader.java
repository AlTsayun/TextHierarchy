package fx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Callback;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class FXMLFileLoader {


    @SneakyThrows
    static public <T> Parent loadFXML(String filename, Class controllerClass, T param){
        FXMLLoader loader = new FXMLLoader(controllerClass.getResource(filename + ".fxml"));

        Map<Class, Callable<?>> creators = new HashMap<>();
        creators.put(controllerClass, () -> {
            Class paramClass = param.getClass();
            return controllerClass.getConstructor(paramClass).newInstance(param);
        });
        loader.setControllerFactory(new Callback<Class<?>, Object>() {
            @SneakyThrows
            @Override
            public Object call(Class<?> controllerClass) {
                Callable<?> constructorToCall = creators.get(controllerClass);
                if (constructorToCall == null) {
                    //cannot find corresponding constructor with param
                    return controllerClass.getConstructor().newInstance();
                } else {
                    //constructor with param
                    return constructorToCall.call();
                }
            }
        });
        return loader.load();
    }
}
