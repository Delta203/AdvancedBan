package de.ab.delta203.bungee.modules.chatlog.listeners;

import de.ab.delta203.bungee.AdvancedBan;
import de.ab.delta203.bungee.modules.chatlog.mysql.LogHandler;
import java.sql.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Chat extends LogHandler implements Listener {

  public Chat(Connection connection) {
    super(connection);
  }

  @EventHandler
  public void onChat(ChatEvent e) {
    if (e.getSender() instanceof ProxiedPlayer p) {
      String message = e.getMessage().replace("<", "&#60;").replace(">", "&#62");
      if (message.startsWith("/")) {
        message = AdvancedBan.config.getString("chatlog.command").replace("%command%", message);
        record(p, message);
      } else {
        if (!isMuted(p.getUniqueId().toString())) record(p, message);
      }
    }
  }
}
