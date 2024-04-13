<?php
function isSessionValid() {
  global $connection;
  if (!isset($_SESSION["user"])) return false;
  if (!str_contains($_SESSION["user"], "|")) return false;
  $session = explode("|", $_SESSION["user"]);
  $uuid = $session[0];
  $key = $session[1];
  $sql = "SELECT PlayerUUID FROM AB_PlayerInfo WHERE LoginKey = '$key'";
  $result = $connection->query($sql);
  if ($row = $result->fetch_assoc()) {
    if ($row["PlayerUUID"] == $uuid) return true;
  }
  return false;
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

function isBanned($uuid) {
  global $connection;
  $sql = "SELECT FromUUID FROM AB_Bans WHERE PlayerUUID = '$uuid'";
  $result = $connection->query($sql);
  if ($row = $result->fetch_assoc()) return true;
  return false;
}

function isMuted($uuid) {
  global $connection;
  $sql = "SELECT FromUUID FROM AB_Mutes WHERE PlayerUUID = '$uuid'";
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

function getMutesCount($uuid) {
  global $connection;
  $count = 0;
  $sql = "SELECT * FROM AB_PlayerHistory WHERE PlayerUUID = '$uuid' AND (Type = 'mute' OR Type = 'tempmute')";
  $result = $connection->query($sql);
  while ($row = $result->fetch_assoc()) $count++;
  return $count;
}

function getReporsCount() {
  global $connection;
  $count = 0;
  $sql = "SELECT * FROM AB_Reports WHERE 1";
  $result = $connection->query($sql);
  while ($row = $result->fetch_assoc()) $count++;
  return $count;
}

function millisecondsToDate($millis) {
  return date("d/m/Y H:i:s", $millis/1000);
}
?>
