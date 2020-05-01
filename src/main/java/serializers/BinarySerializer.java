package serializers;

import annotations.SerializerAnnotation;
import hierarchy.Newspaper;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
@RequiredArgsConstructor
@SerializerAnnotation(type = SerializersTypes.binary)
public class BinarySerializer<T> implements Serializer<T> {

    private final long timeout;
    private final TimeUnit timeUnit;


    @Override
    public byte[] serialize(T[] objects) throws IOException {
        try(    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)){
            for (T o: objects) {
                objectOutputStream.writeObject(o.getClass());
                objectOutputStream.writeObject(((Object) o));
            }
            objectOutputStream.writeObject( (T) null);
            objectOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        }
    }

    @Override
    public List<T> deserialize(byte[] data) throws IOException {

        ArrayList<T> objects;

        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Future future = executor.submit(() -> {
            try(    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
                    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)){
                ArrayList<T> objectsWhileReading = new ArrayList<>();
                Class<?> objectClass;
                T o;
                while ((objectClass = (Class<?>) objectInputStream.readObject()) != null) {
                    o = (T) objectClass.cast(objectInputStream.readObject());
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
            // If you want to stop the code that hasn't finished.
            executor.shutdownNow();
        }
        return objects;
    }
}
