package de.ab.delta203.bungee.listeners;

import de.ab.delta203.bungee.mysql.PlayerInfoHandler;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.Connection;

public class Disconnect extends PlayerInfoHandler implements Listener {

  public Disconnect(Connection connection) {
    super(connection);
  }

  @EventHandler
  public void onDisconnect(PlayerDisconnectEvent e) {
    ProxiedPlayer p = e.getPlayer();
    removeServer(p);
    removeLoginKey(p.getUniqueId().toString());
  }
}
