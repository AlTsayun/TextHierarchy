package annotations;

import serializers.SerializersTypes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface SerializerAnnotation {
    SerializersTypes type();
}
