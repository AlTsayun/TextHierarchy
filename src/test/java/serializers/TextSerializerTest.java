package serializers;

import hierarchy.HierarchyObject;
import hierarchy.Newspaper;
import hierarchy.Text;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class TextSerializerTest {

    @Test
    void givenHierarchyObjects_whenSerialize_thenSavingItByteArray (){
        Serializer<HierarchyObject> serializer = new TextSerializer<>(10, TimeUnit.SECONDS);

        assertDoesNotThrow(() ->{
            System.out.println(Arrays.toString(serializer.serialize(new HierarchyObject[]{new Newspaper(), new Text()})));

        });
    }

    @Test
    void givenEmptyArray_whenSerialize_thenSavingNoneToByteArray(){
        Serializer<HierarchyObject> serializer = new TextSerializer<>(10, TimeUnit.SECONDS);

        assertDoesNotThrow(() ->{
            System.out.println(Arrays.toString(serializer.serialize(new HierarchyObject[]{})));
        });
    }

    @Test
    void givenByteArray_whenDeserialize_thenLoadedObjects(){
        Serializer<HierarchyObject> serializer = new TextSerializer<>(10, TimeUnit.SECONDS);
        assertDoesNotThrow(() -> {
            byte[] bytes = serializer.serialize(new HierarchyObject[]{new Newspaper(), new Text()});
            List<HierarchyObject> objects = serializer.deserialize(bytes);
            System.out.println(objects.toString());
        });
    }

}