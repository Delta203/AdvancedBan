package de.ab.delta203.spigot.commands;

import de.ab.delta203.core.AdvancedBan;
import de.ab.delta203.core.mysql.PlayerInfoHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleBanCommand implements CommandExecutor {

  private final PlayerInfoHandler playerInfoHandler;

  public ToggleBanCommand() {
    playerInfoHandler = new PlayerInfoHandler(AdvancedBan.mysql.connection);
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (sender instanceof Player p) {
      if (p.hasPermission("ab.ban") || p.hasPermission("ab.tempban")) {
        boolean enabled =
            playerInfoHandler.hasNotify(
                p.getUniqueId().toString(), PlayerInfoHandler.Notification.BAN);
        playerInfoHandler.updateNotify(
            p.getUniqueId().toString(), PlayerInfoHandler.Notification.BAN, !enabled);
        if (enabled) {
          p.sendMessage(AdvancedBan.prefix + AdvancedBan.messages.get("toggle.ban.disabled"));
        } else {
          p.sendMessage(AdvancedBan.prefix + AdvancedBan.messages.get("toggle.ban.enabled"));
        }
      } else {
        sender.sendMessage((String) AdvancedBan.messages.get("no_permission"));
      }
    }
    return false;
  }
}
