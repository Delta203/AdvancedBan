package de.ab.delta203.bungee.modules.ban.commands;

import de.ab.delta203.bungee.AdvancedBan;
import de.ab.delta203.bungee.modules.ban.mysql.BanHandler;
import de.ab.delta203.bungee.mysql.PlayerInfoHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class UnBanCommand extends Command {

  private final BanHandler banHandler;
  private final PlayerInfoHandler playerInfoHandler;

  public UnBanCommand(String name) {
    super(name);
    banHandler = new BanHandler(AdvancedBan.mysql.connection);
    playerInfoHandler = new PlayerInfoHandler(AdvancedBan.mysql.connection);
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!sender.hasPermission("ab.unban")) {
      sender.sendMessage(new TextComponent(AdvancedBan.messages.getString("no_permission")));
      return;
    }
    if (args.length == 1) {
      String name = args[0];
      String uuid = playerInfoHandler.getUUID(name);
      if (uuid == null) {
        sender.sendMessage(
            new TextComponent(
                AdvancedBan.prefix
                    + AdvancedBan.messages.getString("not_registered").replace("%player%", name)));
        return;
      }
      if (!banHandler.isBanned(uuid)) {
        sender.sendMessage(
            new TextComponent(
                AdvancedBan.prefix
                    + AdvancedBan.messages.getString("ban.not_banned").replace("%player%", name)));
        return;
      }
      // valid
      String senderUUID = AdvancedBan.config.getString("console");
      if (sender instanceof ProxiedPlayer p) {
        senderUUID = p.getUniqueId().toString();
      }
      banHandler.unban(uuid);
      banHandler.log(uuid, senderUUID, "unban", "-", "-");
      sender.sendMessage(
          new TextComponent(
              AdvancedBan.prefix
                  + AdvancedBan.messages.getString("ban.success.unban").replace("%player%", name)));
    } else {
      sender.sendMessage(
          new TextComponent(AdvancedBan.prefix + AdvancedBan.messages.getString("ban.help.unban")));
    }
  }
}
