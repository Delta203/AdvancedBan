package de.ab.delta203.spigot.modules.ban.commands;

import de.ab.delta203.core.AdvancedBan;
import de.ab.delta203.core.modules.ban.mysql.BanHandler;
import de.ab.delta203.core.mysql.PlayerInfoHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TempBanCommand implements TabExecutor {

  private final BanHandler banHandler;
  private final PlayerInfoHandler playerInfoHandler;

  public TempBanCommand() {
    banHandler = new BanHandler(AdvancedBan.mysql.connection);
    playerInfoHandler = new PlayerInfoHandler(AdvancedBan.mysql.connection);
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (!sender.hasPermission("ab.tempban")) {
      sender.sendMessage((String) AdvancedBan.messages.get("no_permission"));
      return false;
    }
    if (args.length >= 4) {
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
      long value;
      try {
        value = Long.parseLong(args[1]);
      } catch (NumberFormatException e) {
        sender.sendMessage(AdvancedBan.prefix + AdvancedBan.messages.get("unit.not_a_number"));
        return false;
      }
      if (value > (int) AdvancedBan.config.get("max")) {
        sender.sendMessage(AdvancedBan.prefix + AdvancedBan.messages.get("unit.to_big"));
        return false;
      }
      if (value <= 0) {
        sender.sendMessage(AdvancedBan.prefix + AdvancedBan.messages.get("unit.invalid_value"));
        return false;
      }
      String unit = args[2];
      String unitName;
      if (unit.equalsIgnoreCase((String) AdvancedBan.messages.get("unit.seconds.alias"))) {
        value *= 1000;
        unitName = (String) AdvancedBan.messages.get("unit.seconds.name");
      } else if (unit.equalsIgnoreCase((String) AdvancedBan.messages.get("unit.minutes.alias"))) {
        value *= 60 * 1000;
        unitName = (String) AdvancedBan.messages.get("unit.minutes.name");
      } else if (unit.equalsIgnoreCase((String) AdvancedBan.messages.get("unit.hours.alias"))) {
        value *= 60 * 60 * 1000;
        unitName = (String) AdvancedBan.messages.get("unit.hours.name");
      } else if (unit.equalsIgnoreCase((String) AdvancedBan.messages.get("unit.days.alias"))) {
        value *= 24 * 60 * 60 * 1000;
        unitName = (String) AdvancedBan.messages.get("unit.days.name");
      } else {
        sender.sendMessage(
            AdvancedBan.prefix
                + ((String) AdvancedBan.messages.get("unit.invalid_unit"))
                    .replace("%sec%", (String) AdvancedBan.messages.get("unit.seconds.alias"))
                    .replace("%min%", (String) AdvancedBan.messages.get("unit.minutes.alias"))
                    .replace("%hour%", (String) AdvancedBan.messages.get("unit.hours.alias"))
                    .replace("%day%", (String) AdvancedBan.messages.get("unit.days.alias")));
        return false;
      }
      // valid
      StringBuilder reason = new StringBuilder();
      for (int i = 3; i < args.length; i++) {
        reason.append(args[i]).append(" ");
      }
      String senderUUID = (String) AdvancedBan.config.get("console");
      String senderDName = (String) AdvancedBan.config.get("console");
      if (sender instanceof Player p) {
        senderUUID = p.getUniqueId().toString();
        senderDName = p.getDisplayName();
      }
      long end = System.currentTimeMillis() + value;
      String ip = "-";
      Player target = Bukkit.getPlayer(name);
      if (target != null) {
        ip = Objects.requireNonNull(target.getAddress()).getAddress().toString().split(":")[0];
        target.kickPlayer(
            ((String) AdvancedBan.messages.get("ban.message.kick"))
                .replace("%reason%", reason)
                .replace("\\n", "\n"));
      }
      banHandler.ban(uuid, ip, senderUUID, end, reason.toString());
      banHandler.log(uuid, senderUUID, "tempban", args[1] + " " + unitName, reason.toString());
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
                  .replace("%duration%", args[1] + " " + unitName)
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
      sender.sendMessage(AdvancedBan.prefix + AdvancedBan.messages.get("ban.help.tempban"));
    }
    return false;
  }

  @Override
  public List<String> onTabComplete(
      CommandSender sender, Command cmd, String label, String[] args) {
    if (args.length == 3) {
      ArrayList<String> arguments = new ArrayList<>();
      arguments.add((String) AdvancedBan.messages.get("unit.seconds.alias"));
      arguments.add((String) AdvancedBan.messages.get("unit.minutes.alias"));
      arguments.add((String) AdvancedBan.messages.get("unit.hours.alias"));
      arguments.add((String) AdvancedBan.messages.get("unit.days.alias"));
      return arguments;
    }
    return new ArrayList<>();
  }
}
