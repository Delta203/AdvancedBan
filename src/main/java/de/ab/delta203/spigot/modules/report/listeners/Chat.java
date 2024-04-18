package de.ab.delta203.spigot.modules.report.listeners;

import de.ab.delta203.core.AdvancedBan;
import de.ab.delta203.core.modules.mute.mysql.MuteHandler;
import de.ab.delta203.core.modules.report.mysql.ReportHandler;
import de.ab.delta203.spigot.AdvancedBanSpigot;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Chat extends ReportHandler implements Listener {

  private final MuteHandler muteHandler;

  private final HashMap<Player, Long> lastMessageTime;
  private final HashMap<Player, String> lastMessageString;
  private final HashMap<Player, String> last2MessageString;
  private final int interval;

  public Chat(Connection connection) {
    super(connection);
    muteHandler = new MuteHandler(AdvancedBan.mysql.connection);
    lastMessageTime = new HashMap<>();
    lastMessageString = new HashMap<>();
    last2MessageString = new HashMap<>();
    interval = (int) AdvancedBan.config.get("chatlog.autoreport.spam.interval");
  }

  @EventHandler
  public void onChat(AsyncPlayerChatEvent e) {
    Player p = e.getPlayer();
    String message = e.getMessage();
    if (message.startsWith("/")) return;
    if (p.hasPermission("ab.cantbereported")) return;
    if (!(boolean) AdvancedBan.config.get("chatlog.autoreport.enabled")) return;
    if (muteHandler.isMuted(p.getUniqueId().toString())) return;
    if (alreadyReported(p.getUniqueId().toString(), (String) AdvancedBan.config.get("console")))
      return;
    // domains
    for (Object domain : (List<?>) AdvancedBan.chatFilter.get("domains")) {
      String pattern = ".*[^\\s]\\." + domain + ".*";
      if (Pattern.matches(pattern, message)) {
        report(p.getName());
        return;
      }
    }
    // insults
    for (Object word : (List<?>) AdvancedBan.chatFilter.get("insults")) {
      if (message.toLowerCase().contains((String) word)) {
        report(p.getName());
        return;
      }
    }
    // spamming
    if (!(boolean) AdvancedBan.config.get("chatlog.autoreport.spam.enabled")) return;
    if (isSpamming(p, message)) {
      report(p.getName());
    }
  }

  private void report(String name) {
    Bukkit.getScheduler()
        .scheduleSyncDelayedTask(
            AdvancedBanSpigot.plugin,
            () -> {
              String reason = (String) AdvancedBan.config.get("chatlog.autoreport.reason");
              Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "report " + name + " " + reason);
              Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "report $confirm");
            });
  }

  private boolean isSpamming(Player p, String message) {
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
