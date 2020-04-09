package Hierarchy;

import Annotations.HierarchyAnnotation;

@HierarchyAnnotation(dataType = DataType.object, label = "Газета")
public class Newspaper implements HierarchyObject {
    public String editor;
    public String city;
    public int perMonth;

    public Newspaper() {
        editor = "ФИО";
        city = "Минск";
        perMonth = 1;
    }
}
