package hierarchy.dataEnums;

import annotations.EnumAnnotation;
import annotations.HierarchyAnnotation;
import hierarchy.DataType;

@HierarchyAnnotation(dataType = DataType.enumeration, label = "Язык")
public enum Language {
    @EnumAnnotation(fullName = "Русский")
    ru,
    @EnumAnnotation(fullName = "Английский")
    en,
    @EnumAnnotation(fullName = "Немецкий")
    ge,
    @EnumAnnotation(fullName = "Французский")
    fr
}
