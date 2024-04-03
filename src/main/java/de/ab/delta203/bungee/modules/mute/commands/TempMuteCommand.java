package de.ab.delta203.bungee.modules.mute.commands;

import de.ab.delta203.bungee.AdvancedBan;
import de.ab.delta203.bungee.modules.mute.mysql.MuteHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;

public class TempMuteCommand extends Command implements TabExecutor {

  private final MuteHandler muteHandler;

  public TempMuteCommand(String name) {
    super(name);
    muteHandler = new MuteHandler(AdvancedBan.mysql.connection);
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!sender.hasPermission("ab.tempmute")) {
      sender.sendMessage(new TextComponent(AdvancedBan.messages.getString("no_permission")));
      return;
    }
    if (args.length >= 4) {
      ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
      if (target == null) {
        sender.sendMessage(
            new TextComponent(
                AdvancedBan.prefix
                    + AdvancedBan.messages.getString("not_online").replace("%player%", args[0])));
        return;
      }
      /*if (target == sender) {
        sender.sendMessage(
            new TextComponent(
                AdvancedBan.prefix + AdvancedBan.messages.getString("mute.not_yourself")));
        return;
      }*/
      if (muteHandler.isMuted(target.getUniqueId().toString())) {
        sender.sendMessage(
            new TextComponent(
                AdvancedBan.prefix
                    + AdvancedBan.messages
                        .getString("mute.already_muted")
                        .replace("%player%", target.getName())
                        .replace("%playerDN%", target.getDisplayName())));
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
      muteHandler.mute(target.getUniqueId().toString(), senderUUID, end, reason.toString());
      sender.sendMessage(
          new TextComponent(
              AdvancedBan.prefix
                  + AdvancedBan.messages
                      .getString("mute.success.mute")
                      .replace("%player%", target.getName())
                      .replace("%playerDN%", target.getDisplayName())));
      // broadcast
      for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
        if (all.hasPermission("ab.tempmute") || all.hasPermission("ab.mute")) {
          if (AdvancedBan.config.getBoolean("notify.mute")) {
            all.sendMessage(
                new TextComponent(
                    AdvancedBan.prefix
                        + AdvancedBan.messages
                            .getString("mute.notification")
                            .replace("%player%", target.getName())
                            .replace("%playerDN%", target.getDisplayName())
                            .replace("%from%", sender.getName())
                            .replace("%fromDN%", senderDName)
                            .replace("%duration%", args[1] + " " + unitName)));
          }
        }
      }
    } else {
      sender.sendMessage(
          new TextComponent(
              AdvancedBan.prefix + AdvancedBan.messages.getString("mute.help.tempmute")));
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
