package github.samyycx.xtopiachenghao;

import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class YamlUtils {

    public static String getDefaultChenghao() {
        return A.a(XtopiaChenghao.INSTANCE.getConfig().getString("DefaultChenghao"));
    }

    public static long getRefreshInterval() {
        return XtopiaChenghao.INSTANCE.getConfig().getLong("Interval");
    }

    public static String getStorageTitle() {
        return A.a(XtopiaChenghao.INSTANCE.getConfig().getString("StorageTitle"));
    }

    public static YamlConfiguration getPlayerData(UUID uuid) {
        File folder = new File(XtopiaChenghao.INSTANCE.getDataFolder(), "playerdata");
        boolean is_new = false;
        if (!folder.exists()) {
            folder.mkdir();
        }
        File file = new File(XtopiaChenghao.INSTANCE.getDataFolder(), "playerdata/" + uuid + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
                is_new = true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (is_new) {
            config = setDefaultForNew(file);
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public static void save(YamlConfiguration config, UUID uuid) {
        File file = new File(XtopiaChenghao.INSTANCE.getDataFolder(), "playerdata/"+uuid+".yml");
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<UUID> getAllUUID() {
        File dataFolder = new File(XtopiaChenghao.INSTANCE.getDataFolder(), "playerdata");
        if (dataFolder.listFiles() == null) {
            return new ArrayList<>();
        }
        return Arrays.stream(dataFolder.listFiles()).map(file -> UUID.fromString(file.getName().replace(".yml", ""))).collect(Collectors.toList());
    }

    public static YamlConfiguration setDefaultForNew(File file) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.set("NowUsing", 0);
        List<Map<?, ?>> chenghaos = config.getMapList("AllChenghaos");
        Chenghao chenghao = new Chenghao(getDefaultChenghao(), 0, -1, true);

        chenghaos.add(chenghao.toMap());

        config.set("AllChenghaos", chenghaos);
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return config;
    }

    public static void reloadDefault() {
        String defaultChenghao = getDefaultChenghao();

        if (!defaultChenghao.equalsIgnoreCase("")) {

            Chenghao chenghao = new Chenghao(defaultChenghao, 0, -1, true);

            File dataFolder = new File(XtopiaChenghao.INSTANCE.getDataFolder(), "playerdata");
            for (File file : Objects.requireNonNull(dataFolder.listFiles())) {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

                List<Map<?, ?>> chenghaos = config.getMapList("AllChenghaos");
                if (chenghaos.size() == 0) {
                    chenghaos.add(chenghao.toMap());
                    config.set("NowUsing", 0);
                } else {
                    if (Boolean.parseBoolean((String)chenghaos.get(0).get("isDefault"))) {
                        chenghaos.set(0, chenghao.toMap());
                    } else {
                        chenghaos.add(0, chenghao.toMap());
                    }
                }

                config.set("AllChenghaos", chenghaos);
                try {
                    config.save(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            File dataFolder = new File(XtopiaChenghao.INSTANCE.getDataFolder(), "playerdata");
            if (dataFolder.listFiles() == null) {
                return;
            }
            for (File file : dataFolder.listFiles()) {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

                List<Map<?, ?>> chenghaos = config.getMapList("AllChenghaos");
                if (chenghaos.size() != 0) {
                    if (Boolean.parseBoolean((String)chenghaos.get(0).get("isDefault"))) {
                        chenghaos.remove(0);
                    }
                }

                if (config.getInt("NowUsing") == 0) {
                    config.set("NowUsing", -1);
                }

                try {
                    config.save(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static YamlConfiguration getPermData() {
        File file = new File(XtopiaChenghao.INSTANCE.getDataFolder(), "permdata.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return YamlConfiguration.loadConfiguration(file);
    }

    public static void reloadPermData() {
        File file = new File(XtopiaChenghao.INSTANCE.getDataFolder(), "permdata.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        ChenghaoCache.PERMISSION_CACHE.clear();
        for (String permission : config.getKeys(true)) {
            if (config.get(permission) instanceof MemorySection) {
                continue;
            }
            ChenghaoCache.PERMISSION_CACHE.put(permission, A.a(config.getString(permission)));
        }
    }

    public static void savePermData(YamlConfiguration config) {
        File file = new File(XtopiaChenghao.INSTANCE.getDataFolder(), "permdata.yml");
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
