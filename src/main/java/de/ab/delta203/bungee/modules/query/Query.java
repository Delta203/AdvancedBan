package de.ab.delta203.bungee.modules.query;

import de.ab.delta203.bungee.AdvancedBanBungee;
import de.ab.delta203.core.AdvancedBan;
import de.ab.delta203.core.modules.query.mysql.QueryHandler;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Query extends QueryHandler {

  public Query(Connection connection) {
    super(connection);
  }

  private void init() {
    ProxyServer.getInstance()
        .getConsole()
        .sendMessage(
            new TextComponent(AdvancedBan.prefix + AdvancedBan.messages.get("loaded.query")));
    ProxyServer.getInstance()
        .getScheduler()
        .schedule(
            AdvancedBanBungee.plugin,
            () -> {
              ArrayList<String> commands = getCommands();
              for (String commandRaw : commands) {
                String[] commandArray = commandRaw.split("\\|");
                String senderUUID = commandArray[0];
                String command = commandArray[1];
                CommandSender commandSender;
                if (senderUUID.equals(AdvancedBan.config.get("console"))) {
                  commandSender = ProxyServer.getInstance().getConsole();
                } else {
                  UUID uuid;
                  try {
                    uuid = UUID.fromString(senderUUID);
                  } catch (IllegalArgumentException e) {
                    continue;
                  }
                  ProxiedPlayer p = ProxyServer.getInstance().getPlayer(uuid);
                  if (p == null) continue;
                  commandSender = p;
                }
                ProxyServer.getInstance()
                    .getPluginManager()
                    .dispatchCommand(commandSender, command);
              }
            },
            0,
            (int) AdvancedBan.config.get("query"),
            TimeUnit.SECONDS);
  }

  public void registerModule() {
    init();
  }
}
