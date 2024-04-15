package de.ab.delta203.bungee.modules.report.commands;

import de.ab.delta203.bungee.AdvancedBan;
import java.util.ArrayList;
import java.util.HashMap;

import de.ab.delta203.bungee.modules.report.mysql.ReportHandler;
import de.ab.delta203.bungee.mysql.PlayerInfoHandler;
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
                  AdvancedBan.prefix + AdvancedBan.messages.getString("report.cant_be_confirmed")));
          return;
        }
        ProxiedPlayer target = (ProxiedPlayer) confirmations.get(sender)[0];
        if (target.hasPermission("ab.cantbereported")) {
          sender.sendMessage(
              new TextComponent(
                  AdvancedBan.prefix
                      + AdvancedBan.messages
                          .getString("report.cant_be_reported")
                          .replace("%player%", target.getName())));
          return;
        }
        String fromUUID = AdvancedBan.config.getString("console");
        String reason = (String) confirmations.get(sender)[1];
        if (sender instanceof ProxiedPlayer p) {
          fromUUID = p.getUniqueId().toString();
        }
        sender.sendMessage(
            new TextComponent(
                AdvancedBan.prefix
                    + AdvancedBan.messages
                        .getString("report.confirmed")
                        .replace("%player%", target.getName())));
        reportHandler.report(target, fromUUID, reason);
        confirmations.remove(sender);
        // broadcast
        int reports = reportHandler.getOpenReports();
        TextComponent textComponentContent =
            new TextComponent(
                AdvancedBan.prefix
                    + AdvancedBan.messages
                        .getString("report.notification.new.title")
                        .replace("%player%", target.getName())
                        .replace("%reason%", reason)
                        .replace("\\n", "\n"));
        TextComponent textComponentServer =
            new TextComponent(
                AdvancedBan.messages
                    .getString("report.notification.new.server")
                    .replace("%server%", target.getServer().getInfo().getName()));
        textComponentServer.setHoverEvent(
            new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new Text(AdvancedBan.messages.getString("report.notification.new.hover"))));
        textComponentServer.setClickEvent(
            new ClickEvent(
                ClickEvent.Action.RUN_COMMAND,
                "/server " + target.getServer().getInfo().getName()));
        textComponentContent.addExtra(textComponentServer);
        TextComponent textComponentReports =
            new TextComponent(
                AdvancedBan.prefix
                    + AdvancedBan.messages
                        .getString("report.notification.open")
                        .replace("%reports%", String.valueOf(reports)));
        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
          if (all.hasPermission("ab.panel")) {
            if (playerInfoHandler.hasNotify(all, PlayerInfoHandler.Notification.REPORT)) {
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
                  + AdvancedBan.messages
                      .getString("report.reporting.title")
                      .replace("%player%", target.getName())));
      TextComponent reasons = new TextComponent();
      for (String reason : AdvancedBan.messages.getStringList("report.reasons")) {
        TextComponent tc =
            new TextComponent(
                AdvancedBan.messages
                    .getString("report.reporting.reason")
                    .replace("%reason%", reason));
        tc.setClickEvent(
            new ClickEvent(
                ClickEvent.Action.RUN_COMMAND, "/report " + target.getName() + " " + reason));
        tc.setHoverEvent(
            new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new Text(
                    AdvancedBan.messages
                        .getString("report.reporting.hover")
                        .replace("%player%", target.getName()))));
        reasons.addExtra(tc);
      }
      sender.sendMessage(reasons);
    } else if (args.length == 2) {
      ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
      if (!validate(sender, args[0])) return;
      String reason = args[1];
      if (!AdvancedBan.messages.getStringList("report.reasons").contains(reason)) {
        sender.sendMessage(
            new TextComponent(
                AdvancedBan.prefix + AdvancedBan.messages.getString("report.invalid_reason")));
        return;
      }
      // valid
      TextComponent confirm =
          new TextComponent(
              AdvancedBan.prefix
                  + AdvancedBan.messages
                      .getString("report.confirm.info")
                      .replace("%player%", target.getName())
                      .replace("%reason%", reason));
      TextComponent tc =
          new TextComponent(AdvancedBan.messages.getString("report.confirm.confirm"));
      tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/report $confirm"));
      tc.setHoverEvent(
          new HoverEvent(
              HoverEvent.Action.SHOW_TEXT,
              new Text(
                  AdvancedBan.messages
                      .getString("report.confirm.hover")
                      .replace("%player%", target.getName()))));
      confirm.addExtra(tc);
      sender.sendMessage(confirm);
      confirmations.put(sender, new Object[] {target, reason});
    } else {
      sender.sendMessage(
          new TextComponent(AdvancedBan.prefix + AdvancedBan.messages.getString("report.help")));
    }
  }

  private boolean validate(CommandSender sender, String targetName) {
    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetName);
    if (target == null) {
      sender.sendMessage(
          new TextComponent(
              AdvancedBan.prefix
                  + AdvancedBan.messages.getString("not_online").replace("%player%", targetName)));
      return false;
    }
    if (!AdvancedBan.config.getBoolean("self")) {
      if (target == sender) {
        sender.sendMessage(
            new TextComponent(
                AdvancedBan.prefix + AdvancedBan.messages.getString("report.not_yourself")));
        return false;
      }
    }
    if (target.hasPermission("ab.cantbereported")) {
      sender.sendMessage(
          new TextComponent(
              AdvancedBan.prefix
                  + AdvancedBan.messages
                      .getString("report.cant_be_reported")
                      .replace("%player%", target.getName())));
      return false;
    }
    String senderUUID = AdvancedBan.config.getString("console");
    if (sender instanceof ProxiedPlayer p) {
      senderUUID = p.getUniqueId().toString();
    }
    if (reportHandler.alreadyReported(target, senderUUID)) {
      sender.sendMessage(
          new TextComponent(
              AdvancedBan.prefix
                  + AdvancedBan.messages
                      .getString("report.already_reported")
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
      return new ArrayList<>(AdvancedBan.messages.getStringList("report.reasons"));
    }
    return new ArrayList<>();
  }
}
