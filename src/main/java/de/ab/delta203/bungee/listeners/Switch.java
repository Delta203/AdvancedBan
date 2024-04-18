package de.ab.delta203.bungee.listeners;

import de.ab.delta203.core.mysql.PlayerInfoHandler;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.Connection;

public class Switch extends PlayerInfoHandler implements Listener {

  public Switch(Connection connection) {
    super(connection);
  }

  @EventHandler
  public void onSwitch(ServerSwitchEvent e) {
    ProxiedPlayer p = e.getPlayer();
    updateServer(p.getUniqueId().toString(), p.getServer().getInfo().getName());
  }
}
