package de.ab.delta203.bungee.modules.report.commands;

import de.ab.delta203.core.AdvancedBan;
import de.ab.delta203.core.modules.report.mysql.ReportHandler;
import de.ab.delta203.core.mysql.PlayerInfoHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class ReportCommand extends Command implements TabExecutor {

  private final ReportHandler reportHandler;
  private final PlayerInfoHandler playerInfoHandler;
  private final HashMap<CommandSender, Object[]> confirmations;

  public ReportCommand(String name) {
    super(name);
    reportHandler = new ReportHandler(AdvancedBan.mysql.connection);
    playerInfoHandler = new PlayerInfoHandler(AdvancedBan.mysql.connection);
    confirmations = new HashMap<>();
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (args.length == 1) {
      // confirmation
      if (args[0].equals("$confirm")) {
        if (!confirmations.containsKey(sender)) {
          sender.sendMessage(
              new TextComponent(
                  AdvancedBan.prefix + AdvancedBan.messages.get("report.cant_be_confirmed")));
          return;
        }
        ProxiedPlayer target = (ProxiedPlayer) confirmations.get(sender)[0];
        if (target.hasPermission("ab.cantbereported")) {
          sender.sendMessage(
              new TextComponent(
                  AdvancedBan.prefix
                      + ((String) AdvancedBan.messages.get("report.cant_be_reported"))
                          .replace("%player%", target.getName())));
          return;
        }
        String fromUUID = (String) AdvancedBan.config.get("console");
        String reason = (String) confirmations.get(sender)[1];
        if (sender instanceof ProxiedPlayer p) {
          fromUUID = p.getUniqueId().toString();
        }
        sender.sendMessage(
            new TextComponent(
                AdvancedBan.prefix
                    + ((String) AdvancedBan.messages.get("report.confirmed"))
                        .replace("%player%", target.getName())));
        reportHandler.report(
            target.getUniqueId().toString(),
            fromUUID,
            target.getServer().getInfo().getName(),
            reason);
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
                    .replace("%server%", target.getServer().getInfo().getName()));
        textComponentServer.setHoverEvent(
            new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new Text((String) AdvancedBan.messages.get("report.notification.new.hover"))));
        textComponentServer.setClickEvent(
            new ClickEvent(
                ClickEvent.Action.RUN_COMMAND,
                "/server " + target.getServer().getInfo().getName()));
        textComponentContent.addExtra(textComponentServer);
        TextComponent textComponentReports =
            new TextComponent(
                AdvancedBan.prefix
                    + ((String) AdvancedBan.messages.get("report.notification.open"))
                        .replace("%reports%", String.valueOf(reports)));
        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
          if (all.hasPermission("ab.panel")) {
            if (playerInfoHandler.hasNotify(
                all.getUniqueId().toString(), PlayerInfoHandler.Notification.REPORT)) {
              all.sendMessage(textComponentContent);
              all.sendMessage(textComponentReports);
            }
          }
        }
        return;
      }
      // report a player
      ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
      if (!validate(sender, args[0])) return;
      // valid
      sender.sendMessage(
          new TextComponent(
              AdvancedBan.prefix
                  + ((String) AdvancedBan.messages.get("report.reporting.title"))
                      .replace("%player%", target.getName())));
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
      sender.sendMessage(reasons);
    } else if (args.length == 2) {
      ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
      if (!validate(sender, args[0])) return;
      String reason = args[1];
      if (!((List<?>) AdvancedBan.messages.get("report.reasons")).contains(reason)) {
        sender.sendMessage(
            new TextComponent(
                AdvancedBan.prefix + AdvancedBan.messages.get("report.invalid_reason")));
        return;
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
      sender.sendMessage(confirm);
      confirmations.put(sender, new Object[] {target, reason});
    } else {
      sender.sendMessage(
          new TextComponent(AdvancedBan.prefix + AdvancedBan.messages.get("report.help")));
    }
  }

  private boolean validate(CommandSender sender, String targetName) {
    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetName);
    if (target == null) {
      sender.sendMessage(
          new TextComponent(
              AdvancedBan.prefix
                  + ((String) AdvancedBan.messages.get("not_online"))
                      .replace("%player%", targetName)));
      return false;
    }
    if (!(boolean) AdvancedBan.config.get("self")) {
      if (target == sender) {
        sender.sendMessage(
            new TextComponent(
                AdvancedBan.prefix + AdvancedBan.messages.get("report.not_yourself")));
        return false;
      }
    }
    if (target.hasPermission("ab.cantbereported")) {
      sender.sendMessage(
          new TextComponent(
              AdvancedBan.prefix
                  + ((String) AdvancedBan.messages.get("report.cant_be_reported"))
                      .replace("%player%", target.getName())));
      return false;
    }
    String senderUUID = (String) AdvancedBan.config.get("console");
    if (sender instanceof ProxiedPlayer p) {
      senderUUID = p.getUniqueId().toString();
    }
    if (reportHandler.alreadyReported(target.getUniqueId().toString(), senderUUID)) {
      sender.sendMessage(
          new TextComponent(
              AdvancedBan.prefix
                  + ((String) AdvancedBan.messages.get("report.already_reported"))
                      .replace("%player%", target.getName())));
      return false;
    }
    return true;
  }

  @Override
  public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
    if (args.length <= 1) {
      if (!(sender instanceof ProxiedPlayer p)) return new ArrayList<>();
      ArrayList<String> locals = new ArrayList<>();
      for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
        if (all.getServer().getInfo().getName().equals(p.getServer().getInfo().getName())) {
          locals.add(all.getName());
        }
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
