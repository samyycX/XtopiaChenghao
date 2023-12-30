package github.samyycx.xtopiachenghao.chenghao;

import github.samyycx.xtopiachenghao.utils.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PermissionChenghao {

    private String name;
    private int priority;

    public PermissionChenghao(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    public PermissionChenghao() {

    }
    public List<String> toStringList() {

        return new ArrayList<String>() {{
            add("name="+name);
            add("priority="+priority);
        }};
    }
    public static PermissionChenghao fromStringList(List<String> list) {
        PermissionChenghao pc = new PermissionChenghao();

        for (String s : list) {
            String[] split = s.split("=");
            switch (split[0]) {
                case "name":
                    pc.setName(TextUtils.colorize(split[1]));
                    continue;
                case "priority":
                    pc.setPriority(Integer.parseInt(split[1]));
            }


        }

        return pc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "PermissionChenghao{" +
                "name='" + name + '\'' +
                ", priority=" + priority +
                '}';
    }
}
