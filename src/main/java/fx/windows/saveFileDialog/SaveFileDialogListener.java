package fx.windows.saveFileDialog;

import serializers.SerializersTypes;

public interface SaveFileDialogListener {
    void sendFileInfo(String path, SerializersTypes serializersType, String pluginName);
}
