package hierarchy;

import annotations.HierarchyAnnotation;

@HierarchyAnnotation(dataType = DataType.object, label = "Газета")
public class Newspaper extends HierarchyObject {

    @HierarchyAnnotation(dataType = DataType.string, label = "Главный редактор")
    public String editor;

    @HierarchyAnnotation(dataType = DataType.string, label = "Город")
    public String city;

    @HierarchyAnnotation(dataType = DataType.integer, label = "Сколько раз в месяц издаётся")
    public int perMonth;

    public Newspaper() {
        editor = "ФИО";
        city = "Минск";
        perMonth = 1;
    }
}


