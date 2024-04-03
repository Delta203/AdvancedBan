package de.ab.delta203.bungee.modules.report.mysql;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportHandler {

  private final Connection connection;

  public ReportHandler(Connection connection) {
    this.connection = connection;
  }

  public boolean alreadyReported(ProxiedPlayer p, String from) {
    String uuid = p.getUniqueId().toString();
    try {
      PreparedStatement ps =
              connection.prepareStatement("SELECT PlayerUUID FROM AB_Reports WHERE FromUUID = ?");
      ps.setString(1, from);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) return rs.getString("PlayerUUID").equals(uuid);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public void report(ProxiedPlayer p, String from, String reason) {
    long millis = System.currentTimeMillis();
    String uuid = p.getUniqueId().toString();
    String server = p.getServer().getInfo().getName();
    try {
      PreparedStatement ps =
          connection.prepareStatement(
              "INSERT INTO AB_Reports (CurrentMillis, PlayerUUID, FromUUID, Server, Reason) VALUES (?,?,?,?,?)");
      ps.setLong(1, millis);
      ps.setString(2, uuid);
      ps.setString(3, from);
      ps.setString(4, server);
      ps.setString(5, reason);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public int getReports() {
    int result = 0;
    try {
      PreparedStatement ps = connection.prepareStatement("SELECT PlayerUUID FROM AB_Reports");
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        result++;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }
}
