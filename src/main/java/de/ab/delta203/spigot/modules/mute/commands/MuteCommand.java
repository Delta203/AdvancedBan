package de.ab.delta203.spigot.modules.mute.commands;

import de.ab.delta203.core.AdvancedBan;
import de.ab.delta203.core.modules.mute.mysql.MuteHandler;
import de.ab.delta203.core.mysql.PlayerInfoHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteCommand implements CommandExecutor {

  private final MuteHandler muteHandler;
  private final PlayerInfoHandler playerInfoHandler;

  public MuteCommand() {
    muteHandler = new MuteHandler(AdvancedBan.mysql.connection);
    playerInfoHandler = new PlayerInfoHandler(AdvancedBan.mysql.connection);
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (!sender.hasPermission("ab.mute")) {
      sender.sendMessage((String) AdvancedBan.messages.get("no_permission"));
      return false;
    }
    if (args.length >= 2) {
      String name = args[0];
      String uuid = playerInfoHandler.getUUID(name);
      if (uuid == null) {
        sender.sendMessage(
            AdvancedBan.prefix
                + ((String) AdvancedBan.messages.get("not_registered")).replace("%player%", name));
        return false;
      }
      if (!(boolean) AdvancedBan.config.get("self")) {
        if (sender.getName().equals(name)) {
          sender.sendMessage(AdvancedBan.prefix + AdvancedBan.messages.get("mute.not_yourself"));
          return false;
        }
      }
      if (muteHandler.isMuted(uuid)) {
        sender.sendMessage(
            AdvancedBan.prefix
                + ((String) AdvancedBan.messages.get("mute.already_muted"))
                    .replace("%player%", name));
        return false;
      }
      // valid
      StringBuilder reason = new StringBuilder();
      for (int i = 1; i < args.length; i++) {
        reason.append(args[i]).append(" ");
      }
      String senderUUID = (String) AdvancedBan.config.get("console");
      String senderDName = (String) AdvancedBan.config.get("console");
      if (sender instanceof Player p) {
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
          AdvancedBan.prefix
              + ((String) AdvancedBan.messages.get("mute.success.mute")).replace("%player%", name));
      // broadcast
      String textComponent =
          AdvancedBan.prefix
              + ((String) AdvancedBan.messages.get("mute.notification"))
                  .replace("%player%", name)
                  .replace("%from%", sender.getName())
                  .replace("%fromDN%", senderDName)
                  .replace("%reason%", reason)
                  .replace("%duration%", (String) AdvancedBan.messages.get("unit.permanent.name"))
                  .replace("\\n", "\n");
      for (Player all : Bukkit.getOnlinePlayers()) {
        if (all.hasPermission("ab.mute") || all.hasPermission("ab.tempmute")) {
          if (playerInfoHandler.hasNotify(
              all.getUniqueId().toString(), PlayerInfoHandler.Notification.MUTE)) {
            all.sendMessage(textComponent);
          }
        }
      }
    } else {
      sender.sendMessage(AdvancedBan.prefix + AdvancedBan.messages.get("mute.help.mute"));
    }
    return false;
  }
}
