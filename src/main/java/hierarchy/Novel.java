package hierarchy;

import annotations.HierarchyAnnotation;

@HierarchyAnnotation(dataType = DataType.object, label = "Роман")
public class Novel extends Prose implements HierarchyObject {
    @HierarchyAnnotation(dataType = DataType.integer, label = "Количество глав")
    public int chaptersCount;

    public Novel() {
        super();
        chaptersCount = 3;
    }
}
