package hierarchy;

import annotations.HierarchyAnnotation;

import java.time.LocalDate;

@HierarchyAnnotation(dataType = DataType.object, label = "Новостная статья")
public class NewsArticle extends Prose implements HierarchyObject {

    @HierarchyAnnotation(dataType = DataType.object, label = "Газета")
    public Newspaper newspaper;

    @HierarchyAnnotation(dataType = DataType.date, label = "Дата публикации")
    public LocalDate date;

    public NewsArticle() {
        super();
        newspaper = new Newspaper();
        date = LocalDate.now();
    }
}