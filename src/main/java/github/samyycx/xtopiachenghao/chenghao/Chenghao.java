package github.samyycx.xtopiachenghao.chenghao;

import java.util.HashMap;
import java.util.Map;

public class Chenghao {

    private String name;
    private int duration;
    private long timeOnCreated;
    private boolean isDefault;
    private int priority;

    private Chenghao() {
    }
    public Chenghao(String name, long timeOnCreated, int duration, boolean isDefault, int priority) {
        this.name = name;
        this.timeOnCreated = timeOnCreated;
        this.duration = duration;
        this.isDefault = isDefault;
        this.priority = priority;
    }

    public Map<String, String> toMap() {

        return new HashMap<String, String>() {{
            put("name", name);
            put("timeOnCreated", String.valueOf(timeOnCreated));
            put("duration", String.valueOf(duration));
            put("isDefault", String.valueOf(isDefault));
            put("priority", String.valueOf(priority));
        }};
    }

    public static Chenghao fromMap(Map<?, ?> map) {
        return new Chenghao(
                (String) map.get("name"),
                Long.parseLong((String) map.get("timeOnCreated")),
                Integer.parseInt((String) map.get("duration")),
                Boolean.parseBoolean((String) map.get("isDefault")),
                Integer.parseInt((String) map.get("priority"))
        );
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public long getTimeOnCreated() {
        return timeOnCreated;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setTimeOnCreated(long timeOnCreated) {
        this.timeOnCreated = timeOnCreated;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
