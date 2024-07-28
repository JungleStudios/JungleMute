package org.hydr4.junglemute;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteCommand implements CommandExecutor {

    private final JungleMute plugin;

    public MuteCommand(JungleMute plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /mute <player> <motivation> <time> <-s/-p>");
            return true;
        }

        String playerName = args[0];
        String motive = args[1];
        String time = args[2];
        boolean silent = args.length > 3 && args[3].equalsIgnoreCase("-s");

        Player targetPlayer = Bukkit.getPlayer(playerName);
        if (targetPlayer == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }

        long durationMillis = plugin.parseDuration(time);
        plugin.mutePlayer(playerName, durationMillis);

        String notification = plugin.getConfig().getString("messages.mute_notification")
                .replace("%player%", playerName)
                .replace("%time%", plugin.formatDuration(durationMillis))
                .replace("%motivation%", motive);

        if (silent) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', notification));
        } else {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', notification));
        }

        return true;
    }
}
