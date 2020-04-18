package hierarchy.dataEnums;

import annotations.HierarchyAnnotation;
import serializers.ClassesHandler;

import java.util.HashMap;
import java.util.Map;

public class DataEnumsHandler implements ClassesHandler {
    private final Map<String, Class<?>> enumsMap = new HashMap<>();

    public DataEnumsHandler() {
        enumsMap.putAll(Map.ofEntries(
                Map.entry(Language.class.getAnnotation(HierarchyAnnotation.class).label(),Language.class),
                Map.entry(Rhythms.class.getAnnotation(HierarchyAnnotation.class).label(),Rhythms.class),
                Map.entry(TextStyle.class.getAnnotation(HierarchyAnnotation.class).label(),TextStyle.class)
        ));
    }

    @Override
    public Class<?> getClassByName(String name) throws ClassNotFoundException {
        Class<?> value = enumsMap.get(name);
        if(value == null){
            throw new ClassNotFoundException("Cannot find enum \"" + name + "\"");
        }
        return value;
    }
}
