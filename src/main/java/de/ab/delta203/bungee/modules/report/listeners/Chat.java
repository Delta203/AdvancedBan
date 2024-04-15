package de.ab.delta203.bungee.modules.report.listeners;

import de.ab.delta203.bungee.AdvancedBan;
import de.ab.delta203.bungee.modules.mute.mysql.MuteHandler;
import de.ab.delta203.bungee.modules.report.mysql.ReportHandler;
import java.sql.Connection;
import java.util.HashMap;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Chat extends ReportHandler implements Listener {

  private final MuteHandler muteHandler;

  private final HashMap<ProxiedPlayer, Long> lastMessageTime;
  private final HashMap<ProxiedPlayer, String> lastMessageString;
  private final HashMap<ProxiedPlayer, String> last2MessageString;
  private final int interval;

  public Chat(Connection connection) {
    super(connection);
    muteHandler = new MuteHandler(AdvancedBan.mysql.connection);
    lastMessageTime = new HashMap<>();
    lastMessageString = new HashMap<>();
    last2MessageString = new HashMap<>();
    interval = AdvancedBan.config.getInt("chatlog.autoreport.spam.interval");
  }

  @EventHandler
  public void onChat(ChatEvent e) {
    if (e.getSender() instanceof ProxiedPlayer p) {
      String message = e.getMessage();
      if (p.hasPermission("ab.cantbereported")) return;
      if (!AdvancedBan.config.getBoolean("chatlog.autoreport.enabled")) return;
      if (muteHandler.isMuted(p.getUniqueId().toString())) return;
      if (alreadyReported(p, AdvancedBan.config.getString("console"))) return;
      // domains
      for (String domain : AdvancedBan.chatFilter.getStringList("domains")) {
        String pattern = ".*[^\\s]\\." + domain + ".*";
        if (Pattern.matches(pattern, message)) {
          report(p.getName());
          return;
        }
      }
      // insults
      for (String word : AdvancedBan.chatFilter.getStringList("insults")) {
        if (message.contains(word)) {
          report(p.getName());
          return;
        }
      }
      // spamming
      if (!AdvancedBan.config.getBoolean("chatlog.autoreport.spam.enabled")) return;
      if (isSpamming(p, message)) {
        report(p.getName());
      }
    }
  }

  private void report(String name) {
    String reason = AdvancedBan.config.getString("chatlog.autoreport.reason");
    ProxyServer.getInstance()
        .getPluginManager()
        .dispatchCommand(ProxyServer.getInstance().getConsole(), "report " + name + " " + reason);
    ProxyServer.getInstance()
        .getPluginManager()
        .dispatchCommand(ProxyServer.getInstance().getConsole(), "report $confirm");
  }

  private boolean isSpamming(ProxiedPlayer p, String message) {
    long millis = System.currentTimeMillis();
    if (lastMessageTime.containsKey(p)) {
      long lastMillis = lastMessageTime.get(p);
      if (millis - lastMillis < interval) {
        String lastMessage = lastMessageString.get(p);
        String last2Message = last2MessageString.get(p);
        if (lastMessage != null && last2Message != null) {
          if (lastMessage.equals(last2Message) && lastMessage.equals(message)) {
            return true;
          }
        }
      }
    }
    lastMessageTime.put(p, millis);
    last2MessageString.put(p, lastMessageString.get(p));
    lastMessageString.put(p, message);
    return false;
  }
}
