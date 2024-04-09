package de.ab.delta203.bungee.modules.ban.listeners;

import de.ab.delta203.bungee.modules.ban.mysql.BanHandler;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.sql.Connection;

public class Login extends BanHandler implements Listener {

  public Login(Connection connection) {
    super(connection);
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onLogin(LoginEvent e) {
    // TODO: Event
  }
}
