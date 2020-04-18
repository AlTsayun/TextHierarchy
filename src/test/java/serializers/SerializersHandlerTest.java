package serializers;

import hierarchy.HierarchyObject;
import hierarchy.Newspaper;
import hierarchy.Text;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SerializersHandlerTest {

    @Test
    @SneakyThrows
    void givenObjectsAndSerializerTypeAndPath_whenWriting_thenSavingToFile(){
        assertDoesNotThrow(() ->{
            SerializersHandler handler = new SerializersHandler(SerializersTypes.binary);
            handler.write(new HierarchyObject[]{new Newspaper(), new Text()},"test.txt");
        });
        System.out.println(new );
    }
    @Test
    void givenLocalFile_whenReading_thenLoadedObjects (){
        assertDoesNotThrow(() ->{
            SerializersHandler handler = new SerializersHandler(SerializersTypes.binary);
            System.out.println(handler.read("test.txt").toString());

        });
    }
}