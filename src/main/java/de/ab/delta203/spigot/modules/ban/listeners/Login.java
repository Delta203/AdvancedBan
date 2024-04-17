package de.ab.delta203.spigot.modules.ban.listeners;

import de.ab.delta203.core.AdvancedBan;
import de.ab.delta203.core.modules.ban.mysql.BanHandler;
import de.ab.delta203.core.mysql.PlayerInfoHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.sql.Connection;
import java.util.Objects;

public class Login extends BanHandler implements Listener {

  private final PlayerInfoHandler playerInfoHandler;

  public Login(Connection connection) {
    super(connection);
    playerInfoHandler = new PlayerInfoHandler(AdvancedBan.mysql.connection);
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onLogin(PlayerLoginEvent e) {
    String uuid = e.getPlayer().getUniqueId().toString();
    String ip = e.getAddress().toString().split(":")[0];
    if (isBanned(uuid)) {
      long current = System.currentTimeMillis();
      long end = getEnd(uuid);
      if (end == -1 || current < end) {
        String reason = getReason(uuid);
        String duration = getDuration(uuid);
        playerInfoHandler.removeLoginKey(uuid);
        e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        e.setKickMessage(
            ((String) AdvancedBan.messages.get("ban.message.login"))
                .replace("%reason%", reason)
                .replace("%duration%", duration)
                .replace("\\n", "\n"));
        return;
      }
      unban(uuid);
      log(uuid, "-", "unban", "-", "-");
    }
    if (isBannedIp(ip)) {
      playerInfoHandler.removeLoginKey(uuid);
      e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
      e.setKickMessage(
          ((String) AdvancedBan.messages.get("ban.message.ipban")).replace("\\n", "\n"));
    }
  }
}
