package serializers;

import hierarchy.HierarchyHandler;
import hierarchy.HierarchyObject;
import hierarchy.Newspaper;
import hierarchy.Text;
import hierarchy.dataEnums.DataEnumsHandler;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class TextSerializerTest {

    @Test
    void givenObjectsArrayAndFilePath_whenWriting_thenSavingObjectsToFile(){
        TextSerializer<HierarchyObject> serializer = new TextSerializer<>(10, TimeUnit.SECONDS, new HierarchyHandler(), new DataEnumsHandler());

        assertDoesNotThrow(() ->{
            serializer.write(new HierarchyObject[]{new Newspaper(), new Text()}, "test.txt");

        });
    }

    @Test
    void givenEmptyArrayAndFilePath_whenWriting_thenSavingObjectsToFile(){
        TextSerializer<HierarchyObject> serializer = new TextSerializer<>(10, TimeUnit.SECONDS, new HierarchyHandler(), new DataEnumsHandler());

        assertDoesNotThrow(() ->{
            serializer.write(new HierarchyObject[]{}, "test.txt");
        });
    }

    @Test
    void givenFilePath_whenReading_thenDoesntThrow(){
        TextSerializer<HierarchyObject> serializer = new TextSerializer<>(10, TimeUnit.SECONDS, new HierarchyHandler(), new DataEnumsHandler());
        assertDoesNotThrow(() -> {
            List<HierarchyObject> objects = serializer.read("test.txt");
            System.out.println(objects.toString());
        });
    }

}