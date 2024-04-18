package de.ab.delta203.spigot.modules.mute.commands;

import de.ab.delta203.core.AdvancedBan;
import de.ab.delta203.core.modules.mute.mysql.MuteHandler;
import de.ab.delta203.core.mysql.PlayerInfoHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnMuteCommand implements CommandExecutor {

  private final MuteHandler muteHandler;
  private final PlayerInfoHandler playerInfoHandler;

  public UnMuteCommand() {
    muteHandler = new MuteHandler(AdvancedBan.mysql.connection);
    playerInfoHandler = new PlayerInfoHandler(AdvancedBan.mysql.connection);
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (!sender.hasPermission("ab.unmute")) {
      sender.sendMessage((String) AdvancedBan.messages.get("no_permission"));
      return false;
    }
    if (args.length == 1) {
      String name = args[0];
      String uuid = playerInfoHandler.getUUID(name);
      if (uuid == null) {
        sender.sendMessage(
            AdvancedBan.prefix
                + ((String) AdvancedBan.messages.get("not_registered")).replace("%player%", name));
        return false;
      }
      if (!muteHandler.isMuted(uuid)) {
        sender.sendMessage(
            AdvancedBan.prefix
                + ((String) AdvancedBan.messages.get("mute.not_muted")).replace("%player%", name));
        return false;
      }
      // valid
      String senderUUID = (String) AdvancedBan.config.get("console");
      if (sender instanceof Player p) {
        senderUUID = p.getUniqueId().toString();
      }
      muteHandler.unmute(uuid);
      muteHandler.log(uuid, senderUUID, "unmute", "-", "-");
      sender.sendMessage(
          AdvancedBan.prefix
              + ((String) AdvancedBan.messages.get("mute.success.unmute"))
                  .replace("%player%", name));
    } else {
      sender.sendMessage(AdvancedBan.prefix + AdvancedBan.messages.get("mute.help.unmute"));
    }
    return false;
  }
}
