package de.ab.delta203.bungee.modules.mute.commands;

import de.ab.delta203.bungee.AdvancedBan;
import de.ab.delta203.bungee.modules.mute.mysql.MuteHandler;
import de.ab.delta203.bungee.mysql.PlayerInfoHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class UnMuteCommand extends Command {

  private final MuteHandler muteHandler;
  private final PlayerInfoHandler playerInfoHandler;

  public UnMuteCommand(String name) {
    super(name);
    muteHandler = new MuteHandler(AdvancedBan.mysql.connection);
    playerInfoHandler = new PlayerInfoHandler(AdvancedBan.mysql.connection);
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!sender.hasPermission("ab.unmute")) {
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
      if (!muteHandler.isMuted(uuid)) {
        sender.sendMessage(
            new TextComponent(
                AdvancedBan.prefix
                    + AdvancedBan.messages.getString("mute.not_muted").replace("%player%", name)));
        return;
      }
      // valid
      String senderUUID = AdvancedBan.config.getString("console");
      if (sender instanceof ProxiedPlayer p) {
        senderUUID = p.getUniqueId().toString();
      }
      muteHandler.unmute(uuid);
      muteHandler.log(uuid, senderUUID, "unmute", "-", "-");
      sender.sendMessage(
          new TextComponent(
              AdvancedBan.prefix
                  + AdvancedBan.messages
                      .getString("mute.success.unmute")
                      .replace("%player%", name)));
    } else {
      sender.sendMessage(
          new TextComponent(
              AdvancedBan.prefix + AdvancedBan.messages.getString("mute.help.unmute")));
    }
  }
}
