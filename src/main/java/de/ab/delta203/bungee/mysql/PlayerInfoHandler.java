package de.ab.delta203.bungee.mysql;

import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerInfoHandler {

  private final Connection connection;

  public PlayerInfoHandler(Connection connection) {
    this.connection = connection;
  }

  public boolean registered(PendingConnection pc) {
    return getName(pc.getUniqueId().toString()) != null;
  }

  public void register(PendingConnection pc) {
    String uuid = pc.getUniqueId().toString();
    String name = pc.getName();
    try {
      PreparedStatement ps =
          connection.prepareStatement(
              "INSERT INTO AB_PlayerInfo (PlayerUUID, PlayerName, Server, LoginKey, Notify_Ban, "
                  + "Notify_Mute, Notify_Report) VALUES (?,?,?,?,?,?,?)");
      ps.setString(1, uuid);
      ps.setString(2, name);
      ps.setString(3, "-");
      ps.setString(4, "-");
      ps.setBoolean(5, true);
      ps.setBoolean(6, true);
      ps.setBoolean(7, true);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public String getUUID(String name) {
    try {
      PreparedStatement ps =
          connection.prepareStatement("SELECT PlayerUUID FROM AB_PlayerInfo WHERE PlayerName = ?");
      ps.setString(1, name);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) return rs.getString("PlayerUUID");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public String getName(String uuid) {
    try {
      PreparedStatement ps =
          connection.prepareStatement("SELECT PlayerName FROM AB_PlayerInfo WHERE PlayerUUID = ?");
      ps.setString(1, uuid);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) return rs.getString("PlayerName");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void updateName(String uuid, String name) {
    try {
      PreparedStatement ps =
          connection.prepareStatement(
              "UPDATE AB_PlayerInfo SET PlayerName = ? WHERE PlayerUUID = ?");
      ps.setString(1, name);
      ps.setString(2, uuid);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public String getServer(String uuid) {
    try {
      PreparedStatement ps =
          connection.prepareStatement("SELECT Server FROM AB_PlayerInfo WHERE PlayerUUID = ?");
      ps.setString(1, uuid);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) return rs.getString("Server");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void updateServer(ProxiedPlayer p) {
    String uuid = p.getUniqueId().toString();
    String server = p.getServer().getInfo().getName();
    try {
      PreparedStatement ps =
          connection.prepareStatement("UPDATE AB_PlayerInfo SET Server = ? WHERE PlayerUUID = ?");
      ps.setString(1, server);
      ps.setString(2, uuid);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void removeServer(ProxiedPlayer p) {
    String uuid = p.getUniqueId().toString();
    try {
      PreparedStatement ps =
          connection.prepareStatement("UPDATE AB_PlayerInfo SET Server = ? WHERE PlayerUUID = ?");
      ps.setString(1, "-");
      ps.setString(2, uuid);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void resetServers() {
    try {
      PreparedStatement ps =
          connection.prepareStatement("UPDATE AB_PlayerInfo SET Server = ? WHERE 1");
      ps.setString(1, "-");
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public String getLoginKey(String uuid) {
    try {
      PreparedStatement ps =
          connection.prepareStatement("SELECT LoginKey FROM AB_PlayerInfo WHERE PlayerUUID = ?");
      ps.setString(1, uuid);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) return rs.getString("LoginKey");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void updateLoginKey(PendingConnection pc) {
    String uuid = pc.getUniqueId().toString();
    String name = pc.getName();
    long millis = System.currentTimeMillis();
    String key = uuid + name + millis;
    key += new StringBuilder(key).reverse().toString();
    key = md5(key);
    try {
      PreparedStatement ps =
          connection.prepareStatement("UPDATE AB_PlayerInfo SET LoginKey = ? WHERE PlayerUUID = ?");
      ps.setString(1, key);
      ps.setString(2, uuid);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void removeLoginKey(ProxiedPlayer p) {
    String uuid = p.getUniqueId().toString();
    try {
      PreparedStatement ps =
          connection.prepareStatement("UPDATE AB_PlayerInfo SET LoginKey = ? WHERE PlayerUUID = ?");
      ps.setString(1, "-");
      ps.setString(2, uuid);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void resetLoginKeys() {
    try {
      PreparedStatement ps =
          connection.prepareStatement("UPDATE AB_PlayerInfo SET LoginKey = ? WHERE 1");
      ps.setString(1, "-");
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private String md5(String s) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] sD = md.digest(s.getBytes());
      StringBuilder hexString = new StringBuilder();
      for (byte b : sD) {
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1) {
          hexString.append("0");
        }
        hexString.append(hex);
      }
      return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return null;
  }

  public boolean hasNotify(ProxiedPlayer p, Notification notification) {
    String uuid = p.getUniqueId().toString();
    try {
      PreparedStatement ps =
          connection.prepareStatement(
              "SELECT " + notification.getValue() + " FROM AB_PlayerInfo WHERE PlayerUUID = ?");
      ps.setString(1, uuid);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) return rs.getBoolean(notification.getValue());
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public void updateNotify(ProxiedPlayer p, Notification notification, boolean notify) {
    String uuid = p.getUniqueId().toString();
    try {
      PreparedStatement ps =
          connection.prepareStatement(
              "UPDATE AB_PlayerInfo SET " + notification.getValue() + " = ? WHERE PlayerUUID = ?");
      ps.setBoolean(1, notify);
      ps.setString(2, uuid);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public enum Notification {
    BAN("Notify_Ban"),
    MUTE("Notify_Mute"),
    REPORT("Notify_Report");

    private final String value;

    Notification(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }
}
