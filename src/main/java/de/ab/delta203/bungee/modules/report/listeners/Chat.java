package de.ab.delta203.bungee.modules.report.listeners;

import de.ab.delta203.bungee.AdvancedBan;
import de.ab.delta203.bungee.modules.report.mysql.ReportHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.Connection;

public class Chat extends ReportHandler implements Listener {

  public Chat(Connection connection) {
    super(connection);
  }

  @EventHandler
  public void onChat(ChatEvent e) {
    if (e.getSender() instanceof ProxiedPlayer p) {
      String message = e.getMessage();
      if (!AdvancedBan.config.getBoolean("chatlog.autoreport.enabled")) return;
      String reason = AdvancedBan.config.getString("chatlog.autoreport.reason");
      for (String word : AdvancedBan.chatFilter.getStringList("blacklist")) {
        if (message.contains(word)) {
          ProxyServer.getInstance()
              .getPluginManager()
              .dispatchCommand(
                  ProxyServer.getInstance().getConsole(), "report " + p.getName() + " " + reason);
          ProxyServer.getInstance()
              .getPluginManager()
              .dispatchCommand(ProxyServer.getInstance().getConsole(), "report $confirm");
          return;
        }
      }
    }
  }
}
