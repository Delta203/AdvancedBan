<?php
# Placeholders:
#   %player%    | Player name
#   %uuid%      | Player uuid
#   %from%      | From player name
#   %fromUUID%  | From player uuid
#   %key%       | Session key*
#   %server%    | Server
#   %date%      | Date
#   %reason%    | Reason
#   %bans%      | Amount of bans
#   %mutes%     | Amount of mutes
#   %reports%   | Amount of reports

$messages_title = "Advanced Ban Panel";

# Universal
$messages_name = "Name";
$messages_uuid = "UUID";
$messages_key = "Key";
$messages_login = "Login";
$messages_logout = "Logout";
$messages_player = "Player";
$messages_search = "Search";
$messages_date = "Date";
$messages_type = "Type";
$messages_from = "From";
$messages_reason = "Reason";
$messages_duration = "Duration";
$messages_server = "Server";
$messages_message = "Message";

# Assets
$messages_footer = "Advanced Ban - 2024 by Delta203";

$messages_navbar_home = "Home";

$messages_sidebar_logged = "Logged user: <b>%player%</b>";
$messages_sidebar_uuid = "UUID: <b>%uuid%</b>";
$messages_sidebar_session = "Session: <b>%key%</b>";
$messages_sidebar_server = "Server: <b>%server%</b>";

# Error
$messages_invalid_session = "Invalid Session";
$messages_invalid_session_info = "Either the uuid does not match the key, the key is incorrect or the session has expired.";

$messages_not_found = "Not Found";
$messages_not_found_info = "The requested URL was not found on this server.";

# Alerts
$messages_alert_success = "You have successfully completed the report!";
$messages_alert_jumped = "You will now jump to the server <b>%server%</b>!";
$messages_alert_unbanned = "You have successfully unbanned <b>%player%</b>!<br>Wait a few seconds for the unban to process.";
$messages_alert_unmuted = "You have successfully unmuted <b>%player%</b>!<br>Wait a few seconds for the unmute to process.";

# Index
$messages_index_manage_reports = "Manage Reports";
$messages_index_manage_reports_info = "There are currently %reports% open reports.";
$messages_index_manage_reports_button = "Open Report";
$messages_index_check_player = "Check Player";
$messages_index_check_player_info = "Search for player UUID or name.";
$messages_index_global_bans = "Global Bans:";
$messages_index_global_mutes = "Global Mutes:";

# Ban/Mute list
$messages_banlist = "Banned Players";
$messages_mutelist = "Muted Players";

# Check
$messages_check = "Check Player";
$messages_check_unban = "Unban Player";
$messages_check_unmute = "Unmute Player";
$messages_check_name = "Name: <b>%player%</b>";
$messages_check_uuid = "UUID: <b>%uuid%</b>";
$messages_check_namemc = "NameMC Profile:";
$messages_check_reports = "Reports:";
$messages_check_registered = "Registered: <b>%date%</b>";
$messages_check_server = "Server: <b>%server%</b>";
$messages_check_banned = "Banned:";
$messages_check_muted = "Muted:";
$messages_check_ban_mute = "Ban & Mute Verlauf";
$messages_check_ban_mute_bans = "Bans: <b>%bans%</b>";
$messages_check_ban_mute_mutes = "Mutes: <b>%mutes%</b>";

# Report
$messages_report = "Report";
$messages_report_jump = "Jump To Server";
$messages_report_close = "Close Report";
$messages_report_reason = "Reason: <b>%reason%</b>";
$messages_report_name = "Name:";
$messages_report_uuid = "UUID: <b>%uuid%</b>";
$messages_report_date = "Date: <b>%date%</b>";
$messages_report_server = "Server: <b>%server%</b>";
$messages_report_chat = "Chat Log:";
$messages_report_from = "From: <b>%from%</b>";
$messages_report_from_uuid = "From UUID: <b>%fromUUID%</b>";
$messages_report_punish = "Punish the player <b>%player%</b>!";
$messages_report_punish_checked = "Everything checked";
$messages_report_punish_button = "Punish Player";
$messages_report_chatlog = "Chat Log";
?>
