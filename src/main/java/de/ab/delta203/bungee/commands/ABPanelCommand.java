package de.ab.delta203.bungee.commands;

import de.ab.delta203.core.AdvancedBan;
import de.ab.delta203.core.mysql.PlayerInfoHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ABPanelCommand extends Command {

  private final PlayerInfoHandler playerInfoHandler;

  public ABPanelCommand(String name) {
    super(name);
    playerInfoHandler = new PlayerInfoHandler(AdvancedBan.mysql.connection);
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (sender instanceof ProxiedPlayer p) {
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
        p.sendMessage(textComponent);
      } else {
        sender.sendMessage(new TextComponent((String) AdvancedBan.messages.get("no_permission")));
      }
    }
  }
}
