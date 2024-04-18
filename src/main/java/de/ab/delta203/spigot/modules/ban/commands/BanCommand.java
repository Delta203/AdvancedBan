package de.ab.delta203.spigot.modules.ban.commands;

import de.ab.delta203.core.AdvancedBan;
import de.ab.delta203.core.modules.ban.mysql.BanHandler;
import de.ab.delta203.core.mysql.PlayerInfoHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class BanCommand implements CommandExecutor {

  private final BanHandler banHandler;
  private final PlayerInfoHandler playerInfoHandler;

  public BanCommand() {
    banHandler = new BanHandler(AdvancedBan.mysql.connection);
    playerInfoHandler = new PlayerInfoHandler(AdvancedBan.mysql.connection);
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (!sender.hasPermission("ab.ban")) {
      sender.sendMessage((String) AdvancedBan.messages.get("no_permission"));
      return false;
    }
    if (args.length >= 2) {
      String name = args[0];
      String uuid = playerInfoHandler.getUUID(name);
      if (uuid == null) {
        sender.sendMessage(
            AdvancedBan.prefix
                + ((String) AdvancedBan.messages.get("not_registered")).replace("%player%", name));
        return false;
      }
      if (!(boolean) AdvancedBan.config.get("self")) {
        if (sender.getName().equals(name)) {
          sender.sendMessage(AdvancedBan.prefix + AdvancedBan.messages.get("ban.not_yourself"));
          return false;
        }
      }
      if (banHandler.isBanned(uuid)) {
        sender.sendMessage(
            AdvancedBan.prefix
                + ((String) AdvancedBan.messages.get("ban.already_banned"))
                    .replace("%player%", name));
        return false;
      }
      // valid
      StringBuilder reason = new StringBuilder();
      for (int i = 1; i < args.length; i++) {
        reason.append(args[i]).append(" ");
      }
      String senderUUID = (String) AdvancedBan.config.get("console");
      String senderDName = (String) AdvancedBan.config.get("console");
      if (sender instanceof Player p) {
        senderUUID = p.getUniqueId().toString();
        senderDName = p.getDisplayName();
      }
      String ip = "-";
      Player target = Bukkit.getPlayer(name);
      if (target != null) {
        ip = Objects.requireNonNull(target.getAddress()).getAddress().toString().split(":")[0];
        target.kickPlayer(
            ((String) AdvancedBan.messages.get("ban.message.kick"))
                .replace("%reason%", reason)
                .replace("\\n", "\n"));
      }
      banHandler.ban(uuid, ip, senderUUID, -1, reason.toString());
      banHandler.log(
          uuid,
          senderUUID,
          "ban",
          (String) AdvancedBan.messages.get("unit.permanent.name"),
          reason.toString());
      sender.sendMessage(
          AdvancedBan.prefix
              + ((String) AdvancedBan.messages.get("ban.success.ban")).replace("%player%", name));
      // broadcast
      String textComponent =
          AdvancedBan.prefix
              + ((String) AdvancedBan.messages.get("ban.notification"))
                  .replace("%player%", name)
                  .replace("%from%", sender.getName())
                  .replace("%fromDN%", senderDName)
                  .replace("%reason%", reason)
                  .replace("%duration%", (String) AdvancedBan.messages.get("unit.permanent.name"))
                  .replace("\\n", "\n");
      for (Player all : Bukkit.getOnlinePlayers()) {
        if (all.hasPermission("ab.ban") || all.hasPermission("ab.tempban")) {
          if (playerInfoHandler.hasNotify(
              all.getUniqueId().toString(), PlayerInfoHandler.Notification.BAN)) {
            if (all == target) continue;
            all.sendMessage(textComponent);
          }
        }
      }
    } else {
      sender.sendMessage(AdvancedBan.prefix + AdvancedBan.messages.get("ban.help.ban"));
    }
    return false;
  }
}
