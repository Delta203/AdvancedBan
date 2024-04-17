package de.ab.delta203.spigot.commands;

import de.ab.delta203.core.AdvancedBan;
import de.ab.delta203.core.mysql.PlayerInfoHandler;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ABPanelCommand implements CommandExecutor {

  private final PlayerInfoHandler playerInfoHandler;

  public ABPanelCommand() {
    playerInfoHandler = new PlayerInfoHandler(AdvancedBan.mysql.connection);
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (sender instanceof Player p) {
      if (p.hasPermission("ab.panel")) {
        String key = playerInfoHandler.getLoginKey(p.getUniqueId().toString());
        String link =
            ((String) AdvancedBan.config.get("link"))
                .replace("%uuid%", p.getUniqueId().toString())
                .replace("%key%", key);
        TextComponent textComponent =
            new TextComponent(AdvancedBan.prefix + AdvancedBan.messages.get("abpanel.title"));
        textComponent.setHoverEvent(
            new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new Text((String) AdvancedBan.messages.get("abpanel.hover"))));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link));
        p.spigot().sendMessage(textComponent);
      } else {
        sender.sendMessage((String) AdvancedBan.messages.get("no_permission"));
      }
    }
    return false;
  }
}
