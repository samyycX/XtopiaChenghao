package github.samyycx.xtopiachenghao;

import github.samyycx.xtopiachenghao.chenghao.ChenghaoCache;
import github.samyycx.xtopiachenghao.chenghao.ChenghaoRunnable;
import github.samyycx.xtopiachenghao.command.ChCommandExecutor;
import github.samyycx.xtopiachenghao.listener.ChenghaoListener;
import github.samyycx.xtopiachenghao.logger.ChenghaoLogger;
import github.samyycx.xtopiachenghao.hook.ChenghaoExpansion;
import github.samyycx.xtopiachenghao.utils.YamlUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class XtopiaChenghao extends JavaPlugin {

    public static XtopiaChenghao INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        getServer().getPluginCommand("chenghao").setExecutor(new ChCommandExecutor());
        getServer().getPluginManager().registerEvents(new ChenghaoListener(), this);

        saveDefaultConfig();
        reload();

        Bukkit.getScheduler().runTaskTimer(this, new ChenghaoRunnable(), 0L, YamlUtils.getRefreshInterval()*20L);

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new ChenghaoExpansion().register();
            ChenghaoLogger.success(getServer().getConsoleSender(), "已成功链接PlaceholderAPI");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void reload() {
        ChenghaoLogger.init();
        ChenghaoCache.PERMISSION_CACHE.clear();
        YamlUtils.reloadDefault();
        YamlUtils.reloadPermData();
        YamlUtils.reloadPapiData();
    }
}
