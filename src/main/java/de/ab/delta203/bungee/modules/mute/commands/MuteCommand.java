package de.ab.delta203.bungee.modules.mute.commands;

import de.ab.delta203.core.AdvancedBan;
import de.ab.delta203.core.modules.mute.mysql.MuteHandler;
import de.ab.delta203.core.mysql.PlayerInfoHandler;
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
      sender.sendMessage(new TextComponent((String) AdvancedBan.messages.get("no_permission")));
      return;
    }
    if (args.length >= 2) {
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
      // valid
      StringBuilder reason = new StringBuilder();
      for (int i = 1; i < args.length; i++) {
        reason.append(args[i]).append(" ");
      }
      String senderUUID = (String) AdvancedBan.config.get("console");
      String senderDName = (String) AdvancedBan.config.get("console");
      if (sender instanceof ProxiedPlayer p) {
        senderUUID = p.getUniqueId().toString();
        senderDName = p.getDisplayName();
      }
      muteHandler.mute(uuid, senderUUID, -1, reason.toString());
      muteHandler.log(
          uuid,
          senderUUID,
          "mute",
          (String) AdvancedBan.messages.get("unit.permanent.name"),
          reason.toString());
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
                      .replace(
                          "%duration%", (String) AdvancedBan.messages.get("unit.permanent.name"))
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
          new TextComponent(AdvancedBan.prefix + AdvancedBan.messages.get("mute.help.mute")));
    }
  }
}
