package serializers;

public interface ClassesHandler {
    Class<?> getClassByName(String name) throws ClassNotFoundException;
}
