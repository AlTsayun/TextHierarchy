package hierarchy.dataEnums;

import annotations.EnumAnnotation;
import annotations.HierarchyAnnotation;
import hierarchy.DataType;

@HierarchyAnnotation(dataType = DataType.enumeration, label = "Стиль текста")
public enum TextStyle {

    @EnumAnnotation(fullName = "Научный")
    scientific,
    @EnumAnnotation(fullName = "Официальный")
    officialese,
    @EnumAnnotation(fullName = "Публицистический")
    news,
    @EnumAnnotation(fullName = "Разговорный")
    colloquial,
    @EnumAnnotation(fullName = "Художественный")
    writing
}
