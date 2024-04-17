package de.ab.delta203.bungee.modules.chatlog.listeners;

import de.ab.delta203.core.modules.chatlog.mysql.LogHandler;
import de.ab.delta203.core.AdvancedBan;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.Connection;

public class Switch extends LogHandler implements Listener {

  public Switch(Connection connection) {
    super(connection);
  }

  @EventHandler
  public void onSwitch(ServerSwitchEvent e) {
    ProxiedPlayer p = e.getPlayer();
    if (e.getFrom() != null) {
      String disconnect =
          ((String) AdvancedBan.config.get("chatlog.disconnect")).replace("%player%", p.getName());
      record(p.getUniqueId().toString(), p.getServer().getInfo().getName(), disconnect);
    }
    String connect =
        ((String) AdvancedBan.config.get("chatlog.connect")).replace("%player%", p.getName());
    record(p.getUniqueId().toString(), p.getServer().getInfo().getName(), connect);
  }
}
