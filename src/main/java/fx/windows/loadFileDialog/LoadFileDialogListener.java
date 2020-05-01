package fx.windows.loadFileDialog;

import serializers.SerializersTypes;

public interface LoadFileDialogListener {
    void sendFileInfo(String path, SerializersTypes serializersType);
}
