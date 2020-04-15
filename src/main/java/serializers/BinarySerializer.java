package serializers;

import hierarchy.HierarchyObject;

import java.io.*;
import java.util.ArrayList;

public class BinarySerializer implements Serializer {
    @Override
    public void write(HierarchyObject[] hierarchyObjects, String fileName) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(fileName));
        for (HierarchyObject ho: hierarchyObjects) {
            objectOutputStream.writeObject(ho);
        }
    }

    @Override
    public ArrayList<HierarchyObject> read(String fileName) throws IOException {
        ArrayList<HierarchyObject> hierarchyObjects = new ArrayList<>();

        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(fileName));

        return hierarchyObjects;
    }
}
