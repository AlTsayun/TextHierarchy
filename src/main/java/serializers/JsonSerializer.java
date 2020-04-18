package serializers;

import annotations.SerializerAnnotation;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import hierarchy.Newspaper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@SerializerAnnotation(type = SerializersTypes.json)
public class JsonSerializer<T> implements Serializer<T> {

    private final long timeout;
    private final TimeUnit timeUnit;

    @Override
    public void write(T[] objects, String fileName) throws IOException {
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))){
        ObjectMapper objectMapper = new ObjectMapper();
            for (T o: objects) {
                bufferedWriter.write(objectMapper.writeValueAsString(o.getClass()));
                bufferedWriter.write(objectMapper.writeValueAsString(o));
            }
        }
    }

    @Override
    public List<T> read(String fileName) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        List<T> objects;

        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Future future = executor.submit(() -> {
            try{

                List<T> oList = new ArrayList<T>();
                String str = Files.readString(Path.of(fileName));
                JsonParser parser = new JsonFactory().createParser(str);

                while (parser.nextToken() != null){

                    //trap: without (Class<?>) objectClass does not equals the saved Class variable
                    //i dunno why it doesn't force to cast
                    Class<?> objectClass = (Class<?>) objectMapper.readValue(parser, Class.class);
                    T o = (T) objectMapper.readValue(parser, objectClass);
                    log.info("added object to list");
                    oList.add(o);
                }

                parser.close();

                return oList;
            } catch (IOException e) {
                throw  new IOException("Could not properly read objects", e);
            }
        });
        // This does not cancel the already-scheduled task.
        executor.shutdown();

        try {
            objects = (List<T>) future.get(timeout, timeUnit);
        }
        catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new IOException("Could not accomplish reading", e);
        }
        if (!executor.isTerminated()){
            //Stop the code that hasn't finished.
            executor.shutdownNow();
        }
        return objects;
    }
}
