package de.ab.delta203.bungee.modules.chatlog.listeners;

import de.ab.delta203.core.AdvancedBan;
import de.ab.delta203.core.modules.chatlog.mysql.LogHandler;
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
      String message = e.getMessage();
      if (message.startsWith("/")) {
        message = message.replace("<", "&#60;").replace(">", "&#62");
        message =
            ((String) AdvancedBan.config.get("chatlog.command")).replace("%command%", message);
        record(p.getUniqueId().toString(), p.getServer().getInfo().getName(), message);
      } else {
        if (!isMuted(p.getUniqueId().toString())) {
          message = message.replace("<", "&#60;").replace(">", "&#62");
          record(p.getUniqueId().toString(), p.getServer().getInfo().getName(), message);
        }
      }
    }
  }
}
