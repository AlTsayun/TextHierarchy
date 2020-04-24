package plugins;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class PluginsLoader {
    private final ClassLoader classLoader;
    private final URL resourceFolder;
    public PluginsLoader() throws Exception {
            resourceFolder = PluginsLoader.class.getResource("");
        if (resourceFolder == null){
            throw new Exception("Cannot find resource \"plugins\"");
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
                    pluginsNames.add(pluginFile.substring(0, pos));
                }
            }
        }

        return pluginsNames;
    }

    private ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public Class<?> loadPlugin(String className) throws ClassNotFoundException {
        return classLoader.loadClass("plugins." + className);
    }

} 