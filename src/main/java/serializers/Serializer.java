package serializers;

import hierarchy.HierarchyObject;

import java.io.IOException;
import java.util.ArrayList;

public interface Serializer {
    void write(HierarchyObject[] hierarchyObjects, String fileName) throws IOException;
    ArrayList<HierarchyObject> read(String fileName) throws IOException;
}
