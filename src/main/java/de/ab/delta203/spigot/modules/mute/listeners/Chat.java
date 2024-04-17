package de.ab.delta203.spigot.modules.mute.listeners;

import de.ab.delta203.core.AdvancedBan;
import de.ab.delta203.core.modules.mute.mysql.MuteHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.sql.Connection;

public class Chat extends MuteHandler implements Listener {

  public Chat(Connection connection) {
    super(connection);
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onChat(AsyncPlayerChatEvent e) {
    Player p = e.getPlayer();
    if (e.getMessage().startsWith("/")) return;
    String uuid = p.getUniqueId().toString();
    if (isMuted(uuid)) {
      long current = System.currentTimeMillis();
      long end = getEnd(uuid);
      if (end == -1 || current < end) {
        e.setCancelled(true);
        p.sendMessage(
            ((String) AdvancedBan.messages.get("mute.message"))
                .replace("%reason%", getReason(uuid))
                .replace("%duration%", getDuration(uuid))
                .replace("\\n", "\n"));
        return;
      }
      unmute(uuid);
      log(uuid, "-", "unmute", "-", "-");
    }
  }
}
