package github.nrocme.events;

import github.nrocme.playerjoinleave.PlayerJoinLeave;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnLeave implements Listener
{
    private PlayerJoinLeave plugin;

    // CONSTRUCTOR
    public OnLeave (PlayerJoinLeave plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    void onPlayerLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        String name = p.getName();
        if (plugin.getConfig().getBoolean("messages.leave-message.isOn")) {
            e.setQuitMessage(ChatColor.translateAlternateColorCodes(
                    '&', this.plugin.getConfig().getString("messages.leave-message.message").replace("%player%", name))
            );
        }
    }
}