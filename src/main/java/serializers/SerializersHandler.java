package serializers;

import hierarchy.HierarchyObject;

import java.io.IOException;
import java.util.ArrayList;

public class SerializersHandler {
    public void write(HierarchyObject[] hierarchyObjects, String path, SerializersTypes serializersType)throws IOException {
        Serializer serializer;
        switch (serializersType){
            case json: serializer = new JsonSerializer();
            case text: serializer = new TextSerializer();
            case binary: serializer = new BinarySerializer();
//            default: throw new
        }
//        serializer.write();

    };
    public ArrayList<HierarchyObject> read(String path, SerializersTypes serializersTypes)throws IOException{
    return null;
    }
}

