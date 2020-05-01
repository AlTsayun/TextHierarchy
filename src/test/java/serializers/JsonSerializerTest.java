package serializers;

import hierarchy.HierarchyObject;
import hierarchy.Newspaper;
import hierarchy.Text;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class JsonSerializerTest {

    @Test
    void givenHierarchyObjects_whenSerialize_thenSavingItByteArray (){
        final JsonSerializer<HierarchyObject> serializer = new JsonSerializer<>(10, TimeUnit.SECONDS);
        assertDoesNotThrow(() ->{
            System.out.println(Arrays.toString(serializer.serialize(new HierarchyObject[]{new Newspaper(), new Text()})));

        });
    }


    @Test
    void givenByteArray_whenDeserialize_thenLoadedObjects(){
        final JsonSerializer<HierarchyObject> serializer = new JsonSerializer<>(10, TimeUnit.SECONDS);
        assertDoesNotThrow(() ->{
            HierarchyObject[] objects = {new Newspaper(), new Text()};
            System.out.println(Arrays.toString(objects));
            byte[] data = serializer.serialize(objects);
            List<HierarchyObject> hierarchyObjects = serializer.deserialize(data);
            System.out.println(hierarchyObjects.toString());
        });
    }
}