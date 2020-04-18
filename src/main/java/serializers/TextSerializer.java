package serializers;


import annotations.SerializerAnnotation;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@SerializerAnnotation(type = SerializersTypes.text)
public class TextSerializer<T> implements Serializer<T> {

    private final long timeout;
    private final TimeUnit timeUnit;


    @Override
    public void write(T[] objects, String fileName) throws IOException {

    }

    @Override
    public List<T> read(String fileName) throws IOException {
        return null;
    }
}
