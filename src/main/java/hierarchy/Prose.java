package hierarchy;


import annotations.HierarchyAnnotation;

@HierarchyAnnotation(dataType = DataType.object, label = "Проза")
public class Prose extends Text implements HierarchyObject {

    @HierarchyAnnotation(dataType = DataType.integer, label = "Количество страниц")
    public int pagesCount;

    public Prose() {
        super();
        pagesCount = 100;
    }
}
