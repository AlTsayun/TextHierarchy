package hierarchy;


import annotations.HierarchyAnnotation;

@HierarchyAnnotation(dataType = DataType.object, label = "Учебник")
public class Schoolbook extends Prose implements HierarchyObject {

    @HierarchyAnnotation(dataType = DataType.integer, label = "Количество упражнений")
    public int exercisesCount;

    @HierarchyAnnotation(dataType = DataType.string, label = "Предмет")
    public String discipline;

    public Schoolbook() {
        super();
        exercisesCount = 123;
        discipline = "Математика";
    }
}
