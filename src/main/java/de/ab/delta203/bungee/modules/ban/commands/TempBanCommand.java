package de.ab.delta203.bungee.modules.ban.commands;

import de.ab.delta203.bungee.AdvancedBan;
import de.ab.delta203.bungee.modules.ban.mysql.BanHandler;
import de.ab.delta203.bungee.mysql.PlayerInfoHandler;
import java.util.ArrayList;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class TempBanCommand extends Command implements TabExecutor {

  private final BanHandler banHandler;
  private final PlayerInfoHandler playerInfoHandler;

  public TempBanCommand(String name) {
    super(name);
    banHandler = new BanHandler(AdvancedBan.mysql.connection);
    playerInfoHandler = new PlayerInfoHandler(AdvancedBan.mysql.connection);
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!sender.hasPermission("ab.tempban")) {
      sender.sendMessage(new TextComponent(AdvancedBan.messages.getString("no_permission")));
      return;
    }
    if (args.length >= 4) {
      String name = args[0];
      String uuid = playerInfoHandler.getUUID(name);
      if (uuid == null) {
        sender.sendMessage(
            new TextComponent(
                AdvancedBan.prefix
                    + AdvancedBan.messages.getString("not_registered").replace("%player%", name)));
        return;
      }
      if (!AdvancedBan.config.getBoolean("self")) {
        if (sender.getName().equals(name)) {
          sender.sendMessage(
              new TextComponent(
                  AdvancedBan.prefix + AdvancedBan.messages.getString("ban.not_yourself")));
          return;
        }
      }
      if (banHandler.isBanned(uuid)) {
        sender.sendMessage(
            new TextComponent(
                AdvancedBan.prefix
                    + AdvancedBan.messages
                        .getString("ban.already_banned")
                        .replace("%player%", name)));
        return;
      }
      long value;
      try {
        value = Long.parseLong(args[1]);
      } catch (NumberFormatException e) {
        sender.sendMessage(
            new TextComponent(
                AdvancedBan.prefix + AdvancedBan.messages.getString("unit.not_a_number")));
        return;
      }
      if (value > AdvancedBan.config.getInt("max")) {
        sender.sendMessage(
            new TextComponent(AdvancedBan.prefix + AdvancedBan.messages.getString("unit.to_big")));
        return;
      }
      if (value <= 0) {
        sender.sendMessage(
            new TextComponent(
                AdvancedBan.prefix + AdvancedBan.messages.getString("unit.invalid_value")));
        return;
      }
      String unit = args[2];
      String unitName;
      if (unit.equalsIgnoreCase(AdvancedBan.messages.getString("unit.seconds.alias"))) {
        value *= 1000;
        unitName = AdvancedBan.messages.getString("unit.seconds.name");
      } else if (unit.equalsIgnoreCase(AdvancedBan.messages.getString("unit.minutes.alias"))) {
        value *= 60 * 1000;
        unitName = AdvancedBan.messages.getString("unit.minutes.name");
      } else if (unit.equalsIgnoreCase(AdvancedBan.messages.getString("unit.hours.alias"))) {
        value *= 60 * 60 * 1000;
        unitName = AdvancedBan.messages.getString("unit.hours.name");
      } else if (unit.equalsIgnoreCase(AdvancedBan.messages.getString("unit.days.alias"))) {
        value *= 24 * 60 * 60 * 1000;
        unitName = AdvancedBan.messages.getString("unit.days.name");
      } else {
        sender.sendMessage(
            new TextComponent(
                AdvancedBan.prefix
                    + AdvancedBan.messages
                        .getString("unit.invalid_unit")
                        .replace("%sec%", AdvancedBan.messages.getString("unit.seconds.alias"))
                        .replace("%min%", AdvancedBan.messages.getString("unit.minutes.alias"))
                        .replace("%hour%", AdvancedBan.messages.getString("unit.hours.alias"))
                        .replace("%day%", AdvancedBan.messages.getString("unit.days.alias"))));
        return;
      }
      // valid
      StringBuilder reason = new StringBuilder();
      for (int i = 3; i < args.length; i++) {
        reason.append(args[i]).append(" ");
      }
      String senderUUID = AdvancedBan.config.getString("console");
      String senderDName = AdvancedBan.config.getString("console");
      if (sender instanceof ProxiedPlayer p) {
        senderUUID = p.getUniqueId().toString();
        senderDName = p.getDisplayName();
      }
      long end = System.currentTimeMillis() + value;
      String ip = "-";
      ProxiedPlayer target = ProxyServer.getInstance().getPlayer(name);
      if (target != null) {
        ip = target.getSocketAddress().toString().split(":")[0];
        target.disconnect(
            new TextComponent(
                AdvancedBan.messages
                    .getString("ban.message.kick")
                    .replace("%reason%", reason)
                    .replace("\\n", "\n")));
      }
      banHandler.ban(uuid, ip, senderUUID, end, reason.toString());
      banHandler.log(uuid, senderUUID, "tempban", args[1] + " " + unitName, reason.toString());
      sender.sendMessage(
          new TextComponent(
              AdvancedBan.prefix
                  + AdvancedBan.messages.getString("ban.success.ban").replace("%player%", name)));
      // broadcast
      TextComponent textComponent =
          new TextComponent(
              AdvancedBan.prefix
                  + AdvancedBan.messages
                      .getString("ban.notification")
                      .replace("%player%", name)
                      .replace("%from%", sender.getName())
                      .replace("%fromDN%", senderDName)
                      .replace("%reason%", reason)
                      .replace("%duration%", args[1] + " " + unitName)
                      .replace("\\n", "\n"));
      for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
        if (all.hasPermission("ab.ban") || all.hasPermission("ab.tempban")) {
          if (playerInfoHandler.hasNotify(all, PlayerInfoHandler.Notification.BAN)) {
            if (all == target) continue;
            all.sendMessage(textComponent);
          }
        }
      }
    } else {
      sender.sendMessage(
          new TextComponent(
              AdvancedBan.prefix + AdvancedBan.messages.getString("ban.help.tempban")));
    }
  }

  @Override
  public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
    if (args.length == 3) {
      ArrayList<String> arguments = new ArrayList<>();
      arguments.add(AdvancedBan.messages.getString("unit.seconds.alias"));
      arguments.add(AdvancedBan.messages.getString("unit.minutes.alias"));
      arguments.add(AdvancedBan.messages.getString("unit.hours.alias"));
      arguments.add(AdvancedBan.messages.getString("unit.days.alias"));
      return arguments;
    }
    return new ArrayList<>();
  }
}
