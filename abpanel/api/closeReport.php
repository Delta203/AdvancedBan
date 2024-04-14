<?php
  if (!isset($_GET['uuid'])) header("Location: " . $root);
  $uuid = htmlspecialchars($_GET['uuid']);
  $sql = "DELETE FROM AB_Reports WHERE PlayerUUID = '$uuid'";
  $connection->query($sql);
  header("Location: " . $root . "/?success");
?>
