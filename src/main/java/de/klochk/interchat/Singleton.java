package de.klochk.interchat;

import de.klochk.interchat.module.Telegram;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

/**
 * Singleton implementation
 */
@AllArgsConstructor
public enum Singleton {

    INTERCHAT(JavaPlugin.getPlugin(Interchat.class)),
    CONFIG(null),

    DISCORD(null),
    TELEGRAM(new Telegram()),
    ;

    @Setter
    private @Nullable Object object;
    public <T> T get() { return (T) object; }

}
