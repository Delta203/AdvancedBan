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
      /*if (sender.getName().equals(name)) {
        sender.sendMessage(
            new TextComponent(
                AdvancedBan.prefix + AdvancedBan.messages.getString("ban.not_yourself")));
        return;
      }*/
      if (banHandler.isBanned(uuid)) {
        sender.sendMessage(
            new TextComponent(
                AdvancedBan.prefix
                    + AdvancedBan.messages
                        .getString("ban.already_banned")
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
      String ip = "-";
      ProxiedPlayer target = ProxyServer.getInstance().getPlayer(name);
      if (target != null) {
        ip = target.getSocketAddress().toString();
        target.disconnect(
            new TextComponent(
                AdvancedBan.messages.getString("ban.message.kick").replace("%reason%", reason)));
      }
      banHandler.ban(uuid, ip, senderUUID, -1, reason.toString());
      sender.sendMessage(
          new TextComponent(
              AdvancedBan.prefix
                  + AdvancedBan.messages.getString("ban.success.ban").replace("%player%", name)));
      // broadcast
      if (AdvancedBan.config.getBoolean("notify.ban")) {
        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
          if (all.hasPermission("ab.tempban") || all.hasPermission("ab.ban")) {
            all.sendMessage(
                new TextComponent(
                    AdvancedBan.prefix
                        + AdvancedBan.messages
                            .getString("ban.notification")
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
          new TextComponent(AdvancedBan.prefix + AdvancedBan.messages.getString("ban.help.ban")));
    }
  }
}
