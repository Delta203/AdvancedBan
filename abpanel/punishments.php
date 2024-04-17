<?php
$punishments_ban = array();
$punishments_mute = array();

# Punishments
# Note that an array key can only exist once.
#   $punishments_ban  | Ban reasons
#   $punishments_mute | Mute reasons
# Placeholders:
#   %player%    | Player name
$punishments_ban["Hacking"] = "ban %player% Hacking";
$punishments_ban["Bugusing"] = "tempban %player% 7 day Bugusing";
$punishments_ban["Teaming"] = "tempban %player% 1 day Teaming";
$punishments_ban["Building"] = "tempban %player% 3 day Building";
$punishments_ban["Trolling"] = "tempban %player% 3 day Trolling";
$punishments_ban["Skin"] = "tempban %player% 1 day Skin";
$punishments_ban["Name"] = "tempban %player% 1 day Name";
# How to add a new punishment:
# $punishments_ban["new reason"] = "ban command";

$punishments_mute["Spamming"] = "tempmute %player% 1 day Spamming";
$punishments_mute["Advertising"] = "tempmute %player% 7 day Advertising";
$punishments_mute["Bad Word"] = "tempmute %player% 3 day Bad Word";
$punishments_mute["Mild Insult"] = "tempmute %player% 3 day Mild Insult";
$punishments_mute["Serious Insult"] = "tempmute %player% 30 day Serious Insult";
$punishments_mute["Racism"] = "tempmute %player% 30 day Racism";
# How to add a new punishment:
# $punishments_ban["another reason"] = "mute command";

$punishment_all = array_merge($punishments_ban, $punishments_mute);
?>
