package serializers;

import hierarchy.HierarchyObject;

import java.io.IOException;
import java.util.ArrayList;

public interface Serializer <T> {
    void write(T[] objects, String fileName) throws IOException;
    ArrayList<T> read(String fileName) throws IOException;
}
