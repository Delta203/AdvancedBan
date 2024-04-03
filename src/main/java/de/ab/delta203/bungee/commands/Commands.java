package de.ab.delta203.bungee.commands;

import de.ab.delta203.bungee.AdvancedBan;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;

public class Commands extends Command {

  public Commands(String name) {
    super(name);
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    ArrayList<String> help = new ArrayList<>();
    if (sender.hasPermission("ab.tempmute"))
      help.add(AdvancedBan.messages.getString("help.tempmute"));
    if (sender.hasPermission("ab.mute")) help.add(AdvancedBan.messages.getString("help.mute"));
    if (sender.hasPermission("ab.unmute")) help.add(AdvancedBan.messages.getString("help.unmute"));
    if (sender.hasPermission("ab.tempban"))
      help.add(AdvancedBan.messages.getString("help.tempban"));
    if (sender.hasPermission("ab.ban")) help.add(AdvancedBan.messages.getString("help.ban"));
    if (sender.hasPermission("ab.unban")) help.add(AdvancedBan.messages.getString("help.unban"));
    if (sender.hasPermission("ab.check")) help.add(AdvancedBan.messages.getString("help.check"));
    if (sender.hasPermission("ab.panel")) help.add(AdvancedBan.messages.getString("help.panel"));
    if (!help.isEmpty()) {
      sender.sendMessage(
          new TextComponent(AdvancedBan.prefix + AdvancedBan.messages.getString("help.title")));
      for (String s : help) {
        sender.sendMessage(new TextComponent(s));
      }
    } else {
      sender.sendMessage(new TextComponent(AdvancedBan.messages.getString("no_permission")));
    }
  }
}
