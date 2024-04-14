<?php
  $uuid = fetchRandomReport();
  if ($uuid == null) {
    header("Location: " . $root);
  } else {
    header("Location: " . $root . "/?p=report&uuid=" . $uuid);
  }
?>
