package plugins;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;


@Slf4j
public class PluginsLoader {
    private final ClassLoader classLoader;
    private final URL resourceFolder;


    public PluginsLoader() throws MissingResourceException {
            resourceFolder = PluginsLoader.class.getResource("");
        if (resourceFolder == null){
            throw new MissingResourceException("Cannot find resource", "plugins", "");
        }
        URL[] urls = new URL[]{resourceFolder};
        classLoader = new URLClassLoader(urls);


    }

    public List<String> getPluginsNames() throws IOException {
        List<String> pluginsNames = new ArrayList<>();

        try (InputStream in = resourceFolder.openStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String pluginFile;

            while ((pluginFile = br.readLine()) != null) {
                int pos = pluginFile.lastIndexOf(".");
                if (pluginFile.substring(pos).equals(".class")){
                    String pluginName = pluginFile.substring(0, pos);
                    try {
                        Class<?> pluginClass = loadPlugin(pluginName);
                        if(Plugin.class.isAssignableFrom(pluginClass) &&
                                (!Plugin.class.equals(pluginClass))){
                            pluginsNames.add(pluginName);
                        }
                    } catch (ClassNotFoundException e) {
                        log.info("Failed loading \"" + pluginName + "\"");
                    }
                }
            }
        }

        return pluginsNames;
    }

    public Plugin getPluginForFileExtension(String fileExtension) throws IllegalArgumentException, IOException {

        try {
            List<String> pluginsNames = getPluginsNames();
            for (String pluginName :
                    pluginsNames) {
                Plugin plugin = (Plugin) loadPlugin(pluginName).getConstructor().newInstance();
                if (plugin.getFileExtension().equals(fileExtension)){
                    return plugin;
                }
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            throw new IOException("Some plugins to be loaded might not be applicable.");
        }
        throw new IllegalArgumentException("No plugin for such fileExtension found.");
    }

    private ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public Class<?> loadPlugin(String className) throws ClassNotFoundException {
        return classLoader.loadClass("plugins." + className);
    }

} 