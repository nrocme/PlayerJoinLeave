package github.nrocme.playerjoinleave.events;

import github.nrocme.playerjoinleave.PlayerJoinLeave;
import org.bukkit.*;
import org.bukkit.util.Vector;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class OnJoin implements Listener
{
    private PlayerJoinLeave plugin;

    // CONSTRUCTOR
    public OnJoin (PlayerJoinLeave plugin) {
        this.plugin = plugin;
    }

    // final indicates that an item cannot be altered and is final
    private static final Color[] AMERICA_COLORS = {Color.RED, Color.WHITE, Color.BLUE};

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent e)
    {
        Player p = e.getPlayer();
        String name = p.getName();
        if (plugin.getConfig().getBoolean("messages.join-message.isOn")) {
            e.setJoinMessage(ChatColor.translateAlternateColorCodes(
                    '&', this.plugin.getConfig().getString("messages.join-message.message").replace("%player%", name))
            );
        }
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                if (plugin.getConfig().getBoolean("firework.isOn")) {
                    buildFirework(p.getLocation());
                }
            }
        }, plugin.getConfig().getInt("firework.delay"));
    }

    private Firework buildFirework(Location loc) {
        Firework firework = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = firework.getFireworkMeta();
        fwm.addEffect(FireworkEffect.builder()
                .withColor(getColorConfig(this.plugin.getConfig().getStringList("firework.colors")))
                .with(getEffectConfig())
                .withTrail()
                .withFade(getColorConfig(this.plugin.getConfig().getStringList("firework.fadeColors")))
                .build());
        firework.setFireworkMeta(fwm);

        firework.setVelocity(new Vector(0, 1, 0));
        return firework;
    }


    private FireworkEffect.Type getEffectConfig()
    {
        FireworkEffect.Type[] typeArray =
                {
                        FireworkEffect.Type.STAR, FireworkEffect.Type.BALL_LARGE, FireworkEffect.Type.BALL,
                        FireworkEffect.Type.CREEPER, FireworkEffect.Type.BURST
                };
        switch (this.plugin.getConfig().getString("firework.effect").toLowerCase()) {
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

