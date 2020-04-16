package serializers;

import hierarchy.HierarchyObject;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.*;
@RequiredArgsConstructor
public class BinarySerializer<T> implements Serializer<T> {

    private final long timeout;
    private final TimeUnit timeUnit;


    @Override
    public void write(T[] objects, String fileName) throws IOException {
        try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(fileName))){
        for (T o: objects) {
            objectOutputStream.writeObject(o);
        }
        objectOutputStream.writeObject( (T) null);
        }
    }

    @Override
    public ArrayList<T> read(String fileName) throws IOException {

        ArrayList<T> objects;

        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Future future = executor.submit(() -> {
            try(ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(fileName))){
                ArrayList<T> objectsWhileReading = new ArrayList<>();
                T o;
                while ((o = (T) objectInputStream.readObject()) != null) {

                    objectsWhileReading.add(o);
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
