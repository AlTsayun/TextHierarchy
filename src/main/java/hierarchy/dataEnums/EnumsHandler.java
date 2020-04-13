package hierarchy.dataEnums;

import annotations.HierarchyAnnotation;
import lombok.ToString;

import java.lang.reflect.Field;
import java.util.Map;

public class EnumsHandler {
    private Map<String, Class<?>> enumsMap;

    public EnumsHandler() {
        enumsMap.putAll(Map.ofEntries(
                Map.entry(Language.class.getAnnotation(HierarchyAnnotation.class).label(),Language.class),
                Map.entry(Rhythms.class.getAnnotation(HierarchyAnnotation.class).label(),Rhythms.class),
                Map.entry(TextStyle.class.getAnnotation(HierarchyAnnotation.class).label(),TextStyle.class)
        ));
    }
}
