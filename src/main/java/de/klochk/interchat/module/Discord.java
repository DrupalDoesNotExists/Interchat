package de.klochk.interchat.module;

import lombok.SneakyThrows;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import static de.klochk.interchat.Singleton.INTERCHAT;
import static de.klochk.interchat.utility.RGB.*;

/**
 * Discord module
 */
public class Discord extends ListenerAdapter {

    /*
    Logger
     */
    private static final Logger logger = INTERCHAT.<JavaPlugin>get().getLogger();

    /*
    JDA instance
     */
    private JDA jda;

    /*
    Configuration
     */
    private String minecraft;
    private String endpoint;
    private boolean viewMedia;
    private Component viewMediaHover;
    private TextChannel channel;
    private Guild guild;

    /**
     * Initialize module
     *
     * @param token     Token
     * @param minecraft Minecraft format
     * @param endpoint  Endpoint format
     * @param viewMedia View media?
     */
    @SneakyThrows
    public void init(String token, String minecraft, String endpoint,
                     boolean viewMedia, Component viewMediaHover, long channel) {

        logger.info("Activating discord module");

        this.minecraft = minecraft;
        this.endpoint = endpoint;
        this.viewMedia = viewMedia;
        this.viewMediaHover = viewMediaHover;

        jda = JDABuilder.createDefault(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .setActivity(Activity.playing("Linked"))
                .addEventListeners(this)
                .build();

        jda.awaitReady();
        this.channel = jda.getTextChannelById(channel);
        assert this.channel != null;
        this.guild = this.channel.getGuild();

    }

    /**
     * Send message to endpoint
     *
     * @param message Message
     */
    public void sendMessage(TextComponent message, String username) {

        String msg = message.content();

        String format = endpoint.replace("{name}", username)
                             .replace("{message}", msg);

        this.guild.modifyNickname(this.guild.getSelfMember(), username)
                .queue((queue) -> this.channel.sendMessage(format).queue((event) -> {
                    Member self = guild.getSelfMember();
                    self.modifyNickname(self.getUser().getName()).queue();
                }));

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (event.getChannelType().isGuild()) {
            Member member = event.getMember();

            if (member == null) return;
            if (member.getUser().equals(jda.getSelfUser())) return;

            Message message = event.getMessage();
            Role role = null;

            if (member.getRoles().size() > 0) {
                role = member.getRoles().get(0);
            }

            String roleName = (role != null) ? role.getName() : "";
            TextColor roleColor = (role != null && role.getColor() != null)
                    ? asTextColor(role.getColor())
                    : null;

            AtomicReference<Component> component = new AtomicReference<>(
                    colorize(minecraft, TagResolver.builder()
                            .tag("name", Tag.selfClosingInserting(
                                    Component.text(member.getEffectiveName())
                            ))
                            .tag("role", Tag.selfClosingInserting(
                                    Component.text(roleName, roleColor)
                            ))
                            .tag("message", Tag.selfClosingInserting(
                                    Component.text(message.getContentDisplay())
                            ))
                            .build())
            );

            List<Message.Attachment> attachmentList = message.getAttachments();
            if (viewMedia && attachmentList.size() > 0) {

                attachmentList.forEach(attachment -> {
                    String type = " <Media>";
                    if (attachment.isImage()) type = " <Image>";
                    if (attachment.isVideo()) type = " <Video>";

                    String url = attachment.getUrl();
                    Component attachmentComponent =
                            Component.text(type).hoverEvent(viewMediaHover)
                                .clickEvent(ClickEvent.openUrl(url))
                                .color(NamedTextColor.AQUA);

                    component.set(component.get().append(attachmentComponent));
                });

            }

            Bukkit.broadcast(component.get());
        }

    }

    /**
     * Close all connections
     */
    public void disable() {
        logger.info("Disabling discord module");
        jda.shutdown();
    }

}
