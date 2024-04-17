package de.ab.delta203.spigot.listeners;

import de.ab.delta203.core.mysql.PlayerInfoHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.sql.Connection;

public class Login extends PlayerInfoHandler implements Listener {

  public Login(Connection connection) {
    super(connection);
  }

  @EventHandler
  public void onLogin(PlayerLoginEvent e) {
    Player p = e.getPlayer();
    if (!registered(p.getUniqueId().toString())) {
      register(p.getUniqueId().toString(), p.getName());
    }
    if (!getName(p.getUniqueId().toString()).equals(p.getName())) {
      updateName(p.getUniqueId().toString(), p.getName());
    }
    updateLoginKey(p.getUniqueId().toString(), p.getName());
  }
}
