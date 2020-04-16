package serializers;

import hierarchy.HierarchyObject;

import java.io.IOException;
import java.util.ArrayList;

public class TextSerializer<T> implements Serializer<T> {

    @Override
    public void write(T[] objects, String fileName) throws IOException {

    }

    @Override
    public ArrayList<T> read(String fileName) throws IOException {
        return null;
    }
}
