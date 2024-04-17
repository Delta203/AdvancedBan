package de.ab.delta203.spigot.modules.report.commands;

import de.ab.delta203.core.AdvancedBan;
import de.ab.delta203.core.modules.report.mysql.ReportHandler;
import de.ab.delta203.core.mysql.PlayerInfoHandler;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReportCommand implements TabExecutor {

  private final ReportHandler reportHandler;
  private final PlayerInfoHandler playerInfoHandler;
  private final HashMap<CommandSender, Object[]> confirmations;

  public ReportCommand() {
    reportHandler = new ReportHandler(AdvancedBan.mysql.connection);
    playerInfoHandler = new PlayerInfoHandler(AdvancedBan.mysql.connection);
    confirmations = new HashMap<>();
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (args.length == 1) {
      // confirmation
      if (args[0].equals("$confirm")) {
        if (!confirmations.containsKey(sender)) {
          sender.sendMessage(
              AdvancedBan.prefix + AdvancedBan.messages.get("report.cant_be_confirmed"));
          return false;
        }
        Player target = (Player) confirmations.get(sender)[0];
        if (target.hasPermission("ab.cantbereported")) {
          sender.sendMessage(
              AdvancedBan.prefix
                  + ((String) AdvancedBan.messages.get("report.cant_be_reported"))
                      .replace("%player%", target.getName()));
          return false;
        }
        String fromUUID = (String) AdvancedBan.config.get("console");
        String reason = (String) confirmations.get(sender)[1];
        if (sender instanceof Player p) {
          fromUUID = p.getUniqueId().toString();
        }
        sender.sendMessage(
            AdvancedBan.prefix
                + ((String) AdvancedBan.messages.get("report.confirmed"))
                    .replace("%player%", target.getName()));
        reportHandler.report(
            target.getUniqueId().toString(), fromUUID, target.getWorld().getName(), reason);
        confirmations.remove(sender);
        // broadcast
        int reports = reportHandler.getOpenReports();
        TextComponent textComponentContent =
            new TextComponent(
                AdvancedBan.prefix
                    + ((String) AdvancedBan.messages.get("report.notification.new.title"))
                        .replace("%player%", target.getName())
                        .replace("%reason%", reason)
                        .replace("\\n", "\n"));
        TextComponent textComponentServer =
            new TextComponent(
                ((String) AdvancedBan.messages.get("report.notification.new.server"))
                    .replace("%server%", target.getWorld().getName()));
        textComponentServer.setHoverEvent(
            new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new Text((String) AdvancedBan.messages.get("report.notification.new.hover"))));
        textComponentServer.setClickEvent(
            new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + target.getName()));
        textComponentContent.addExtra(textComponentServer);
        TextComponent textComponentReports =
            new TextComponent(
                AdvancedBan.prefix
                    + ((String) AdvancedBan.messages.get("report.notification.open"))
                        .replace("%reports%", String.valueOf(reports)));
        for (Player all : Bukkit.getOnlinePlayers()) {
          if (all.hasPermission("ab.panel")) {
            if (playerInfoHandler.hasNotify(
                all.getUniqueId().toString(), PlayerInfoHandler.Notification.REPORT)) {
              all.spigot().sendMessage(textComponentContent);
              all.spigot().sendMessage(textComponentReports);
            }
          }
        }
        return false;
      }
      // report a player
      Player target = Bukkit.getPlayer(args[0]);
      if (!validate(sender, args[0])) return false;
      // valid
      sender.sendMessage(
          AdvancedBan.prefix
              + ((String) AdvancedBan.messages.get("report.reporting.title"))
                  .replace("%player%", target.getName()));
      TextComponent reasons = new TextComponent();
      for (Object reason : (List<?>) AdvancedBan.messages.get("report.reasons")) {
        TextComponent tc =
            new TextComponent(
                ((String) AdvancedBan.messages.get("report.reporting.reason"))
                    .replace("%reason%", (String) reason));
        tc.setClickEvent(
            new ClickEvent(
                ClickEvent.Action.RUN_COMMAND, "/report " + target.getName() + " " + reason));
        tc.setHoverEvent(
            new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new Text(
                    ((String) AdvancedBan.messages.get("report.reporting.hover"))
                        .replace("%player%", target.getName()))));
        reasons.addExtra(tc);
      }
      sender.spigot().sendMessage(reasons);
    } else if (args.length == 2) {
      Player target = Bukkit.getPlayer(args[0]);
      if (!validate(sender, args[0])) return false;
      String reason = args[1];
      if (!((List<?>) AdvancedBan.messages.get("report.reasons")).contains(reason)) {
        sender.sendMessage(AdvancedBan.prefix + AdvancedBan.messages.get("report.invalid_reason"));
        return false;
      }
      // valid
      TextComponent confirm =
          new TextComponent(
              AdvancedBan.prefix
                  + ((String) AdvancedBan.messages.get("report.confirm.info"))
                      .replace("%player%", target.getName())
                      .replace("%reason%", reason));
      TextComponent tc =
          new TextComponent((String) AdvancedBan.messages.get("report.confirm.confirm"));
      tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/report $confirm"));
      tc.setHoverEvent(
          new HoverEvent(
              HoverEvent.Action.SHOW_TEXT,
              new Text(
                  ((String) AdvancedBan.messages.get("report.confirm.hover"))
                      .replace("%player%", target.getName()))));
      confirm.addExtra(tc);
      sender.spigot().sendMessage(confirm);
      confirmations.put(sender, new Object[] {target, reason});
    } else {
      sender.sendMessage(AdvancedBan.prefix + AdvancedBan.messages.get("report.help"));
    }
    return false;
  }

  private boolean validate(CommandSender sender, String targetName) {
    Player target = Bukkit.getPlayer(targetName);
    if (target == null) {
      sender.sendMessage(
          AdvancedBan.prefix
              + ((String) AdvancedBan.messages.get("not_online")).replace("%player%", targetName));
      return false;
    }
    if (!(boolean) AdvancedBan.config.get("self")) {
      if (target == sender) {
        sender.sendMessage(AdvancedBan.prefix + AdvancedBan.messages.get("report.not_yourself"));
        return false;
      }
    }
    if (target.hasPermission("ab.cantbereported")) {
      sender.sendMessage(
          AdvancedBan.prefix
              + ((String) AdvancedBan.messages.get("report.cant_be_reported"))
                  .replace("%player%", target.getName()));
      return false;
    }
    String senderUUID = (String) AdvancedBan.config.get("console");
    if (sender instanceof Player p) {
      senderUUID = p.getUniqueId().toString();
    }
    if (reportHandler.alreadyReported(target.getUniqueId().toString(), senderUUID)) {
      sender.sendMessage(
          AdvancedBan.prefix
              + ((String) AdvancedBan.messages.get("report.already_reported"))
                  .replace("%player%", target.getName()));
      return false;
    }
    return true;
  }

  public List<String> onTabComplete(
      CommandSender sender, Command cmd, String label, String[] args) {
    if (args.length <= 1) {
      if (!(sender instanceof Player)) return new ArrayList<>();
      ArrayList<String> locals = new ArrayList<>();
      for (Player all : Bukkit.getOnlinePlayers()) {
        locals.add(all.getName());
      }
      return locals;
    } else if (args.length == 2) {
      ArrayList<String> reasons = new ArrayList<>();
      for (Object reason : (List<?>) AdvancedBan.messages.get("report.reasons")) {
        reasons.add((String) reason);
      }
      return reasons;
    }
    return new ArrayList<>();
  }
}
