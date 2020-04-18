package serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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





    @Test
    public void whenSerializingJava8Date_thenCorrect()
            throws JsonProcessingException {
        LocalDate date = LocalDate.of(2014, 12, 20);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String result = mapper.writeValueAsString(date);
        assertEquals(result, "\"2014-12-20\"");
    }
}