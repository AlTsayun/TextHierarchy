package serializers;

import hierarchy.*;
import hierarchy.dataEnums.Language;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SerializersHandlerTest {

    @Test
    @SneakyThrows
    void givenObjectsAndSerializerTypeAndPath_whenWriting_thenSavingToFile(){
        assertDoesNotThrow(() ->{
            SerializersHandler handler = new SerializersHandler(SerializersTypes.text);
            System.out.println(Arrays.toString(handler.write(new HierarchyObject[]{new Newspaper(), new Text(), new NewsArticle(), new Novel(), new Poem(), new Prose(), new Schoolbook()})));

        });
    }
    @Test
    void givenLocalFile_whenReading_thenLoadedObjects (){
        assertDoesNotThrow(() ->{
            SerializersHandler handler = new SerializersHandler(SerializersTypes.text);
            byte[] bytes = handler.write(new HierarchyObject[]{new Newspaper(), new Text(), new NewsArticle(), new Novel(), new Poem(), new Prose(), new Schoolbook()});
            System.out.println(handler.read(bytes).toString());

        });
    }
}