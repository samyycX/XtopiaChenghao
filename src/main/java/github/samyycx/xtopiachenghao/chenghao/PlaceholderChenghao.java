package github.samyycx.xtopiachenghao.chenghao;

import github.samyycx.xtopiachenghao.utils.TextUtils;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class PlaceholderChenghao {

    private String name;
    private String placeholder;
    private String value;
    private int priority;

    private PlaceholderChenghao() {

    }

    public PlaceholderChenghao(String name, String placeholder, String value, int priority) {
        this.name = name;
        this.placeholder = placeholder;
        this.value = value;
        this.priority = priority;
    }

    public Map<String, String> toMap() {

        return new HashMap<String, String>() {{
            put("name", name);
            put("placeholder", placeholder);
            put("value", value);
            put("priority", String.valueOf(priority));
        }};
    }
    public static PlaceholderChenghao fromConfigurationSection(ConfigurationSection section) {
        PlaceholderChenghao pc = new PlaceholderChenghao();

        pc.setName(TextUtils.colorize(section.getString("name")));
        pc.setPlaceholder(section.getString("placeholder"));
        pc.setValue(section.getString("value"));
        pc.setPriority(section.getInt("priority"));

        return pc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
