package de.ab.delta203.spigot.listeners;

import de.ab.delta203.core.mysql.PlayerInfoHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Connection;

public class Join extends PlayerInfoHandler implements Listener {

  public Join(Connection connection) {
    super(connection);
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent e) {
    Player p = e.getPlayer();
    updateServer(p.getUniqueId().toString(), p.getWorld().getName());
  }
}
