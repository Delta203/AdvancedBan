package de.ab.delta203.bungee.modules.query;

import de.ab.delta203.bungee.AdvancedBan;
import de.ab.delta203.bungee.modules.query.mysql.QueryHandler;
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
            new TextComponent(AdvancedBan.prefix + AdvancedBan.messages.getString("loaded.query")));
    ProxyServer.getInstance()
        .getScheduler()
        .schedule(
            AdvancedBan.plugin,
            () -> {
              ArrayList<String> commands = getCommands();
              for (String commandRaw : commands) {
                String[] commandArray = commandRaw.split("\\|");
                String senderUUID = commandArray[0];
                String command = commandArray[1];
                CommandSender commandSender;
                if (senderUUID.equals(AdvancedBan.config.getString("console"))) {
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
            AdvancedBan.config.getInt("query"),
            TimeUnit.SECONDS);
  }

  public void registerModule() {
    init();
  }
}
