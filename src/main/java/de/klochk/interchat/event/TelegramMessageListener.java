package de.klochk.interchat.event;

import de.klochk.interchat.module.Telegram;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static de.klochk.interchat.Singleton.TELEGRAM;

/**
 * Chat listener for telegram
 */
public class TelegramMessageListener implements Listener {

    @EventHandler
    public void onChat(AsyncChatEvent event) {

        TextComponent message = (TextComponent) event.message();
        TELEGRAM.<Telegram>get().sendMessage(message, event.getPlayer().getName());

    }

}
