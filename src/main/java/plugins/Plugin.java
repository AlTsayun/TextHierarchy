package plugins;

import java.io.IOException;

public interface Plugin {
    void save(byte[] data, String fileNameWithoutExtension) throws IOException;
    byte[] load(String fileNameWithExtension) throws IllegalArgumentException, IOException;
}
