package serializers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hierarchy.HierarchyObject;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.*;

@RequiredArgsConstructor
public class JsonSerializer<T> implements Serializer<T> {

    private final long timeout;
    private final TimeUnit timeUnit;

    @Override
    public void write(T[] objects, String fileName) throws IOException {
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))){
        ObjectMapper objectMapper = new ObjectMapper();
            for (T o: objects) {
                bufferedWriter.write(objectMapper.writeValueAsString(o));
            }
        }
    }

    @Override
    public ArrayList<T> read(String fileName) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<T> objects;

        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Future future = executor.submit(() -> {
            try(ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(fileName))){
                ArrayList<T> objectsWhileReading = new ArrayList<>();
                String str;
                while ((str = (String) objectInputStream.readObject()) != null) {

                    objectsWhileReading.add(objectMapper.readValue(str));
                }
                return objectsWhileReading;
            } catch (IOException | ClassNotFoundException e) {
                throw  new IOException("Could not properly read objects");
            }
        });
        // This does not cancel the already-scheduled task.
        executor.shutdown();

        try {
            objects = (ArrayList<T>) future.get(timeout, timeUnit);
        }
        catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new IOException("Could not accomplish reading");
        }
        if (!executor.isTerminated()){
            executor.shutdownNow(); // If you want to stop the code that hasn't finished.
            throw new IOException("Reading timeout reached");
        }
        return objects;
    }
}
