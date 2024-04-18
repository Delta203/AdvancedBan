package de.ab.delta203.bungee.commands;

import de.ab.delta203.core.AdvancedBan;
import de.ab.delta203.core.mysql.PlayerInfoHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ToggleBanCommand extends Command {

  private final PlayerInfoHandler playerInfoHandler;

  public ToggleBanCommand(String name) {
    super(name);
    playerInfoHandler = new PlayerInfoHandler(AdvancedBan.mysql.connection);
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (sender instanceof ProxiedPlayer p) {
      if (p.hasPermission("ab.ban") || p.hasPermission("ab.tempban")) {
        boolean enabled =
            playerInfoHandler.hasNotify(
                p.getUniqueId().toString(), PlayerInfoHandler.Notification.BAN);
        playerInfoHandler.updateNotify(
            p.getUniqueId().toString(), PlayerInfoHandler.Notification.BAN, !enabled);
        if (enabled) {
          p.sendMessage(
              new TextComponent(
                  AdvancedBan.prefix + AdvancedBan.messages.get("toggle.ban.disabled")));
        } else {
          p.sendMessage(
              new TextComponent(
                  AdvancedBan.prefix + AdvancedBan.messages.get("toggle.ban.enabled")));
        }
      } else {
        sender.sendMessage(new TextComponent((String) AdvancedBan.messages.get("no_permission")));
      }
    }
  }
}
