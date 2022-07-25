package de.klochk.interchat.event;

import de.klochk.interchat.module.Discord;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static de.klochk.interchat.Singleton.DISCORD;

/**
 * Event listener for chat that sends discord messages
 */
public class DiscordMessageListener implements Listener {

    @EventHandler
    public void onMessage(AsyncChatEvent event) {

        TextComponent component = (TextComponent) event.message();
        DISCORD.<Discord>get().sendMessage(component, event.getPlayer().getName());

    }

}
