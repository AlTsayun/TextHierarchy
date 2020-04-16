package serializers;

import lombok.ToString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BinarySerializerTest {

    @Test
    void givenObjectsArrayAndFilePath_whenWriting_thenSavingObjectsToFile(){
        BinarySerializer<Integer> integerBinarySerializer = new BinarySerializer<>();

        assertDoesNotThrow(() ->{
            integerBinarySerializer.write(new Integer[]{1, 2, 3}, "test.txt");

        });
    }

    @Test
    void givenEmptyArrayAndFilePath_whenWriting_thenSavingObjectsToFile(){
        BinarySerializer<Integer> integerBinarySerializer = new BinarySerializer<>();

        assertDoesNotThrow(() ->{
            integerBinarySerializer.write(new Integer[]{1, 2, 3}, "test.txt");

        });
    }

    @Test
    void givenFilePath_whenReading_thenThrowsIOException(){
        BinarySerializer<Integer> integerBinarySerializer = new BinarySerializer<>();
        assertThrows(IOException.class,() -> {
            ArrayList<Integer> integers = integerBinarySerializer.read("test.txt");
            System.out.println(integers.toString());
        });
    }

    @Test
    void givenWrongFilePath_whenReading_thenThrowsIOException(){
        BinarySerializer<Integer> integerBinarySerializer = new BinarySerializer<>();
        assertThrows(IOException.class,() -> {
            ArrayList<Integer> integers = integerBinarySerializer.read("teasdst.txt");
            System.out.println(integers.toString());
        });
    }
}