package hierarchy;

import annotations.HierarchyAnnotation;
import hierarchy.dataEnums.Language;
import hierarchy.dataEnums.TextStyle;
import lombok.ToString;


@HierarchyAnnotation(dataType = DataType.object, label = "Текст")
public class Text extends HierarchyObject {

    @HierarchyAnnotation(dataType = DataType.integer, label = "Количество символов")
    public int symbolCount;

    @HierarchyAnnotation(dataType = DataType.enumeration, label = "Язык")
    public Language language;

    @HierarchyAnnotation(dataType = DataType.enumeration, label = "Стиль текста")
    public TextStyle style;

    @HierarchyAnnotation(dataType = DataType.string, label = "Заголовок")
    public String header;

    public Text() {
        symbolCount = 100;
        language = Language.ru;
        style = TextStyle.writing;
        header= "Заголовок";
    }
}
