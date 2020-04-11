package fx.listComponents;

import javafx.scene.Node;
import javafx.scene.Parent;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class LoadedHandlerResponse {
    public final Map<Field, Component> fieldComponentMap;
    public final Collection<? extends Node> panes;
}
