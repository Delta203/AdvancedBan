package de.ab.delta203.bungee.listeners;

import de.ab.delta203.bungee.mysql.PlayerInfoHandler;
import java.sql.Connection;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Login extends PlayerInfoHandler implements Listener {

  public Login(Connection connection) {
    super(connection);
  }

  @EventHandler
  public void onLogin(LoginEvent e) {
    PendingConnection pc = e.getConnection();
    if (!registered(pc)) {
      register(pc);
    }
    if (!getName(pc.getUniqueId().toString()).equals(pc.getName())) {
      updateName(pc.getUniqueId().toString(), pc.getName());
    }
    updateLoginKey(pc);
  }
}
