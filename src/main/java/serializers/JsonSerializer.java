package serializers;

import annotations.SerializerAnnotation;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@RequiredArgsConstructor
@SerializerAnnotation(type = SerializersTypes.json)
public class JsonSerializer<T> implements Serializer<T> {

    private final long timeout;
    private final TimeUnit timeUnit;

    class LocalDateSerializer extends StdSerializer<LocalDate>{

        public LocalDateSerializer(){
            this(null);
        }

        protected LocalDateSerializer(Class<LocalDate> t) {
            super(t);
        }

        @Override
        public void serialize(LocalDate value, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
            jgen.writeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
    }

    class LocalDateDeserializer extends StdDeserializer<LocalDate>{

        public LocalDateDeserializer() {
            this(null);
        }

        protected LocalDateDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public LocalDate deserialize(JsonParser jpars, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            String s = jpars.readValueAs(String.class);
            return LocalDate.parse(s);
        }
    }


    @Override
    public void write(T[] objects, String fileName) throws IOException {
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))){
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new SimpleModule().addSerializer(LocalDate.class, new LocalDateSerializer()));
            for (T o: objects) {
                bufferedWriter.write(objectMapper.writeValueAsString(o.getClass()));
                bufferedWriter.write(objectMapper.writeValueAsString(o));
            }
        }
    }

    @Override
    public List<T> read(String fileName) throws IOException {
        List<T> objects;

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future future = executor.submit(() -> {
            try{

                List<T> oList = new ArrayList<T>();
                String str = Files.readString(Path.of(fileName));

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new SimpleModule().addDeserializer(LocalDate.class, new LocalDateDeserializer()));
                JsonFactory factory = new JsonFactory();
                factory.setCodec(objectMapper);
                JsonParser parser = factory.createParser(str);

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
