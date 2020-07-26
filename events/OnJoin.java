package github.nrocme.events;

import github.nrocme.playerjoinleave.PlayerJoinLeave;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.Vector;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import java.io.File;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Sound;
import sun.security.krb5.Config;


public class OnJoin implements Listener
{
    private PlayerJoinLeave plugin;
    public FileConfiguration config;

    // CONSTRUCTOR
    public OnJoin (PlayerJoinLeave plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }


    @EventHandler
    void onPlayerJoin(PlayerJoinEvent e)
    {
        Player p = e.getPlayer();
        String name = p.getName();

        // join message
        if (this.config.getBoolean("messages.join-message.isOn")) {
            e.setJoinMessage(ChatColor.translateAlternateColorCodes(
                    '&', this.config.getString("messages.join-message.message").replace("%player%", name))
            );
        }

        // fireworks on join with delay
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                if (config.getBoolean("firework.isOn")) {
                    buildFirework(p.getLocation(), config);
                }
            }
        }, this.config.getInt("firework.delay"));

        // sound on join with delay
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                if (config.getBoolean("sounds.isOn")) {
                    joinSound(p, config);
                }
            }
        }, this.config.getInt("sounds.delay"));

        // commands on join with delay
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                if (config.getBoolean("commands.isOn")) {
                    executeCommands(p, config);
                }
            }
        }, this.config.getInt("commands.delay"));
    }

    // methods that plays a join sound and looks at the config for this sound
    private void joinSound(Player p, FileConfiguration config)
    {
        float volume = (float)(config.getDouble("sounds.volume"));
        float pitch = (float)(config.getDouble("sounds.pitch"));
        p.playSound(p.getLocation(), Sound.valueOf(
                Objects.requireNonNull(config.getString("sounds.sound")).toUpperCase()), volume, pitch
        );
    }

    // methods that executes a command or commands on the player spawn
    private void executeCommands(Player p, FileConfiguration config)
    {
        // could probably use a repeatable task but this method works. Task are scheduled each x ticks apart from
        // each other
        int count = 1;
        for(String command : config.getStringList("commands.executions")) {

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run()
                {
                    p.performCommand(command.replace("%player%", p.getName()));
                }
            }, config.getInt("commands.delayInBetween") * count++);
        }
    }


    private Firework buildFirework(Location loc, FileConfiguration config)
    {
        Firework firework = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = firework.getFireworkMeta();
        fwm.addEffect(FireworkEffect.builder()
                .withColor(getColorConfig(config.getStringList("firework.colors")))
                .with(getEffectConfig(config))
                .withTrail()
                .withFade(getColorConfig(config.getStringList("firework.fadeColors")))
                .build());
        firework.setFireworkMeta(fwm);

        firework.setVelocity(new Vector(0, 1, 0));
        return firework;
    }


    private FireworkEffect.Type getEffectConfig(FileConfiguration config)
    {
        FireworkEffect.Type[] typeArray =
                {
                        FireworkEffect.Type.STAR, FireworkEffect.Type.BALL_LARGE, FireworkEffect.Type.BALL,
                        FireworkEffect.Type.CREEPER, FireworkEffect.Type.BURST
                };
        switch (config.getString("firework.effect").toLowerCase()) {
            case "random":
                return typeArray[randInt(0,4)];
            case "ball_large":
                return FireworkEffect.Type.BALL_LARGE;
            case "star":
                return FireworkEffect.Type.STAR;
            case "burst":
                return FireworkEffect.Type.BURST;
            case "creeper":
                return FireworkEffect.Type.CREEPER;
            default:
                return FireworkEffect.Type.BALL;
        }
    }


    private Set getColorConfig(List <String> colorList)
    {
        Set<Color> colors = new HashSet<>();
        for(String s :  colorList) {
            switch (s.toLowerCase()) {
                case "black":
                    colors.add(Color.BLACK);
                    break;
                case "yellow":
                    colors.add(Color.YELLOW);
                    break;
                case "aqua":
                    colors.add(Color.AQUA);
                    break;
                case "blue":
                    colors.add(Color.BLUE);
                    break;
                case "gray":
                    colors.add(Color.GRAY);
                    break;
                case "green":
                    colors.add(Color.GREEN);
                    break;
                case "fuchsia":
                    colors.add(Color.FUCHSIA);
                    break;
                case "lime":
                    colors.add(Color.LIME);
                    break;
                case "maroon":
                    colors.add(Color.MAROON);
                    break;
                case "navy":
                    colors.add(Color.NAVY);
                    break;
                case "orange":
                    colors.add(Color.ORANGE);
                    break;
                case "red":
                    colors.add(Color.RED);
                    break;
                case "silver":
                    colors.add(Color.SILVER);
                    break;
                case "teal":
                    colors.add(Color.TEAL);
                    break;
                case "white":
                    colors.add(Color.WHITE);
                    break;
                case "purple":
                    colors.add(Color.PURPLE);
                    break;
                case "olive":
                    colors.add(Color.OLIVE);
                    break;
                default:
                    colors.add(Color.fromRGB(randInt(0, 255), randInt(0, 255), randInt(0, 255)));
            }
        }
        return colors;
    }


    private int randInt(int min, int max)
    {
        // nextInt returns a random number from arg1 to arg2 with arg2 being exclusive
        return ThreadLocalRandom.current().nextInt(min, max+1);
    }

}

