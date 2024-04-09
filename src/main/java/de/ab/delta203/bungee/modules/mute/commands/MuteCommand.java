package de.ab.delta203.bungee.modules.mute.commands;

import de.ab.delta203.bungee.AdvancedBan;
import de.ab.delta203.bungee.modules.mute.mysql.MuteHandler;
import de.ab.delta203.bungee.mysql.PlayerInfoHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MuteCommand extends Command {

  private final MuteHandler muteHandler;
  private final PlayerInfoHandler playerInfoHandler;

  public MuteCommand(String name) {
    super(name);
    muteHandler = new MuteHandler(AdvancedBan.mysql.connection);
    playerInfoHandler = new PlayerInfoHandler(AdvancedBan.mysql.connection);
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!sender.hasPermission("ab.mute")) {
      sender.sendMessage(new TextComponent(AdvancedBan.messages.getString("no_permission")));
      return;
    }
    if (args.length >= 2) {
      String name = args[0];
      String uuid = playerInfoHandler.getUUID(name);
      if (uuid == null) {
        sender.sendMessage(
            new TextComponent(
                AdvancedBan.prefix
                    + AdvancedBan.messages.getString("not_registered").replace("%player%", name)));
        return;
      }
      /*if (sender.getName().equals(name)) {
        sender.sendMessage(
            new TextComponent(
                AdvancedBan.prefix + AdvancedBan.messages.getString("mute.not_yourself")));
        return;
      }*/
      if (muteHandler.isMuted(uuid)) {
        sender.sendMessage(
            new TextComponent(
                AdvancedBan.prefix
                    + AdvancedBan.messages
                        .getString("mute.already_muted")
                        .replace("%player%", uuid)));
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
      muteHandler.mute(uuid, senderUUID, -1, reason.toString());
      sender.sendMessage(
          new TextComponent(
              AdvancedBan.prefix
                  + AdvancedBan.messages.getString("mute.success.mute").replace("%player%", name)));
      // broadcast
      if (AdvancedBan.config.getBoolean("notify.mute")) {
        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
          if (all.hasPermission("ab.tempmute") || all.hasPermission("ab.mute")) {
            all.sendMessage(
                new TextComponent(
                    AdvancedBan.prefix
                        + AdvancedBan.messages
                            .getString("mute.notification")
                            .replace("%player%", name)
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