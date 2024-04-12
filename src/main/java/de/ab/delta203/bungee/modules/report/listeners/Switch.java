package de.ab.delta203.bungee.modules.report.listeners;

import de.ab.delta203.bungee.AdvancedBan;
import de.ab.delta203.bungee.modules.report.mysql.ReportHandler;
import de.ab.delta203.bungee.mysql.PlayerInfoHandler;
import java.sql.Connection;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Switch extends ReportHandler implements Listener {

  private final PlayerInfoHandler playerInfoHandler;

  public Switch(Connection connection) {
    super(connection);
    playerInfoHandler = new PlayerInfoHandler(AdvancedBan.mysql.connection);
  }

  @EventHandler
  public void onSwitch(ServerSwitchEvent e) {
    ProxiedPlayer p = e.getPlayer();
    if (p.hasPermission("ab.panel")) {
      if (playerInfoHandler.hasNotify(p, PlayerInfoHandler.Notification.REPORT)) {
        int reports = getReports();
        if (reports > 0) {
          p.sendMessage(
              new TextComponent(
                  AdvancedBan.prefix
                      + AdvancedBan.messages
                          .getString("report.notification.open")
                          .replace("%reports%", String.valueOf(reports))));
        }
      }
    }
  }
}
