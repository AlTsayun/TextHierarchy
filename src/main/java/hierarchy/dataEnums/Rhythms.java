package hierarchy.dataEnums;

import annotations.EnumAnnotation;
import annotations.HierarchyAnnotation;
import hierarchy.DataType;

@HierarchyAnnotation(dataType = DataType.enumeration, label = "Вид ритма")
public enum Rhythms {

    @EnumAnnotation(fullName = "Ямб")
    lamb,
    @EnumAnnotation(fullName = "Хорей")
    trochee,
    @EnumAnnotation(fullName = "Спондей")
    spondee,
    @EnumAnnotation(fullName = "Анапест")
    anapest,
    @EnumAnnotation(fullName = "Дактиль")
    dactyl
}
