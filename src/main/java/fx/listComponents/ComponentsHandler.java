package fx.listComponents;

import Annotations.HierarchyAnnotation;
import Hierarchy.DataType;
import Hierarchy.HierarchyHandler;
import Hierarchy.HierarchyObject;
import fx.FXMLFileLoader;
import javafx.scene.Parent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ComponentsHandler {
//    Map of dataTypes' with corresponding controller classes .fxml files
    static private final Map<DataType, ComponentInfo> componentsInfoList = Map.ofEntries(
            Map.entry(DataType.object, new ComponentInfo(ObjectComponent.class, "objectComponent"))
    );

    @SneakyThrows
    public static Map<Class<? extends Component>, Parent> getControllersAndPanesForHierarchyObject(HierarchyObject hierarchyObject){
        HierarchyHandler hierarchyHandler = new HierarchyHandler();
        Field[] fields = hierarchyHandler.getFields(hierarchyObject);
        Map<Class<?  extends Component>, Parent> controllersAndPanes = new HashMap<>();
        for (Field f:
             fields) {
            DataType dataType = f.getAnnotation(HierarchyAnnotation.class).dataType();
            Object value = f.get(hierarchyObject);
            ComponentInfo componentInfo = componentsInfoList.get(dataType);
            Parent pane = FXMLFileLoader.loadFXML(componentInfo.fxmlFile, componentInfo.controller, value);
            controllersAndPanes.put(componentInfo.controller,pane);
        }
        log.info("Controllers and panes loaded");
        return controllersAndPanes;
    }

}
