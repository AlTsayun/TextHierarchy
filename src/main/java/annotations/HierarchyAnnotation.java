package annotations;

import hierarchy.DataType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface HierarchyAnnotation {
    DataType dataType();
    String label();
}