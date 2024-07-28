package org.hydr4.junglemute.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Text {

    private static final String[] TIME_UNITS = {"s", "m", "h", "h", "h", "h", "h", "h", "h"};
    private static final String[] MONEY_UNITS = {"", "k", "m", "b", "t", "q", "qi", "s", "sep", "OC", "N", "DEC", "UN", "DUO", "TRE"};
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");
    private static final DecimalFormat LONG_FORMATTER = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);

    static {
        LONG_FORMATTER.applyPattern("#,###,###.##");
    }

    public static String color(String text) {
        return (text == null || text.isEmpty()) ? "" : ChatColor.translateAlternateColorCodes('&', text);
    }

    public static List<String> color(List<String> list) {
        List<String> coloredList = new ArrayList<>(list.size());
        list.forEach(o -> coloredList.add(Text.color(o)));
        return coloredList;
    }

    public static String formatMoney(double value) {
        int index = 0;
        while (value >= 1000.0) {
            value /= 1000.0;
            index++;
        }
        return DECIMAL_FORMAT.format(value) + MONEY_UNITS[index];
    }

    public static String formatTime(int value) {
        int index = 0;
        while (value >= 1000) {
            value /= 1000;
            index++;
        }
        return DECIMAL_FORMAT.format(value) + TIME_UNITS[index];
    }

    public static void send(CommandSender commandSender, List<String> messages) {
        messages.forEach(message -> commandSender.sendMessage(color(message)));
    }

    public static void send(Player player, String msg) {
        player.sendMessage(color(msg));
    }

    public static void send(CommandSender commandSender, String msg) {
        commandSender.sendMessage(color(msg));
    }
}