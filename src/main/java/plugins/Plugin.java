package plugins;

public interface Plugin {
    byte[] convert(byte[] data);
    byte[] revert(byte[] data);
    String getFileExtension();
}
