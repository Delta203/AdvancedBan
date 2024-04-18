package de.ab.delta203.spigot.modules.report.listeners;

import de.ab.delta203.core.AdvancedBan;
import de.ab.delta203.core.modules.report.mysql.ReportHandler;
import de.ab.delta203.core.mysql.PlayerInfoHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Connection;

public class Join extends ReportHandler implements Listener {

  private final PlayerInfoHandler playerInfoHandler;

  public Join(Connection connection) {
    super(connection);
    playerInfoHandler = new PlayerInfoHandler(AdvancedBan.mysql.connection);
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent e) {
    Player p = e.getPlayer();
    if (p.hasPermission("ab.panel")) {
      if (playerInfoHandler.hasNotify(
          p.getUniqueId().toString(), PlayerInfoHandler.Notification.REPORT)) {
        int reports = getOpenReports();
        if (reports > 0) {
          p.sendMessage(
              AdvancedBan.prefix
                  + ((String) AdvancedBan.messages.get("report.notification.open"))
                      .replace("%reports%", String.valueOf(reports)));
        }
      }
    }
  }
}
