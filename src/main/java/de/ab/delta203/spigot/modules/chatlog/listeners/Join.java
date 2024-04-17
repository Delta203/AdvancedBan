package de.ab.delta203.spigot.modules.chatlog.listeners;

import de.ab.delta203.core.AdvancedBan;
import de.ab.delta203.core.modules.chatlog.mysql.LogHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Connection;

public class Join extends LogHandler implements Listener {

  public Join(Connection connection) {
    super(connection);
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent e) {
    Player p = e.getPlayer();
    String connect =
        ((String) AdvancedBan.config.get("chatlog.connect")).replace("%player%", p.getName());
    record(p.getUniqueId().toString(), p.getWorld().getName(), connect);
  }
}
