package de.ab.delta203.bungee.modules.report.listeners;

import de.ab.delta203.bungee.AdvancedBan;
import de.ab.delta203.bungee.modules.report.mysql.ReportHandler;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.Connection;

public class Switch extends ReportHandler implements Listener {

  public Switch(Connection connection) {
    super(connection);
  }

  @EventHandler
  public void onSwitch(ServerSwitchEvent e) {
    ProxiedPlayer p = e.getPlayer();
    if (p.hasPermission("ab.panel")) {
      int reports = getReports();
      if (reports > 0) {
        if (AdvancedBan.config.getBoolean("notify.report")) {
          p.sendMessage(
              new TextComponent(
                  AdvancedBan.prefix
                      + AdvancedBan.messages
                          .getString("report.notification")
                          .replace("%reports%", String.valueOf(reports))));
        }
      }
    }
  }
}
