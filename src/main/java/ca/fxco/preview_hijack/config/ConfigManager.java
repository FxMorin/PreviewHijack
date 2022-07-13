package ca.fxco.preview_hijack.config;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.lang3.SerializationException;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.*;
import org.yaml.snakeyaml.representer.Representer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;

public class ConfigManager {

    private final Path configPath;
    private final Yaml yaml;

    public ConfigManager(String modId) {
        DumperOptions options = new DumperOptions();
        options.setAllowReadOnlyProperties(true);
        this.yaml = new Yaml(new Constructor(PHConfig.class), new SmartRepresenter(), options);
        this.configPath = FabricLoader.getInstance().getConfigDir().resolve(modId + ".yaml");
    }

    public PHConfig loadConfig() {
        if (!Files.exists(this.configPath)) {
            PHConfig config = new PHConfig().validateOnLoad(); // Default
            saveConfig(config);
            return config;
        }
        try (InputStream stream = Files.newInputStream(this.configPath)) {
            return ((PHConfig)this.yaml.load(stream)).validateOnLoad();
        } catch (IllegalStateException | IOException e) {
            throw new SerializationException(e);
        }
    }

    public void saveConfig(PHConfig config) { // TODO: Modifying config through commands
        try {
            Files.createDirectories(this.configPath.getParent());
            Files.writeString(
                    this.configPath,
                    Pattern.compile("^- \\{(.*)}$", Pattern.MULTILINE)
                    .matcher(this.yaml.dump(config))
                    .replaceAll("- $1")
            );
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    private static class SmartRepresenter extends Representer {
        @Override
        protected NodeTuple representJavaBeanProperty(Object javaBean, Property property,
                                                      Object propertyValue, Tag customTag) {
            if (propertyValue == null) return null;
            NodeTuple tuple = super.representJavaBeanProperty(javaBean, property, propertyValue, customTag);
            Node valueNode = tuple.getValueNode();
            if (Tag.NULL.equals(valueNode.getTag())) return null; // skip 'null' values again
            if (valueNode instanceof CollectionNode) {
                if (Tag.SEQ.equals(valueNode.getTag())) {
                    SequenceNode seq = (SequenceNode) valueNode;
                    if (seq.getValue().isEmpty()) return null; // skip empty lists
                }
                if (Tag.MAP.equals(valueNode.getTag())) {
                    MappingNode seq = (MappingNode) valueNode;
                    if (seq.getValue().isEmpty()) return null; // skip empty maps
                }
            }
            return tuple;
        }

        @Override
        protected Set<Property> getProperties(Class<?> type) {
            Set<Property> reversed = new TreeSet<>(Collections.reverseOrder());
            reversed.addAll(super.getProperties(type));
            List<Property> result = new ArrayList<>(reversed);
            result.sort((o1, o2) -> {
                Weight weight1 = o1.getAnnotation(Weight.class);
                Weight weight2 = o2.getAnnotation(Weight.class);
                return weight1 == null ?
                        (weight2 == null ? 0 : 1) :
                        (weight2 == null ? -1 : (weight1.value() > weight2.value() ? -1 : 1));
            });
            return new LinkedHashSet<>(result);
        }
    }
}
