package de.klochk.interchat.command;

import de.klochk.interchat.Interchat;
import de.klochk.interchat.config.ConfigImpl;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import static de.klochk.interchat.Singleton.CONFIG;
import static de.klochk.interchat.Singleton.INTERCHAT;
import static de.klochk.interchat.utility.RGB.colorize;

/**
 * /interchat command
 */
public class ConfigReloadCommand implements CommandExecutor {

    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        JavaPlugin plugin = INTERCHAT.get();
        String version = plugin.getDescription().getVersion();

        if (!sender.hasPermission("interchat.reload")) {
            sender.sendMessage(colorize(
                    "<aqua>Interchat v<dark_aqua>" + version + "</dark_aqua>"
            ));
            return true;
        }

        plugin.reloadConfig();
        ConfigImpl config = ConfigImpl.init(plugin.getConfig());
        CONFIG.setObject(config);

        sender.sendMessage(colorize("<green>Successfully reloaded configuration!"));

        return true;
    }

}
