package serializers;

import java.io.IOException;
import java.util.List;

public interface Serializer <T> {
    void write(T[] objects, String fileName) throws IOException;
    List<T> read(String fileName) throws IOException;
}
