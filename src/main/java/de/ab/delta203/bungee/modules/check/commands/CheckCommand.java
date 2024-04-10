package de.ab.delta203.bungee.modules.check.commands;

import de.ab.delta203.bungee.AdvancedBan;
import de.ab.delta203.bungee.modules.ban.mysql.BanHandler;
import de.ab.delta203.bungee.modules.mute.mysql.MuteHandler;
import de.ab.delta203.bungee.mysql.PlayerInfoHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CheckCommand extends Command {

  private final PlayerInfoHandler playerInfoHandler;
  private final BanHandler banHandler;
  private final MuteHandler muteHandler;

  public CheckCommand(String name) {
    super(name);
    playerInfoHandler = new PlayerInfoHandler(AdvancedBan.mysql.connection);
    banHandler = new BanHandler(AdvancedBan.mysql.connection);
    muteHandler = new MuteHandler(AdvancedBan.mysql.connection);
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (args.length == 1) {
      if (!sender.hasPermission("ab.check")) {
        sender.sendMessage(new TextComponent(AdvancedBan.messages.getString("no_permission")));
        return;
      }
      String name = args[0];
      String uuidCached = playerInfoHandler.getUUID(name);
      String uuid = uuidCached == null ? "-" : uuidCached;
      String ip = "-";
      String server = "-";
      boolean banned = banHandler.isBanned(uuid);
      boolean muted = muteHandler.isMuted(uuid);
      ProxiedPlayer target = ProxyServer.getInstance().getPlayer(name);
      if (target != null) {
        uuid = target.getUniqueId().toString();
        ip = target.getSocketAddress().toString();
        server = target.getServer().getInfo().getName();
      }
      String prefixTrue = AdvancedBan.messages.getString("check.prefix.true");
      String prefixFalse = AdvancedBan.messages.getString("check.prefix.false");
      sender.sendMessage(
          new TextComponent(
              AdvancedBan.prefix
                  + AdvancedBan.messages.getString("check.title").replace("%player%", name)));
      sender.sendMessage(
          new TextComponent(
              AdvancedBan.prefix
                  + AdvancedBan.messages.getString("check.uuid").replace("%uuid%", uuid)));
      sender.sendMessage(
          new TextComponent(
              AdvancedBan.prefix + AdvancedBan.messages.getString("check.ip").replace("%ip%", ip)));
      sender.sendMessage(
          new TextComponent(
              AdvancedBan.prefix
                  + AdvancedBan.messages.getString("check.server").replace("%server%", server)));
      sender.sendMessage(
          new TextComponent(
              AdvancedBan.prefix
                  + AdvancedBan.messages
                      .getString("check.banned")
                      .replace("%banned%", (banned ? prefixTrue : prefixFalse) + banned)));
      sender.sendMessage(
          new TextComponent(
              AdvancedBan.prefix
                  + AdvancedBan.messages
                      .getString("check.muted")
                      .replace("%muted%", (muted ? prefixTrue : prefixFalse) + muted)));
    } else {
      sender.sendMessage(
          new TextComponent(AdvancedBan.prefix + AdvancedBan.messages.getString("check.help")));
    }
  }
}
