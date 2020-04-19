package serializers;

import hierarchy.HierarchyObject;
import hierarchy.NewsArticle;
import hierarchy.Newspaper;
import hierarchy.Text;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class JsonSerializerTest {

    @Test
    void givenHierarchyObjectAndFilePath_whenWriting_thenSavingItToFile (){
        final JsonSerializer<HierarchyObject> serializer = new JsonSerializer<>(10, TimeUnit.SECONDS);
        assertDoesNotThrow(() ->{
            serializer.write(new HierarchyObject[]{new NewsArticle(), new Text()}, "test.txt");
        });
    }


    @Test
    void givenLocalFile_whenReading_thenLoadedObjects (){
        final JsonSerializer<HierarchyObject> serializer = new JsonSerializer<>(10, TimeUnit.SECONDS);
        assertDoesNotThrow(() ->{
            List<HierarchyObject> hierarchyObjects = serializer.read("test.txt");
            System.out.println(hierarchyObjects.toString());
        });
    }
}