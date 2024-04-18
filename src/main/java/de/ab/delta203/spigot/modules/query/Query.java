package de.ab.delta203.spigot.modules.query;

import de.ab.delta203.core.AdvancedBan;
import de.ab.delta203.core.modules.query.mysql.QueryHandler;
import de.ab.delta203.spigot.AdvancedBanSpigot;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Query extends QueryHandler {

  public Query(Connection connection) {
    super(connection);
  }

  private void init() {
    Bukkit.getConsoleSender()
        .sendMessage(AdvancedBan.prefix + AdvancedBan.messages.get("loaded.query"));
    Bukkit.getScheduler()
        .scheduleSyncRepeatingTask(
            AdvancedBanSpigot.plugin,
            () -> {
              ArrayList<String> commands = getCommands();
              for (String commandRaw : commands) {
                String[] commandArray = commandRaw.split("\\|");
                String senderUUID = commandArray[0];
                String command = commandArray[1];
                CommandSender commandSender;
                if (senderUUID.equals(AdvancedBan.config.get("console"))) {
                  commandSender = Bukkit.getConsoleSender();
                } else {
                  UUID uuid;
                  try {
                    uuid = UUID.fromString(senderUUID);
                  } catch (IllegalArgumentException e) {
                    continue;
                  }
                  Player p = Bukkit.getPlayer(uuid);
                  if (p == null) continue;
                  commandSender = p;
                }
                Bukkit.dispatchCommand(commandSender, command);
              }
            },
            0,
            (int) AdvancedBan.config.get("query"));
  }

  public void registerModule() {
    init();
  }
}
