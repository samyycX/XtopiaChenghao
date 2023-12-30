package github.samyycx.xtopiachenghao.hook;
import github.samyycx.xtopiachenghao.chenghao.ChenghaoManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChenghaoExpansion extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "xchenghao";
    }

    @Override
    public @NotNull String getAuthor() {
        return "samyyc";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("name")) {
            return String.join(" ",ChenghaoManager.renderPlayerChenghao(player.getPlayer()));
        }
        return null;
    }
}
