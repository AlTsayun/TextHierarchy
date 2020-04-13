package fx.listComponents;

import annotations.EnumAnnotation;
import annotations.HierarchyAnnotation;
import hierarchy.DataType;
import hierarchy.HierarchyHandler;
import hierarchy.HierarchyObject;
import fx.FXMLFileLoader;
import fx.FXMLFileLoaderResponse;
import javafx.scene.Parent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.*;

@Slf4j
public class ComponentsHandler {
    //    Map of dataTypes' with corresponding controller classes .fxml files
    private final Map<DataType, ComponentInfo> componentsInfoList = new HashMap<>();

    public ComponentsHandler() {
        componentsInfoList.putAll(Map.ofEntries(
                Map.entry(DataType.object, new ComponentInfo(ObjectComponent.class, "objectComponent")),
                Map.entry(DataType.integer, new ComponentInfo(IntegerComponent.class, "integerComponent")),
                Map.entry(DataType.bool, new ComponentInfo(BoolComponent.class, "boolComponent")),
                Map.entry(DataType.date, new ComponentInfo(DateComponent.class, "dateComponent")),
                Map.entry(DataType.enumeration, new ComponentInfo(EnumerationComponent.class, "enumerationComponent")),
                Map.entry(DataType.string, new ComponentInfo(StringComponent.class, "stringComponent"))
        ));
    }


    @SneakyThrows
    public LoadedHandlerResponse loadComponents(HierarchyObject hierarchyObject) {
        HierarchyHandler hierarchyHandler = new HierarchyHandler();
        Field[] fields = hierarchyHandler.getFields(hierarchyObject);
        Map<Field, Component> fieldComponentMap = new HashMap<>();
        Collection<Parent> panes = new ArrayList<>();
        for (Field f : fields) {
            DataType dataType = f.getAnnotation(HierarchyAnnotation.class).dataType();
            ComponentConstructorParam param = new ComponentConstructorParam(f.getAnnotation(HierarchyAnnotation.class).label(), f.get(hierarchyObject));
            ComponentInfo componentInfo = componentsInfoList.get(dataType);
            FXMLFileLoaderResponse<Object, Object> loaderResponse = FXMLFileLoader.loadFXML(componentInfo.fxmlFile, componentInfo.controller, param);

            panes.add((Parent) loaderResponse.loadedObject);
            fieldComponentMap.put(f, (Component) loaderResponse.controller);
        }
        log.info("Controllers and panes loaded");
        return new LoadedHandlerResponse(fieldComponentMap, panes);
    }
}
