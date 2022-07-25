package de.klochk.interchat.module;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.SendMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Logger;

import static de.klochk.interchat.Singleton.INTERCHAT;
import static de.klochk.interchat.utility.RGB.colorize;

/**
 * Telegram module
 */
public class Telegram implements UpdatesListener {

    /*
    Logger
    */
    private static final Logger logger = INTERCHAT.<JavaPlugin>get().getLogger();

    /*
    Instance
     */
    private TelegramBot bot;

    /*
    Configuration
     */
    private String minecraft;
    private String endpoint;
    private boolean viewMedia;
    private Component viewMediaHover;
    private String channel;

    /**
     * Initialize module
     *
     * @param token     Token
     * @param minecraft Minecraft format
     * @param endpoint  Endpoint format
     * @param viewMedia View media?
     */
    public void init(String token, String minecraft, String endpoint, boolean viewMedia, Component viewMediaHover, String channel) {
        this.minecraft = minecraft;
        this.endpoint = endpoint;
        this.viewMedia = viewMedia;
        this.viewMediaHover = viewMediaHover;
        this.channel = channel;

        logger.info("Activating telegram module");
        this.bot = new TelegramBot(token);

        bot.setUpdatesListener(this);

    }

    /**
     * Send message to endpoint
     *
     * @param message  Message
     * @param username Username
     */
    public void sendMessage(TextComponent message, String username) {

        String text = endpoint.replace("{name}", username)
                .replace("{message}", message.content());

        SendMessage action = new SendMessage(channel, text);
        bot.execute(action);

    }

    /**
     * Close all connections
     */
    public void disable() {

        logger.info("Disabling telegram module");
        bot.shutdown();

    }

    /**
     * Callback handler with available updates
     *
     * @param updates available updates
     * @return id of the last processed update which should not be re-delivered
     * There are 2 convenient values:
     * @see #CONFIRMED_UPDATES_ALL
     * @see #CONFIRMED_UPDATES_NONE
     */
    @Override
    public int process(List<Update> updates) {

        updates.forEach(update -> {
            Message message = update.message();
            if (message == null) return;
            User user = message.from();

            String broadcast = minecraft;

            String userName = (user.firstName() != null) ? user.firstName() : "";
            broadcast = broadcast.replace("{first_name}", userName);

            String userLastName = (user.lastName() != null) ? user.lastName() : "";
            broadcast = broadcast.replace("{last_name}", userLastName);

            broadcast = broadcast.replace("{name}", user.username());

            String messageText = (message.text() != null) ? message.text() : "";
            broadcast = broadcast.replace("{message}", messageText);

            Component component = colorize(broadcast);
            if (message.sticker() != null && viewMedia) {
                component = component.append(Component.text(
                        " <Sticker>", NamedTextColor.AQUA
                ));
            }

            if (message.photo() != null && viewMedia) {
                component = component.append(Component.text(
                        " <Photo>", NamedTextColor.AQUA
                ));
            }

            Bukkit.broadcast(component);
        });

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
