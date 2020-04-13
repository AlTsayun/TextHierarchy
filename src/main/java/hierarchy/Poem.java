package hierarchy;

import annotations.HierarchyAnnotation;
import hierarchy.dataEnums.Rhythms;

@HierarchyAnnotation(dataType = DataType.object, label = "Стихотворение")
public class Poem extends Text implements HierarchyObject {

    @HierarchyAnnotation(dataType = DataType.integer, label = "Количество строк")
    public int linesCount;

    @HierarchyAnnotation(dataType = DataType.bool, label = "Рифмованный")
    public boolean isRhyme;

    @HierarchyAnnotation(dataType = DataType.enumeration, label = "Вид ритма")
    public Rhythms rhythm;

    public Poem() {
        super();
        linesCount = 16;
        isRhyme = true;
        rhythm = Rhythms.lamb;

    }
}
