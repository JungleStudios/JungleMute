package org.hydr4.junglemute;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.hydr4.junglemute.utils.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class JungleMute extends JavaPlugin implements Listener {

    private Map<String, Long> mutedPlayers;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        printASCII();
        saveDefaultConfig();
        config = getConfig();
        mutedPlayers = new HashMap<>();
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("jm").setExecutor(this);
        getCommand("mute").setExecutor(new MuteCommand(this));
    }


private void printASCII() {
    getLogger().info("");
    logWithColor(" &d                __        ___            ___  ___ ");
    logWithColor(" &d   | |  | |\\ | / _` |    |__   |\\/| |  |  |  |__  ");
    logWithColor(" &d\\__/ \\__/ | \\| \\__> |___ |___  |  | \\__/  |  |___ ");
    logWithColor(" &d                                                   ");
    logWithColor("  &8Made by: &9Hydr4                                &8Version: &9" + this.getDescription().getVersion());
    getLogger().info("");
}

public void logWithColor(String s) {
    getServer().getConsoleSender().sendMessage("[" + this.getDescription().getName() + "] " + Text.color(s));
}

    @Override
    public void onDisable() {
        mutedPlayers.clear();
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (isPlayerMuted(player.getName())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.player_muted")));
            event.setCancelled(true);
            return;
        }

        for (String badword : config.getStringList("badwords")) {
            if (Pattern.compile(Pattern.quote(badword), Pattern.CASE_INSENSITIVE).matcher(message).find()) {
                event.setMessage(message.replaceAll("(?i)" + Pattern.quote(badword), "****"));
                notifyStaff(player, message);
                break;
            }
        }
    }

    private void notifyStaff(Player player, String message) {
        String alertMessage = config.getString("messages.bad_word_alert")
                .replace("%player%", player.getName())
                .replace("%message%", message);
        String muteMessage = config.getString("messages.mute_message")
                .replace("%player%", player.getName());
        String clickableMuteCommand = "/mute " + player.getName() + " bad language 3h -s";

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.hasPermission("junglemute.staff")) {
                onlinePlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', alertMessage));
                onlinePlayer.spigot().sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', muteMessage))
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, clickableMuteCommand))
                        .create());
            }
        }
        getLogger().info(alertMessage);
    }

    public boolean isPlayerMuted(String playerName) {
        return mutedPlayers.containsKey(playerName) && mutedPlayers.get(playerName) > System.currentTimeMillis();
    }

    public void mutePlayer(String playerName, long durationMillis) {
        mutedPlayers.put(playerName, System.currentTimeMillis() + durationMillis);
    }

    public long parseDuration(String duration) {
        long timeMillis = 0;
        String[] units = {"mon", "w", "d", "h", "min", "s"};
        long[] unitMillis = {2592000000L, 604800000L, 86400000L, 3600000L, 60000L, 1000L};

        for (int i = 0; i < units.length; i++) {
            String unit = units[i];
            int index = duration.indexOf(unit);
            if (index != -1) {
                String value = duration.substring(0, index);
                timeMillis += Long.parseLong(value) * unitMillis[i];
                duration = duration.substring(index + unit.length());
            }
        }
        return timeMillis;
    }

    public String formatDuration(long durationMillis) {
        long seconds = (durationMillis / 1000) % 60;
        long minutes = (durationMillis / 60000) % 60;
        long hours = (durationMillis / 3600000) % 24;
        long days = (durationMillis / 86400000) % 7;
        long weeks = (durationMillis / 604800000) % 4;
        long months = (durationMillis / 2592000000L);

        return String.format("%dmon %dw %dd %dh %dmin %ds", months, weeks, days, hours, minutes, seconds);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("jm")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.GREEN + "JungleMute v1.0 by Hydr4");
                return true;
            }
            switch (args[0].toLowerCase()) {
                case "reload":
                    if (sender.hasPermission("junglemute.staff")) {
                        reloadConfig();
                        config = getConfig();
                        sender.sendMessage(ChatColor.GREEN + "Configuration reloaded.");
                    }
                    return true;
                case "info":
                    sender.sendMessage(ChatColor.GREEN + "JungleMute v1.0 by Hydr4");
                    return true;
                case "help":
                    sender.sendMessage(ChatColor.GREEN + "/jm - Show plugin info");
                    sender.sendMessage(ChatColor.GREEN + "/jm reload - Reload configuration");
                    sender.sendMessage(ChatColor.GREEN + "/jm info - Show plugin info");
                    sender.sendMessage(ChatColor.GREEN + "/jm help - Show this help message");
                    sender.sendMessage(ChatColor.GREEN + "/mute <player> <motivation> <time> <-s/-p> - Mute a player");
                    return true;
                case "m":
                case "menu":
                    showMuteMenu(sender);
                    return true;
                default:
                    sender.sendMessage(ChatColor.RED + "Unknown command. Use /jm help for help.");
                    return true;
            }
        }
        return false;
    }

    private void showMuteMenu(CommandSender sender) {
        if (mutedPlayers.isEmpty()) {
            sender.sendMessage(ChatColor.YELLOW + "No players are currently muted.");
            return;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.mute_menu_header")));
        for (Map.Entry<String, Long> entry : mutedPlayers.entrySet()) {
            String player = entry.getKey();
            long remainingTime = entry.getValue() - System.currentTimeMillis();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.mute_menu_entry")
                    .replace("%player%", player)
                    .replace("%time%", formatDuration(remainingTime))));
        }
    }
}
