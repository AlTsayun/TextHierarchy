package plugins;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PluginsLoaderTest {


    @Test
    void givenFolderWithFiles_whenLoading_thenCorrect(){
        assertDoesNotThrow(() -> {
            PluginsLoader loader = new PluginsLoader();
            System.out.println(loader.getPluginsNames());
        });
    }

    @Test
    void givenClassName_whenLoadingClass_thenCorrect(){
        assertDoesNotThrow(() -> {
            PluginsLoader loader = new PluginsLoader();
            System.out.println(loader.loadPlugin("Base64"));
        });
    }

}