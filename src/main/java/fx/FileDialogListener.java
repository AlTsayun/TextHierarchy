package fx;

import serializers.SerializersTypes;

public interface FileDialogListener {
    void sendFileInfo(String path, SerializersTypes serializersType);
}
