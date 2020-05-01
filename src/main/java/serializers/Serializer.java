package serializers;

import java.io.IOException;
import java.util.List;

public interface Serializer <T> {
    byte[] serialize(T[] objects) throws IOException;
    List<T> deserialize(byte[] data) throws IOException;
}
