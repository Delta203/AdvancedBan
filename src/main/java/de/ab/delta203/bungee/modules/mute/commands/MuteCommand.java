package de.ab.delta203.bungee.modules.mute.commands;

import de.ab.delta203.bungee.AdvancedBan;
import de.ab.delta203.bungee.modules.mute.mysql.MuteHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MuteCommand extends Command {

  private final MuteHandler muteHandler;

  public MuteCommand(String name) {
    super(name);
    muteHandler = new MuteHandler(AdvancedBan.mysql.connection);
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!sender.hasPermission("ab.mute")) {
      sender.sendMessage(new TextComponent(AdvancedBan.messages.getString("no_permission")));
      return;
    }
    if (args.length >= 2) {
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
      // valid
      StringBuilder reason = new StringBuilder();
      for (int i = 1; i < args.length; i++) {
        reason.append(args[i]).append(" ");
      }
      String senderUUID = AdvancedBan.config.getString("console");
      String senderDName = AdvancedBan.config.getString("console");
      if (sender instanceof ProxiedPlayer p) {
        senderUUID = p.getUniqueId().toString();
        senderDName = p.getDisplayName();
      }
      muteHandler.mute(target.getUniqueId().toString(), senderUUID, -1, reason.toString());
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
                            .replace(
                                "%duration%",
                                AdvancedBan.messages.getString("unit.permanent.name"))));
          }
        }
      }
    } else {
      sender.sendMessage(
          new TextComponent(AdvancedBan.prefix + AdvancedBan.messages.getString("mute.help.mute")));
    }
  }
}
