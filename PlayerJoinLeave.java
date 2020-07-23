package github.nrocme.playerjoinleave;

import github.nrocme.playerjoinleave.events.OnJoin;
import github.nrocme.playerjoinleave.events.OnLeave;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerJoinLeave extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new OnJoin(this), this);
        getServer().getPluginManager().registerEvents(new OnLeave(this), this);
        // saves the config as the default on for servers which creates a folder containing
        // out config.yml
        this.saveDefaultConfig();

        // message displayed in console on successful boot
        System.out.println("XxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxX");
        System.out.println("X----------------PLAYER JOIN LEAVE V1.0 LOADED-----------------X");
        System.out.println("XxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxX");

    }


    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("PJL")) {
            if(!sender.hasPermission("PJL.reload")) {
                sender.sendMessage(ChatColor.RED + "You cannot run this command");
                return true;
            }
            if (args.length == 0) {
                // /PJL
                sender.sendMessage(ChatColor.RED + "Usage: /PJL help");
                return true;
            }
            if (args.length > 0) {
                // /PJL reload
                if (args[0].equalsIgnoreCase("reload")) {
                    this.reloadConfig();
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("messages.reload-message")));
                }
            }
        }
        return false;
    }
}
