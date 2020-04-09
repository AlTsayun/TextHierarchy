package fx.listComponents;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ComponentInfo {
    public final Class<? extends Component> controller;
    public final String fxmlFile;
}
