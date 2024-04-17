package de.ab.delta203.spigot.modules.check.commands;

import de.ab.delta203.core.AdvancedBan;
import de.ab.delta203.core.modules.ban.mysql.BanHandler;
import de.ab.delta203.core.modules.mute.mysql.MuteHandler;
import de.ab.delta203.core.mysql.PlayerInfoHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class CheckCommand implements CommandExecutor {

  private final PlayerInfoHandler playerInfoHandler;
  private final BanHandler banHandler;
  private final MuteHandler muteHandler;

  public CheckCommand() {
    playerInfoHandler = new PlayerInfoHandler(AdvancedBan.mysql.connection);
    banHandler = new BanHandler(AdvancedBan.mysql.connection);
    muteHandler = new MuteHandler(AdvancedBan.mysql.connection);
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (!sender.hasPermission("ab.check")) {
      sender.sendMessage((String) AdvancedBan.messages.get("no_permission"));
      return false;
    }
    if (args.length == 1) {
      String name = args[0];
      String uuidCached = playerInfoHandler.getUUID(name);
      String uuid = uuidCached == null ? "-" : uuidCached;
      String ip = "-";
      String server = "-";
      boolean banned = banHandler.isBanned(uuid);
      boolean muted = muteHandler.isMuted(uuid);
      Player target = Bukkit.getPlayer(name);
      if (target != null) {
        uuid = target.getUniqueId().toString();
        ip = Objects.requireNonNull(target.getAddress()).getAddress().toString();
        server = target.getWorld().getName();
      }
      String prefixTrue = (String) AdvancedBan.messages.get("check.prefix.true");
      String prefixFalse = (String) AdvancedBan.messages.get("check.prefix.false");
      sender.sendMessage(
          AdvancedBan.prefix
              + ((String) AdvancedBan.messages.get("check.title")).replace("%player%", name));
      sender.sendMessage(
          AdvancedBan.prefix
              + ((String) AdvancedBan.messages.get("check.uuid")).replace("%uuid%", uuid));
      sender.sendMessage(
          AdvancedBan.prefix + ((String) AdvancedBan.messages.get("check.ip")).replace("%ip%", ip));
      sender.sendMessage(
          AdvancedBan.prefix
              + ((String) AdvancedBan.messages.get("check.server")).replace("%server%", server));
      sender.sendMessage(
          AdvancedBan.prefix
              + ((String) AdvancedBan.messages.get("check.banned"))
                  .replace("%banned%", (banned ? prefixTrue : prefixFalse) + banned));
      sender.sendMessage(
          AdvancedBan.prefix
              + ((String) AdvancedBan.messages.get("check.muted"))
                  .replace("%muted%", (muted ? prefixTrue : prefixFalse) + muted));
    } else {
      sender.sendMessage(AdvancedBan.prefix + AdvancedBan.messages.get("check.help"));
    }
    return false;
  }
}
