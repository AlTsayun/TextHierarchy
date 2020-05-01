package serializers;

import annotations.SerializerAnnotation;
import hierarchy.HierarchyObject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SerializersHandler {

    private final Map<SerializersTypes, Class<?>> serializersList = new HashMap<>();
    private final Serializer<HierarchyObject> serializer;
    private final long timeout = 40;
    private final TimeUnit timeUnit = TimeUnit.SECONDS;

    public SerializersHandler(SerializersTypes serializersType) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        serializersList.putAll(Map.ofEntries(
                Map.entry(BinarySerializer.class.getAnnotation(SerializerAnnotation.class).type(), BinarySerializer.class),
                Map.entry(JsonSerializer.class.getAnnotation(SerializerAnnotation.class).type(), JsonSerializer.class),
                Map.entry(TextSerializer.class.getAnnotation(SerializerAnnotation.class).type(), TextSerializer.class)
        ));
            this.serializer = (Serializer<HierarchyObject>) serializersList.get(serializersType).getConstructor(long.class, TimeUnit.class).newInstance(timeout, timeUnit);
    }

    public byte[] write(HierarchyObject[] hierarchyObjects)throws IOException {
        return serializer.serialize(hierarchyObjects);
    };
    public List<HierarchyObject> read(byte[] data)throws IOException{
        return serializer.deserialize(data);
    }
}

