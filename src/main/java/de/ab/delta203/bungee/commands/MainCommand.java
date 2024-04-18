package de.ab.delta203.bungee.commands;

import de.ab.delta203.core.AdvancedBan;
import java.util.ArrayList;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MainCommand extends Command {

  public MainCommand(String name) {
    super(name);
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    ArrayList<String> help = new ArrayList<>();
    if (sender.hasPermission("ab.ban")) help.add((String) AdvancedBan.messages.get("help.ban"));
    if (sender.hasPermission("ab.tempban"))
      help.add((String) AdvancedBan.messages.get("help.tempban"));
    if (sender.hasPermission("ab.unban")) help.add((String) AdvancedBan.messages.get("help.unban"));
    if (sender.hasPermission("ab.mute")) help.add((String) AdvancedBan.messages.get("help.mute"));
    if (sender.hasPermission("ab.tempmute"))
      help.add((String) AdvancedBan.messages.get("help.tempmute"));
    if (sender.hasPermission("ab.unmute"))
      help.add((String) AdvancedBan.messages.get("help.unmute"));
    if (sender.hasPermission("ab.check")) help.add((String) AdvancedBan.messages.get("help.check"));
    if (sender.hasPermission("ab.panel")) help.add((String) AdvancedBan.messages.get("help.panel"));
    if (!help.isEmpty()) {
      sender.sendMessage(
          new TextComponent(AdvancedBan.prefix + AdvancedBan.messages.get("help.title")));
      for (String s : help) {
        sender.sendMessage(new TextComponent(s));
      }
      if (sender instanceof ProxiedPlayer) {
        sender.sendMessage(new TextComponent((String) AdvancedBan.messages.get("help.notify.ban")));
        sender.sendMessage(
            new TextComponent((String) AdvancedBan.messages.get("help.notify.mute")));
        sender.sendMessage(
            new TextComponent((String) AdvancedBan.messages.get("help.notify.report")));
      }
    } else {
      sender.sendMessage(new TextComponent((String) AdvancedBan.messages.get("no_permission")));
    }
  }
}
