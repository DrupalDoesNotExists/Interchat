package de.klochk.interchat.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import static de.klochk.interchat.utility.RGB.colorize;

/**
 * Configuration implementation
 */
@RequiredArgsConstructor @Getter
public class ConfigImpl {

    /*
    Configuration start
     */
    private final boolean mediaViewEnable;
    private final @NotNull Component mediaViewHover;

    private final @NotNull String discordToken;
    private final boolean discordEnabled;
    private final @NotNull String discordMinecraft;
    private final @NotNull String discordEndpoint;
    private final long discordChannel;

    private final @NotNull String telegramToken;
    private final boolean telegramEnabled;
    private final @NotNull String telegramMinecraft;
    private final @NotNull String telegramEndpoint;
    private final String telegramChannel;
    /*
    Configuration end
     */

    /**
     * Read configuration from section
     * @param section Section
     * @return Configuration instance
     */
    public static ConfigImpl init(ConfigurationSection section) {

        ConfigurationSection mediaView =
                section.getConfigurationSection("media-view");
        assert mediaView != null;

        boolean mediaViewEnable = mediaView.getBoolean("enable", false);
        Component mediaViewHover = colorize(mediaView.getString("hover"));

        ConfigurationSection discord =
                section.getConfigurationSection("discord");
        assert discord != null;

        String discordToken = discord.getString("token");
        boolean discordEnabled = discord.getBoolean("enabled", false);
        String discordMinecraft = discord.getString("minecraft");
        String discordEndpoint = discord.getString("endpoint");
        long discordChannel = discord.getLong("channel");

        ConfigurationSection telegram =
                section.getConfigurationSection("telegram");
        assert telegram != null;

        String telegramToken = telegram.getString("token");
        boolean telegramEnabled = telegram.getBoolean("enabled", false);
        String telegramMinecraft = telegram.getString("minecraft");
        String telegramEndpoint = telegram.getString("endpoint");
        String telegramChannel = telegram.getString("channel");

        return new ConfigImpl(
                mediaViewEnable, mediaViewHover,
                discordToken, discordEnabled, discordMinecraft, discordEndpoint, discordChannel,
                telegramToken, telegramEnabled, telegramMinecraft, telegramEndpoint, telegramChannel
        );
    }

}
