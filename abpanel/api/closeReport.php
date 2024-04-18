<?php
if (!isset($_GET["player"])) header("Location: " . $root);
$uuid = htmlspecialchars($_GET["player"]);
$sql = "DELETE FROM AB_Reports WHERE PlayerUUID = '$uuid'";
$connection->query($sql);
header("Location: " . $root . "/?success");
?>
