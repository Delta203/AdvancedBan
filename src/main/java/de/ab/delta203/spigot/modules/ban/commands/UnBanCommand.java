package de.ab.delta203.spigot.modules.ban.commands;

import de.ab.delta203.core.AdvancedBan;
import de.ab.delta203.core.modules.ban.mysql.BanHandler;
import de.ab.delta203.core.mysql.PlayerInfoHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnBanCommand implements CommandExecutor {

  private final BanHandler banHandler;
  private final PlayerInfoHandler playerInfoHandler;

  public UnBanCommand() {
    banHandler = new BanHandler(AdvancedBan.mysql.connection);
    playerInfoHandler = new PlayerInfoHandler(AdvancedBan.mysql.connection);
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (!sender.hasPermission("ab.unban")) {
      sender.sendMessage((String) AdvancedBan.messages.get("no_permission"));
      return false;
    }
    if (args.length == 1) {
      String name = args[0];
      String uuid = playerInfoHandler.getUUID(name);
      if (uuid == null) {
        sender.sendMessage(
            AdvancedBan.prefix
                + ((String) AdvancedBan.messages.get("not_registered")).replace("%player%", name));
        return false;
      }
      if (!banHandler.isBanned(uuid)) {
        sender.sendMessage(
            ((String) AdvancedBan.messages.get("ban.not_banned")).replace("%player%", name));
        return false;
      }
      // valid
      String senderUUID = (String) AdvancedBan.config.get("console");
      if (sender instanceof Player p) {
        senderUUID = p.getUniqueId().toString();
      }
      banHandler.unban(uuid);
      banHandler.log(uuid, senderUUID, "unban", "-", "-");
      sender.sendMessage(
          AdvancedBan.prefix
              + ((String) AdvancedBan.messages.get("ban.success.unban")).replace("%player%", name));
    } else {
      sender.sendMessage(AdvancedBan.prefix + AdvancedBan.messages.get("ban.help.unban"));
    }
    return false;
  }
}
