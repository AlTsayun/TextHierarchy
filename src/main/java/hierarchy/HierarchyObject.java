package hierarchy;

import annotations.HierarchyAnnotation;
import lombok.SneakyThrows;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HierarchyObject implements Serializable {
    @Override
    public String toString() {
        final HierarchyObject thisValue = this;
        final Class thisClass =this.getClass();
        HierarchyAnnotation annotation = (HierarchyAnnotation) thisClass.getAnnotation(HierarchyAnnotation.class);
        String str = annotation.label() + ":";
        return  str + Arrays.stream(thisClass.getFields()).map(new Function<Field, String>() {
            @Override
            @SneakyThrows
            public String apply(Field f) {
                Object value = f.get(thisValue);
                return f.getName() + ":" + value.toString();
            }
        }).collect(Collectors.joining(","));
    }
}
