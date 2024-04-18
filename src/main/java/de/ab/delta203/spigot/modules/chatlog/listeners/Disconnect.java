package de.ab.delta203.spigot.modules.chatlog.listeners;

import de.ab.delta203.core.AdvancedBan;
import de.ab.delta203.core.modules.chatlog.mysql.LogHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.Connection;

public class Disconnect extends LogHandler implements Listener {

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
    String disconnect =
        ((String) AdvancedBan.config.get("chatlog.disconnect")).replace("%player%", p.getName());
    record(p.getUniqueId().toString(), p.getWorld().getName(), disconnect);
  }
}
