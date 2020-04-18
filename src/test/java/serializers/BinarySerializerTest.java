package serializers;

import hierarchy.HierarchyObject;
import hierarchy.Newspaper;
import hierarchy.Text;
import lombok.ToString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class BinarySerializerTest {

    @Test
    void givenObjectsArrayAndFilePath_whenWriting_thenSavingObjectsToFile(){
        BinarySerializer<HierarchyObject> serializer = new BinarySerializer<>(10, TimeUnit.SECONDS);

        assertDoesNotThrow(() ->{
            serializer.write(new HierarchyObject[]{new Newspaper(), new Text()}, "test.txt");

        });
    }

    @Test
    void givenEmptyArrayAndFilePath_whenWriting_thenSavingObjectsToFile(){
        BinarySerializer<HierarchyObject> serializer = new BinarySerializer<>(10, TimeUnit.SECONDS);

        assertDoesNotThrow(() ->{
            serializer.write(new HierarchyObject[]{}, "test.txt");
        });
    }

    @Test
    void givenFilePath_whenReading_thenDoesntThrow(){
        BinarySerializer<HierarchyObject> serializer = new BinarySerializer<>(10, TimeUnit.SECONDS);
        assertDoesNotThrow(() -> {
            List<HierarchyObject> objects = serializer.read("test.txt");
            System.out.println(objects.toString());
        });
    }

    @Test
    void givenWrongFilePath_whenReading_thenThrowsIOException(){
        BinarySerializer<Integer> integerBinarySerializer = new BinarySerializer<>(10, TimeUnit.SECONDS);
        assertThrows(IOException.class,() -> {
            ArrayList<Integer> integers = integerBinarySerializer.read("test_test.txt");
            System.out.println(integers.toString());
        });
    }
}