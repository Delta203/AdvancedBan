package de.ab.delta203.bungee.modules.mute.listeners;

import de.ab.delta203.core.AdvancedBan;
import de.ab.delta203.core.modules.mute.mysql.MuteHandler;
import java.sql.Connection;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class Chat extends MuteHandler implements Listener {

  public Chat(Connection connection) {
    super(connection);
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onChat(ChatEvent e) {
    if (e.getSender() instanceof ProxiedPlayer p) {
      if (e.getMessage().startsWith("/")) return;
      String uuid = p.getUniqueId().toString();
      if (isMuted(uuid)) {
        long current = System.currentTimeMillis();
        long end = getEnd(uuid);
        if (end == -1 || current < end) {
          e.setCancelled(true);
          p.sendMessage(
              new TextComponent(
                  ((String) AdvancedBan.messages.get("mute.message"))
                      .replace("%reason%", getReason(uuid))
                      .replace("%duration%", getDuration(uuid))
                      .replace("\\n", "\n")));
          return;
        }
        unmute(uuid);
        log(uuid, "-", "unmute", "-", "-");
      }
    }
  }
}
