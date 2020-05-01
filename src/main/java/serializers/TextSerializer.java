package serializers;


import annotations.HierarchyAnnotation;
import annotations.SerializerAnnotation;
import hierarchy.DataType;
import hierarchy.HierarchyHandler;
import hierarchy.dataEnums.DataEnumsHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@SerializerAnnotation(type = SerializersTypes.text)
//readField() depends on
//      HierarchyAnnotation,
//      DataType,
//      toString() of
//          T
//          enums
//
// need dataType for resolving read values)
public class TextSerializer<T> implements Serializer<T> {

    private final long timeout;
    private final TimeUnit timeUnit;
    private final String valueSign = ":";
    private final String separateSign = ",";
    private final ClassesHandler objectsHandler;
    private final ClassesHandler enumsHandler;

    public TextSerializer(long timeout, TimeUnit timeUnit) {
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.objectsHandler = new HierarchyHandler();
        this.enumsHandler = new DataEnumsHandler();
    }

    @Override
    public byte[] serialize(T[] objects) throws IOException {
        try(    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(byteArrayOutputStream))){
            for (T o: objects) {
                bufferedWriter.write(o.toString() + "\n");
            }
            bufferedWriter.flush();
            return byteArrayOutputStream.toByteArray();
        }
    }

    @Override
    public List<T> deserialize(byte[] data) throws IOException {
        List<T> objects;

        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Future future = executor.submit(() -> {
            try(    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(byteArrayInputStream))){

                List<T> oList = new ArrayList<T>();
                String line;
                T o;
                while ((line = reader.readLine()) != null){
                    o = (T) readObject(line).readObj;
                    log.info("added object to list");
                    oList.add(o);
                }

                return oList;
            } catch (IOException e) {
                throw  new IOException("Could not properly read objects", e);
            }
        });
        // This does not cancel the already-scheduled task.
        executor.shutdown();

        try {
            objects = (List<T>) future.get(timeout, timeUnit);
        }
        catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new IOException("Could not accomplish reading", e);
        }
        if (!executor.isTerminated()){
            //Stop the code that hasn't finished.
            executor.shutdownNow();
        }
        return objects;
    }

    @RequiredArgsConstructor
    class ReadResponse{
        private final Object readObj;
        private final String remainingStr;
    }

    @SneakyThrows
    private ReadResponse readObject(String strToRead){
        Class<?> objectClass;
        String[] splittedStr = strToRead.split(valueSign, 2);
        strToRead = splittedStr[1];
        objectClass = objectsHandler.getClassByName(splittedStr[0]);
        Object obj = objectClass.getConstructor().newInstance();
        Map<String, Field> fieldNameFieldMap = Arrays.stream(objectClass.getFields()).
                collect(Collectors.toMap(f -> f.getName(), f -> f));
        String fieldName;
        while (!fieldNameFieldMap.isEmpty()){
            splittedStr = strToRead.split(valueSign, 2);
            fieldName = splittedStr[0];
            strToRead = splittedStr[1];
            Field field = fieldNameFieldMap.remove(fieldName);
            if (field == null){
                throw new IOException("Cannot find field in " + objectClass.toString() + "for \"" + fieldName + "\"");
            }
            ReadResponse readResponse = readField(strToRead, field);
            field.set(obj, readResponse.readObj);
            strToRead = readResponse.remainingStr;
        }

        splittedStr = strToRead.split(separateSign, 2);
        if (splittedStr.length > 1){
            //Removes comma after object
            strToRead = splittedStr[1];
        } else{
            //For root object last comma in line is used by last field
            //so no need to remove it
            strToRead = splittedStr[0];
        }
        return new ReadResponse(obj, strToRead);
    }

    @SneakyThrows
    private ReadResponse readField(String strToRead, Field field){
        DataType dataType = field.getAnnotation(HierarchyAnnotation.class).dataType();
        String[] splittedStr;
        switch (dataType){
            case object:{
                return readObject(strToRead);
            }
            case integer:{
                splittedStr = strToRead.split(separateSign,2);
                return new ReadResponse(Integer.parseInt(splittedStr[0]), splittedStr[1]);
            }
            case enumeration:{
                splittedStr = strToRead.split(valueSign, 2);
                Class<?> enumClass = enumsHandler.getClassByName(splittedStr[0]);
                strToRead = splittedStr[1];
                splittedStr = strToRead.split(separateSign, 2);
                Field enumConstantField = enumClass.getField(splittedStr[0]);
                enumConstantField.setAccessible(true);
                return new ReadResponse(enumConstantField.get(null), splittedStr[1]);
            }
            case string:{
                splittedStr = strToRead.split(separateSign,2);
                return new ReadResponse(splittedStr[0], splittedStr[1]);
            }
            case bool:{
                splittedStr = strToRead.split(separateSign,2);
                return new ReadResponse(Boolean.parseBoolean(splittedStr[0]), splittedStr[1]);
            }
            case date:{
                splittedStr = strToRead.split(separateSign,2);
                return new ReadResponse(LocalDate.parse(splittedStr[0]), splittedStr[1]);
            }
            default:{
                throw new IllegalStateException("Unknown DataType " + dataType.toString());
            }
        }
    }


}
