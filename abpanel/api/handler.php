<?php
function isSessionValid() {
  global $connection, $admin_enabled, $admin_uuid, $admin_key;
  if (!isset($_SESSION["user"])) return false;
  if (!str_contains($_SESSION["user"], "|")) return false;
  $session = explode("|", $_SESSION["user"]);
  $uuid = $session[0];
  $key = $session[1];
  if ($key == "-") return false;
  // admin account
  if ($admin_enabled) {
    if ($uuid == $admin_uuid && $key == $admin_key) {
      return true;
    }
  }
  // uuid and login key
  $sql = "SELECT PlayerUUID FROM AB_PlayerInfo WHERE LoginKey = '$key'";
  $result = $connection->query($sql);
  if ($row = $result->fetch_assoc()) {
    if ($row["PlayerUUID"] == $uuid) return true;
  }
  return false;
}

function millisecondsToDate($millis) {
  return date("d/m/Y H:i:s", $millis/1000);
}

function getRegisterMillis($uuid) {
  global $connection;
  $sql = "SELECT CurrentMillis FROM AB_PlayerInfo WHERE PlayerUUID = '$uuid'";
  $result = $connection->query($sql);
  if ($row = $result->fetch_assoc()) return $row["CurrentMillis"];
  return 0;
}

function getUUID($name) {
  global $connection;
  $sql = "SELECT PlayerUUID FROM AB_PlayerInfo WHERE PlayerName = '$name'";
  $result = $connection->query($sql);
  if ($row = $result->fetch_assoc()) return $row["PlayerUUID"];
  return "-";
}

function getName($uuid) {
  global $connection;
  $sql = "SELECT PlayerName FROM AB_PlayerInfo WHERE PlayerUUID = '$uuid'";
  $result = $connection->query($sql);
  if ($row = $result->fetch_assoc()) return $row["PlayerName"];
  return "-";
}

function getServer($uuid) {
  global $connection;
  $sql = "SELECT Server FROM AB_PlayerInfo WHERE PlayerUUID = '$uuid'";
  $result = $connection->query($sql);
  if ($row = $result->fetch_assoc()) return $row["Server"];
  return "-";
}

function getHistoryCount($uuid) {
  global $connection;
  $count = 0;
  $sql = "SELECT * FROM AB_PlayerHistory WHERE PlayerUUID = '$uuid'";
  $result = $connection->query($sql);
  while ($row = $result->fetch_assoc()) $count++;
  return $count;
}

function isBanned($uuid) {
  global $connection;
  $sql = "SELECT FromUUID FROM AB_Bans WHERE PlayerUUID = '$uuid'";
  $result = $connection->query($sql);
  if ($row = $result->fetch_assoc()) return true;
  return false;
}

function getBansCount($uuid) {
  global $connection;
  $count = 0;
  $sql = "SELECT * FROM AB_PlayerHistory WHERE PlayerUUID = '$uuid' AND (Type = 'ban' OR Type = 'tempban')";
  $result = $connection->query($sql);
  while ($row = $result->fetch_assoc()) $count++;
  return $count;
}

function getGlobalBansCount() {
  global $connection;
  $count = 0;
  $sql = "SELECT * FROM AB_Bans";
  $result = $connection->query($sql);
  while ($row = $result->fetch_assoc()) $count++;
  return $count;
}

function isMuted($uuid) {
  global $connection;
  $sql = "SELECT FromUUID FROM AB_Mutes WHERE PlayerUUID = '$uuid'";
  $result = $connection->query($sql);
  if ($row = $result->fetch_assoc()) return true;
  return false;
}

function getMutesCount($uuid) {
  global $connection;
  $count = 0;
  $sql = "SELECT * FROM AB_PlayerHistory WHERE PlayerUUID = '$uuid' AND (Type = 'mute' OR Type = 'tempmute')";
  $result = $connection->query($sql);
  while ($row = $result->fetch_assoc()) $count++;
  return $count;
}

function getGlobaMutesCount() {
  global $connection;
  $count = 0;
  $sql = "SELECT * FROM AB_Mutes";
  $result = $connection->query($sql);
  while ($row = $result->fetch_assoc()) $count++;
  return $count;
}

function getReportsCount($uuid) {
  global $connection;
  $count = 0;
  $sql = "SELECT * FROM AB_Reports WHERE PlayerUUID = '$uuid'";
  $result = $connection->query($sql);
  while ($row = $result->fetch_assoc()) $count++;
  return $count;
}

function getGlobalReportsCount() {
  global $connection;
  $count = 0;
  $sql = "SELECT * FROM AB_Reports WHERE InProgress = '0'";
  $result = $connection->query($sql);
  while ($row = $result->fetch_assoc()) $count++;
  return $count;
}

function fetchRandomReport() {
  global $connection;
  $uuid = null;
  $sql = "SELECT PlayerUUID FROM AB_Reports WHERE InProgress = 'false' ORDER BY RAND() LIMIT 1";
  $result = $connection->query($sql);
  if ($row = $result->fetch_assoc()) $uuid = $row["PlayerUUID"];
  $sql = "UPDATE AB_Reports SET InProgress = '1' WHERE PlayerUUID = '$uuid'";
  $connection->query($sql);
  return $uuid;
}

function getLatestReport($uuid) {
  global $connection;
  $sql = "SELECT * FROM AB_Reports WHERE PlayerUUID = '$uuid' ORDER BY CurrentMillis DESC";
  $result = $connection->query($sql);
  if ($row = $result->fetch_assoc()) return $row;
  return null;
}
?>
