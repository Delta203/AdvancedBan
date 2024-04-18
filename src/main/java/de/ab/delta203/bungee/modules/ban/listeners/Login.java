package de.ab.delta203.bungee.modules.ban.listeners;

import de.ab.delta203.core.AdvancedBan;
import de.ab.delta203.core.modules.ban.mysql.BanHandler;
import de.ab.delta203.core.mysql.PlayerInfoHandler;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.sql.Connection;

public class Login extends BanHandler implements Listener {

  private final PlayerInfoHandler playerInfoHandler;

  public Login(Connection connection) {
    super(connection);
    playerInfoHandler = new PlayerInfoHandler(AdvancedBan.mysql.connection);
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onLogin(LoginEvent e) {
    String uuid = e.getConnection().getUniqueId().toString();
    String ip = e.getConnection().getSocketAddress().toString().split(":")[0];
    if (isBanned(uuid)) {
      long current = System.currentTimeMillis();
      long end = getEnd(uuid);
      if (end == -1 || current < end) {
        String reason = getReason(uuid);
        String duration = getDuration(uuid);
        playerInfoHandler.removeLoginKey(uuid);
        e.setCancelled(true);
        e.setReason(
            new TextComponent(
                ((String) AdvancedBan.messages.get("ban.message.login"))
                    .replace("%reason%", reason)
                    .replace("%duration%", duration)
                    .replace("\\n", "\n")));
        return;
      }
      unban(uuid);
      log(uuid, "-", "unban", "-", "-");
    }
    if (!(boolean) AdvancedBan.config.get("ip_bans")) return;
    if (isBannedIp(ip)) {
      playerInfoHandler.removeLoginKey(uuid);
      e.setCancelled(true);
      e.setReason(
          new TextComponent(
              ((String) AdvancedBan.messages.get("ban.message.ipban")).replace("\\n", "\n")));
    }
  }
}
