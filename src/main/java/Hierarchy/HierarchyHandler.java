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

    public Set<String> getAllClassesNames(){
        return hierarchyClassesList.keySet();
    }

    public Field[] getFields(String className){
        Class<HierarchyObject> hierarchyClass = (Class<HierarchyObject>) hierarchyClassesList.get(className);
        return hierarchyClass.getFields();
    }
    public Field[] getFields(HierarchyObject hierarchyObject){
        String className = hierarchyObject.getClass().getAnnotation(HierarchyAnnotation.class).label();
        return getFields(className);
    }

    @Deprecated
    public DataType[] getFieldsTypes(String className){
        //order is important!!!

        Field[] fields = getFields(className);

        List<DataType> fieldsTypes = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            fieldsTypes.add(i,fields[i].getAnnotation(HierarchyAnnotation.class).dataType());
        }
        return fieldsTypes.toArray(DataType[]::new);
    }


    public HierarchyObject getDefaultHierarchyObject(String className) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<HierarchyObject> hierarchyObjectClass = (Class<HierarchyObject>) hierarchyClassesList.get(className);
        return hierarchyObjectClass.getConstructor().newInstance();
    }

    public void setHierarchyObjectField(String label, HierarchyObject hierarchyObject, Object value) throws IllegalAccessException {
        Field[] fields = getFields(hierarchyObject);
        for (Field field: fields) {
            if(field.getAnnotation(HierarchyAnnotation.class).label().equals(label)){
                field.set(hierarchyObject,value);
                break;
            }
        }
    }


}
