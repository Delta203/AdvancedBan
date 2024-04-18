package de.ab.delta203.core.modules.ban.mysql;

import de.ab.delta203.core.AdvancedBan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BanHandler {

  private final Connection connection;

  public BanHandler(Connection connection) {
    this.connection = connection;
  }

  public boolean isBanned(String uuid) {
    try {
      PreparedStatement ps =
          connection.prepareStatement("SELECT FromUUID FROM AB_Bans WHERE PlayerUUID = ?");
      ps.setString(1, uuid);
      ResultSet rs = ps.executeQuery();
      return rs.next();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public boolean isBannedIp(String ip) {
    try {
      PreparedStatement ps =
          connection.prepareStatement("SELECT PlayerUUID FROM AB_Bans WHERE PlayerIp = ?");
      ps.setString(1, ip);
      ResultSet rs = ps.executeQuery();
      return rs.next();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public long getEnd(String uuid) {
    try {
      PreparedStatement ps =
          connection.prepareStatement("SELECT End FROM AB_Bans WHERE PlayerUUID = ?");
      ps.setString(1, uuid);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) return rs.getLong("End");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return -1;
  }

  public String getDuration(String uuid) {
    long current = System.currentTimeMillis();
    long end = getEnd(uuid);
    if (end == -1) return (String) AdvancedBan.messages.get("unit.permanent.name");
    long millis = end - current;
    long seconds = 0;
    long minutes = 0;
    long hours = 0;
    long days = 0;
    while (millis > 1000) {
      millis -= 1000;
      seconds++;
    }
    while (seconds > 60) {
      seconds -= 60;
      minutes++;
    }
    while (minutes > 60) {
      minutes -= 60;
      hours++;
    }
    while (hours > 24) {
      hours -= 24;
      days++;
    }
    return days
        + " "
        + AdvancedBan.messages.get("unit.days.name")
        + " "
        + hours
        + " "
        + AdvancedBan.messages.get("unit.hours.name")
        + " "
        + minutes
        + " "
        + AdvancedBan.messages.get("unit.minutes.name")
        + " "
        + seconds
        + " "
        + AdvancedBan.messages.get("unit.seconds.name");
  }

  public String getReason(String uuid) {
    try {
      PreparedStatement ps =
          connection.prepareStatement("SELECT Reason FROM AB_Bans WHERE PlayerUUID = ?");
      ps.setString(1, uuid);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) return rs.getString("Reason");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void ban(String uuid, String ip, String from, long end, String reason) {
    try {
      PreparedStatement ps =
          connection.prepareStatement(
              "INSERT INTO AB_Bans (PlayerUUID, PlayerIp, FromUUID, End, Reason) VALUES (?,?,?,?,?)");
      ps.setString(1, uuid);
      ps.setString(2, ip);
      ps.setString(3, from);
      ps.setLong(4, end);
      ps.setString(5, reason);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void unban(String uuid) {
    try {
      PreparedStatement ps =
          connection.prepareStatement("DELETE FROM AB_Bans WHERE PlayerUUID = ?");
      ps.setString(1, uuid);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void log(String uuid, String from, String type, String duration, String reason) {
    long current = System.currentTimeMillis();
    try {
      PreparedStatement ps =
          connection.prepareStatement(
              "INSERT INTO AB_PlayerHistory (CurrentMillis, PlayerUUID, FromUUID, Type, Duration, Reason) VALUES (?,?,?,?,?,?)");
      ps.setLong(1, current);
      ps.setString(2, uuid);
      ps.setString(3, from);
      ps.setString(4, type);
      ps.setString(5, duration);
      ps.setString(6, reason);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
