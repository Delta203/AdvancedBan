package de.ab.delta203.bungee.modules.chatlog.listeners;

import de.ab.delta203.bungee.AdvancedBan;
import de.ab.delta203.bungee.modules.chatlog.mysql.LogHandler;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.Connection;

public class Disconnect extends LogHandler implements Listener {

  public Disconnect(Connection connection) {
    super(connection);
  }

  @EventHandler
  public void onDisconnect(PlayerDisconnectEvent e) {
    ProxiedPlayer p = e.getPlayer();
    String disconnect =
        AdvancedBan.config.getString("chatlog.disconnect").replace("%player%", p.getName());
    record(p, disconnect);
  }
}
