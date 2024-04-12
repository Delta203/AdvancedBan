package de.ab.delta203.bungee.commands;

import de.ab.delta203.bungee.AdvancedBan;
import de.ab.delta203.bungee.mysql.PlayerInfoHandler;
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
        String link = AdvancedBan.config.getString("link").replace("%key%", key);
        TextComponent textComponent =
            new TextComponent(AdvancedBan.prefix + AdvancedBan.messages.getString("abpanel.title"));
        textComponent.setHoverEvent(
            new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new Text(AdvancedBan.messages.getString("abpanel.hover"))));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link));
        p.sendMessage(textComponent);
      } else {
        sender.sendMessage(new TextComponent(AdvancedBan.messages.getString("no_permission")));
      }
    }
  }
}
