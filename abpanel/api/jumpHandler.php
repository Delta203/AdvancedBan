<?php
  if (!isset($_GET['report'])) header("Location: " . $root);
  if (!isset($_GET['server'])) header("Location: " . $root);
  $report = htmlspecialchars($_GET['report']);
  $server = htmlspecialchars($_GET['server']);
  $sql = "INSERT INTO AB_CommandQuery (SenderUUID, Command) VALUES ('$session_uuid', 'server $server')";
  $connection->query($sql);
  header("Location: " . $root . "/?p=report&uuid=" . $report . "&jumped");
?>
