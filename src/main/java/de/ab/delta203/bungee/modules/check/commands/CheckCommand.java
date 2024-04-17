package de.ab.delta203.bungee.modules.check.commands;

import de.ab.delta203.core.AdvancedBan;
import de.ab.delta203.core.modules.ban.mysql.BanHandler;
import de.ab.delta203.core.modules.mute.mysql.MuteHandler;
import de.ab.delta203.core.mysql.PlayerInfoHandler;
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
    if (!sender.hasPermission("ab.check")) {
      sender.sendMessage(new TextComponent((String) AdvancedBan.messages.get("no_permission")));
      return;
    }
    if (args.length == 1) {
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
      String prefixTrue = (String) AdvancedBan.messages.get("check.prefix.true");
      String prefixFalse = (String) AdvancedBan.messages.get("check.prefix.false");
      sender.sendMessage(
          new TextComponent(
              AdvancedBan.prefix
                  + ((String) AdvancedBan.messages.get("check.title")).replace("%player%", name)));
      sender.sendMessage(
          new TextComponent(
              AdvancedBan.prefix
                  + ((String) AdvancedBan.messages.get("check.uuid")).replace("%uuid%", uuid)));
      sender.sendMessage(
          new TextComponent(
              AdvancedBan.prefix
                  + ((String) AdvancedBan.messages.get("check.ip")).replace("%ip%", ip)));
      sender.sendMessage(
          new TextComponent(
              AdvancedBan.prefix
                  + ((String) AdvancedBan.messages.get("check.server"))
                      .replace("%server%", server)));
      sender.sendMessage(
          new TextComponent(
              AdvancedBan.prefix
                  + ((String) AdvancedBan.messages.get("check.banned"))
                      .replace("%banned%", (banned ? prefixTrue : prefixFalse) + banned)));
      sender.sendMessage(
          new TextComponent(
              AdvancedBan.prefix
                  + ((String) AdvancedBan.messages.get("check.muted"))
                      .replace("%muted%", (muted ? prefixTrue : prefixFalse) + muted)));
    } else {
      sender.sendMessage(
          new TextComponent(AdvancedBan.prefix + AdvancedBan.messages.get("check.help")));
    }
  }
}
