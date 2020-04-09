package Hierarchy;

import Annotations.HierarchyAnnotation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class HierarchyHandler {

    //Map of classes' labels with corresponding class
    private final Map<String, Class<?>> hierarchyClassesList = new HashMap<>();

    public HierarchyHandler() {
        hierarchyClassesList.putAll(Map.ofEntries(
                Map.entry(Newspaper.class.getAnnotation(HierarchyAnnotation.class).label(), Newspaper.class)
        ));
    }

    public Set<String> getAllClassesNames() {
        return hierarchyClassesList.keySet();
    }

    public Field[] getFields(String className) throws ClassNotFoundException {
        try {
            Class<HierarchyObject> hierarchyClass = (Class<HierarchyObject>) hierarchyClassesList.get(className);
            return hierarchyClass.getFields();
        } catch (Exception e) {
            throw new ClassNotFoundException();
        }
    }

    public Field[] getFields(HierarchyObject hierarchyObject) throws ClassNotFoundException {
        String className = hierarchyObject.getClass().getAnnotation(HierarchyAnnotation.class).label();
        return getFields(className);
    }


    public HierarchyObject getDefaultHierarchyObject(String className) throws ClassNotFoundException {
        try {
            Class<HierarchyObject> hierarchyObjectClass = (Class<HierarchyObject>) hierarchyClassesList.get(className);
            return hierarchyObjectClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new ClassNotFoundException();
        }
    }

    public void setHierarchyObjectField(String label, HierarchyObject hierarchyObject, Object value) throws IllegalAccessException, ClassNotFoundException, NoSuchFieldException {
        Field[] fields = getFields(hierarchyObject);
        for (Field field : fields) {
            if (field.getAnnotation(HierarchyAnnotation.class).label().equals(label)) {
                field.set(hierarchyObject, value);
                break;
            }
        }
        throw new NoSuchFieldException();
    }


}
