package de.ab.delta203.spigot.listeners;

import de.ab.delta203.core.mysql.PlayerInfoHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.Connection;

public class Disconnect extends PlayerInfoHandler implements Listener {

  public Disconnect(Connection connection) {
    super(connection);
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent e) {
    act(e.getPlayer());
  }

  @EventHandler
  public void onKick(PlayerKickEvent e) {
    act(e.getPlayer());
  }

  private void act(Player p) {
    removeServer(p.getUniqueId().toString());
    removeLoginKey(p.getUniqueId().toString());
  }
}
