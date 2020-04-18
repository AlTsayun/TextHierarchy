package hierarchy;

import annotations.HierarchyAnnotation;
import serializers.ClassesHandler;

import java.lang.reflect.Field;
import java.util.*;

public class HierarchyHandler implements ClassesHandler {

    //Map of classes' labels with corresponding class
    private final Map<String, Class<?>> hierarchyClassesList = new HashMap<>();

    public HierarchyHandler() {
        hierarchyClassesList.putAll(Map.ofEntries(
                Map.entry(NewsArticle.class.getAnnotation(HierarchyAnnotation.class).label(), NewsArticle.class),
                Map.entry(Newspaper.class.getAnnotation(HierarchyAnnotation.class).label(), Newspaper.class),
                Map.entry(Novel.class.getAnnotation(HierarchyAnnotation.class).label(), Novel.class),
                Map.entry(Poem.class.getAnnotation(HierarchyAnnotation.class).label(), Poem.class),
                Map.entry(Prose.class.getAnnotation(HierarchyAnnotation.class).label(), Prose.class),
                Map.entry(Schoolbook.class.getAnnotation(HierarchyAnnotation.class).label(), Schoolbook.class),
                Map.entry(Text.class.getAnnotation(HierarchyAnnotation.class).label(), Text.class)
                ));
    }

    public Set<String> getAllClassesNames() {
        return hierarchyClassesList.keySet();
    }



    public Field[] getFields(String className) throws ClassNotFoundException {
        try {
            Class<HierarchyObject> hierarchyClass = (Class<HierarchyObject>) hierarchyClassesList.get(className);
            Field[] fields = hierarchyClass.getFields();
            return fields;
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


    @Override
    public Class<?> getClassByName(String name)throws ClassNotFoundException {
        Class<?> value = hierarchyClassesList.get(name);
        if(value == null){
            throw new ClassNotFoundException("Cannot find class \"" + name + "\"");
        }
        return value;
    }
}
