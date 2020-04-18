package serializers;

import hierarchy.*;
import hierarchy.dataEnums.Language;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SerializersHandlerTest {

    @Test
    @SneakyThrows
    void givenObjectsAndSerializerTypeAndPath_whenWriting_thenSavingToFile(){
        assertDoesNotThrow(() ->{
            SerializersHandler handler = new SerializersHandler(SerializersTypes.text);
            handler.write(new HierarchyObject[]{new Newspaper(), new Text()},"test.txt");

        });
    }
    @Test
    void givenLocalFile_whenReading_thenLoadedObjects (){
        assertDoesNotThrow(() ->{
            SerializersHandler handler = new SerializersHandler(SerializersTypes.text);
            System.out.println(handler.read("test.txt").toString());

        });
    }
}