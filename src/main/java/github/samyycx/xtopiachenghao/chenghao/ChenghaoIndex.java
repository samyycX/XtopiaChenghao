package github.samyycx.xtopiachenghao.chenghao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChenghaoIndex implements Serializable {

    private String type;
    private int intValue;
    private String stringValue;



    public ChenghaoIndex() {

    }

    public ChenghaoIndex(int id) {
        this.type = "custom";
        this.intValue = id;
    }

    public ChenghaoIndex(String type, String value) {
        this.type = type;
        this.stringValue = value;
    }

    public HashMap<String, String> toMap() {
        return new HashMap<String, String>() {{
            put("type", type);
            switch (type) {
                case "custom":
                    put("value", String.valueOf(intValue));
                    break;
                case "permission":
                case "placeholder":
                    put("value", stringValue);
                    break;
            }
        }};
    }

    public static ChenghaoIndex fromMap(Map<?, ?> map) {
        ChenghaoIndex ci = new ChenghaoIndex();
        String type = (String) map.get("type");
        ci.type = type;
        switch (type) {
            case "custom":
                ci.intValue = Integer.parseInt((String) map.get("value"));
                break;
            case "permission":
            case "placeholder":
                ci.stringValue = (String) map.get("value");
                break;
        }

        return ci;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Integer) {
            return this.intValue == (Integer) obj;
        } else {
            return this.stringValue != null && this.stringValue.equals(obj);
        }
    }
}
