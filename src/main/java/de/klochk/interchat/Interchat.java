package de.klochk.interchat;

import de.klochk.interchat.command.ConfigReloadCommand;
import de.klochk.interchat.config.ConfigImpl;
import de.klochk.interchat.event.DiscordMessageListener;
import de.klochk.interchat.event.TelegramMessageListener;
import de.klochk.interchat.module.Discord;
import de.klochk.interchat.module.Telegram;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import static de.klochk.interchat.Singleton.*;

public final class Interchat extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();

        getLogger().info("Interchat | Integral of sine function?");
        ConfigImpl config = ConfigImpl.init(getConfig());
        CONFIG.setObject(config);

        if (!config.isDiscordEnabled() && !config.isTelegramEnabled()) {
            getLogger().warning("All message endpoints disabled.");
            getLogger().warning("Edit your configuration if it is first installation!");
        }

        if (config.isDiscordEnabled()) {
            Discord discord = new Discord();
            DISCORD.setObject(discord);

            try {
                discord.init(
                        config.getDiscordToken(),
                        config.getDiscordMinecraft(),
                        config.getDiscordEndpoint(),
                        config.isMediaViewEnable(),
                        config.getMediaViewHover(),
                        config.getDiscordChannel()
                );
                Bukkit.getPluginManager().registerEvents(new DiscordMessageListener(), this);
            } catch (Exception exception) {
                getLogger().warning("Discord module caused exception:");
                getLogger().warning(exception.getLocalizedMessage());
            }
        }

        if (config.isTelegramEnabled()) {
            Telegram telegram = new Telegram();
            TELEGRAM.setObject(telegram);

            try {
                telegram.init(
                        config.getTelegramToken(),
                        config.getTelegramMinecraft(),
                        config.getTelegramEndpoint(),
                        config.isMediaViewEnable(),
                        config.getTelegramChannel()
                );
                Bukkit.getPluginManager().registerEvents(new TelegramMessageListener(), this);
            } catch (Exception exception) {
                getLogger().warning("Telegram module caused exception:");
                getLogger().warning(exception.getLocalizedMessage());
            }

        }

        getCommand("interchat").setExecutor(new ConfigReloadCommand());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        DISCORD.<Discord>get().disable();
        TELEGRAM.<Telegram>get().disable();
    }

}
