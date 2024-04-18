package de.ab.delta203.core.modules.report.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportHandler {

  private final Connection connection;

  public ReportHandler(Connection connection) {
    this.connection = connection;
  }

  public boolean alreadyReported(String uuid, String from) {
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

  public int getOpenReports() {
    int result = 0;
    try {
      PreparedStatement ps =
          connection.prepareStatement("SELECT PlayerUUID FROM AB_Reports WHERE InProgress = ?");
      ps.setBoolean(1, false);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        result++;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }

  public void report(String uuid, String from, String server, String reason) {
    long millis = System.currentTimeMillis();
    try {
      PreparedStatement ps =
          connection.prepareStatement(
              "INSERT INTO AB_Reports (CurrentMillis, PlayerUUID, FromUUID, Server, Reason, InProgress) VALUES (?,?,?,?,?,?)");
      ps.setLong(1, millis);
      ps.setString(2, uuid);
      ps.setString(3, from);
      ps.setString(4, server);
      ps.setString(5, reason);
      ps.setBoolean(6, false);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
