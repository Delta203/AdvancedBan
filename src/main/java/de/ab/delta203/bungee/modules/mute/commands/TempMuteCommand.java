package de.ab.delta203.bungee.modules.mute.commands;

import de.ab.delta203.core.AdvancedBan;
import de.ab.delta203.core.modules.mute.mysql.MuteHandler;
import de.ab.delta203.core.mysql.PlayerInfoHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;

public class TempMuteCommand extends Command implements TabExecutor {

  private final MuteHandler muteHandler;
  private final PlayerInfoHandler playerInfoHandler;

  public TempMuteCommand(String name) {
    super(name);
    muteHandler = new MuteHandler(AdvancedBan.mysql.connection);
    playerInfoHandler = new PlayerInfoHandler(AdvancedBan.mysql.connection);
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!sender.hasPermission("ab.tempmute")) {
      sender.sendMessage(new TextComponent((String) AdvancedBan.messages.get("no_permission")));
      return;
    }
    if (args.length >= 4) {
      String name = args[0];
      String uuid = playerInfoHandler.getUUID(name);
      if (uuid == null) {
        sender.sendMessage(
            new TextComponent(
                AdvancedBan.prefix
                    + ((String) AdvancedBan.messages.get("not_registered"))
                        .replace("%player%", name)));
        return;
      }
      if (!(boolean) AdvancedBan.config.get("self")) {
        if (sender.getName().equals(name)) {
          sender.sendMessage(
              new TextComponent(
                  AdvancedBan.prefix + AdvancedBan.messages.get("mute.not_yourself")));
          return;
        }
      }
      if (muteHandler.isMuted(uuid)) {
        sender.sendMessage(
            new TextComponent(
                AdvancedBan.prefix
                    + ((String) AdvancedBan.messages.get("mute.already_muted"))
                        .replace("%player%", name)));
        return;
      }
      long value;
      try {
        value = Long.parseLong(args[1]);
      } catch (NumberFormatException e) {
        sender.sendMessage(
            new TextComponent(AdvancedBan.prefix + AdvancedBan.messages.get("unit.not_a_number")));
        return;
      }
      if (value > (int) AdvancedBan.config.get("max")) {
        sender.sendMessage(
            new TextComponent(AdvancedBan.prefix + AdvancedBan.messages.get("unit.to_big")));
        return;
      }
      if (value <= 0) {
        sender.sendMessage(
            new TextComponent(AdvancedBan.prefix + AdvancedBan.messages.get("unit.invalid_value")));
        return;
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
            new TextComponent(
                AdvancedBan.prefix
                    + ((String) AdvancedBan.messages.get("unit.invalid_unit"))
                        .replace("%sec%", (String) AdvancedBan.messages.get("unit.seconds.alias"))
                        .replace("%min%", (String) AdvancedBan.messages.get("unit.minutes.alias"))
                        .replace("%hour%", (String) AdvancedBan.messages.get("unit.hours.alias"))
                        .replace("%day%", (String) AdvancedBan.messages.get("unit.days.alias"))));
        return;
      }
      // valid
      StringBuilder reason = new StringBuilder();
      for (int i = 3; i < args.length; i++) {
        reason.append(args[i]).append(" ");
      }
      String senderUUID = (String) AdvancedBan.config.get("console");
      String senderDName = (String) AdvancedBan.config.get("console");
      if (sender instanceof ProxiedPlayer p) {
        senderUUID = p.getUniqueId().toString();
        senderDName = p.getDisplayName();
      }
      long end = System.currentTimeMillis() + value;
      muteHandler.mute(uuid, senderUUID, end, reason.toString());
      muteHandler.log(uuid, senderUUID, "tempmute", args[1] + " " + unitName, reason.toString());
      sender.sendMessage(
          new TextComponent(
              AdvancedBan.prefix
                  + ((String) AdvancedBan.messages.get("mute.success.mute"))
                      .replace("%player%", name)));
      // broadcast
      TextComponent textComponent =
          new TextComponent(
              AdvancedBan.prefix
                  + ((String) AdvancedBan.messages.get("mute.notification"))
                      .replace("%player%", name)
                      .replace("%from%", sender.getName())
                      .replace("%fromDN%", senderDName)
                      .replace("%reason%", reason)
                      .replace("%duration%", args[1] + " " + unitName)
                      .replace("\\n", "\n"));
      for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
        if (all.hasPermission("ab.mute") || all.hasPermission("ab.tempmute")) {
          if (playerInfoHandler.hasNotify(
              all.getUniqueId().toString(), PlayerInfoHandler.Notification.MUTE)) {
            all.sendMessage(textComponent);
          }
        }
      }
    } else {
      sender.sendMessage(
          new TextComponent(AdvancedBan.prefix + AdvancedBan.messages.get("mute.help.tempmute")));
    }
  }

  @Override
  public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
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
