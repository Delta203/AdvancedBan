package de.ab.delta203.bungee.modules.ban.commands;

import de.ab.delta203.bungee.AdvancedBan;
import de.ab.delta203.bungee.modules.ban.mysql.BanHandler;
import de.ab.delta203.bungee.mysql.PlayerInfoHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BanCommand extends Command {

  private final BanHandler banHandler;
  private final PlayerInfoHandler playerInfoHandler;

  public BanCommand(String name) {
    super(name);
    banHandler = new BanHandler(AdvancedBan.mysql.connection);
    playerInfoHandler = new PlayerInfoHandler(AdvancedBan.mysql.connection);
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!sender.hasPermission("ab.ban")) {
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
      banHandler.ban(uuid, ip, senderUUID, -1, reason.toString());
      banHandler.log(
          uuid,
          senderUUID,
          "ban",
          AdvancedBan.messages.getString("unit.permanent.name"),
          reason.toString());
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
                      .replace("%duration%", AdvancedBan.messages.getString("unit.permanent.name"))
                      .replace("\\n", "\n"));
      for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
        if (all.hasPermission("ab.ban") || all.hasPermission("ab.tempban")) {
          if (playerInfoHandler.hasNotify(all, PlayerInfoHandler.Notification.BAN)) {
            all.sendMessage(textComponent);
          }
        }
      }
    } else {
      sender.sendMessage(
          new TextComponent(AdvancedBan.prefix + AdvancedBan.messages.getString("ban.help.ban")));
    }
  }
}
