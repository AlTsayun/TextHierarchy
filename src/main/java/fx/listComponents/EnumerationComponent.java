package fx.listComponents;

import annotations.EnumAnnotation;
import annotations.HierarchyAnnotation;
import hierarchy.dataEnums.Language;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import lombok.SneakyThrows;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EnumerationComponent implements Component{

    private String lblText;
    private Enum<?> value;
    @FXML
    private Label label;

    @FXML
    private ComboBox<Enum<?>> cbValue;

    public EnumerationComponent(ComponentConstructorParam param) {
        this.lblText = param.lblText;
        this.value = (Enum<?>) param.value;
    }

    @Override
    public Object getValue() throws IOException {
        return cbValue.getSelectionModel().getSelectedItem();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
         cbValue.setButtonCell( new ListCell<Enum<?>>() {
            @Override
            @SneakyThrows
            public void updateItem(Enum<?> item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setText(null);
                } else {
                    setText(item.getDeclaringClass().getField(item.toString()).getAnnotation(EnumAnnotation.class).fullName());
                }
            }
        });

        cbValue.setCellFactory(lv -> {
            ListCell<Enum<?>> cell = new ListCell<Enum<?>>() {
                @Override
                @SneakyThrows
                public void updateItem(Enum<?> item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setText(null);
                    } else {
                        setText(item.getDeclaringClass().getField(item.toString()).getAnnotation(EnumAnnotation.class).fullName());
                    }
                }
            };
            return cell;
        });
        ArrayList<Enum<?>> constants = Arrays.stream(value.getDeclaringClass().getFields()).map(new Function<Field, Enum<?>>() {
            @SneakyThrows
            @Override
            public Enum<?> apply(Field field) {
                    field.setAccessible(true);
                    Enum<?> constant = (Enum<?>) field.get(null);
                return constant;
            }
        }).collect(Collectors.toCollection(ArrayList::new));
        cbValue.setItems(FXCollections.observableArrayList(constants));
        cbValue.getSelectionModel().select(value);
        label.setText(lblText);
    }
}
