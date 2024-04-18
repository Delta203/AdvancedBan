package de.ab.delta203.spigot.commands;

import de.ab.delta203.core.AdvancedBan;
import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
      sender.sendMessage(AdvancedBan.prefix + AdvancedBan.messages.get("help.title"));
      for (String s : help) {
        sender.sendMessage(s);
      }
      if (sender instanceof Player) {
        sender.sendMessage((String) AdvancedBan.messages.get("help.notify.ban"));
        sender.sendMessage((String) AdvancedBan.messages.get("help.notify.mute"));
        sender.sendMessage((String) AdvancedBan.messages.get("help.notify.report"));
      }
    } else {
      sender.sendMessage((String) AdvancedBan.messages.get("no_permission"));
    }
    return false;
  }
}
