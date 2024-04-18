package de.ab.delta203.spigot.modules.chatlog.listeners;

import de.ab.delta203.core.AdvancedBan;
import de.ab.delta203.core.modules.chatlog.mysql.LogHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.sql.Connection;

public class Chat extends LogHandler implements Listener {

  public Chat(Connection connection) {
    super(connection);
  }

  @EventHandler
  public void onChat(AsyncPlayerChatEvent e) {
    Player p = e.getPlayer();
    String message = e.getMessage();
    if (message.startsWith("/")) {
      message = message.replace("<", "&#60;").replace(">", "&#62");
      message = ((String) AdvancedBan.config.get("chatlog.command")).replace("%command%", message);
      record(p.getUniqueId().toString(), p.getWorld().getName(), message);
    } else {
      if (!isMuted(p.getUniqueId().toString())) {
        message = message.replace("<", "&#60;").replace(">", "&#62");
        record(p.getUniqueId().toString(), p.getWorld().getName(), message);
      }
    }
  }
}
