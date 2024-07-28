package org.hydr4.junglemute;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnmuteCommand implements CommandExecutor {

    private final JungleMute plugin;

    public UnmuteCommand(JungleMute plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /unmute <player>");
            return true;
        }

        String playerName = args[0];
        Player targetPlayer = plugin.getServer().getPlayer(playerName);
        if (targetPlayer == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }

        if (!plugin.isPlayerMuted(playerName)) {
            sender.sendMessage(ChatColor.RED + "Player is not muted.");
            return true;
        }

        plugin.unmutePlayer(playerName);
        sender.sendMessage(ChatColor.GREEN + "Player " + playerName + " has been unmuted.");

        return true;
    }
}
