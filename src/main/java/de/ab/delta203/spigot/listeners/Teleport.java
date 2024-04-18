package de.ab.delta203.spigot.listeners;

import de.ab.delta203.core.mysql.PlayerInfoHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.sql.Connection;
import java.util.Objects;

public class Teleport extends PlayerInfoHandler implements Listener {

  public Teleport(Connection connection) {
    super(connection);
  }

  @EventHandler
  public void onTeleport(PlayerTeleportEvent e) {
    Player p = e.getPlayer();
    String from = Objects.requireNonNull(e.getFrom().getWorld()).getName();
    String to = Objects.requireNonNull(Objects.requireNonNull(e.getTo()).getWorld()).getName();
    if (from.equals(to)) return;
    updateServer(p.getUniqueId().toString(), to);
  }
}
