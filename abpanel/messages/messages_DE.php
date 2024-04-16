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
$messages_key = "Schlüssel";
$messages_login = "Anmelden";
$messages_logout = "Abmelden";
$messages_player = "Spieler";
$messages_search = "Suche";
$messages_date = "Datum";
$messages_type = "Typ";
$messages_from = "Von";
$messages_reason = "Grund";
$messages_duration = "Dauer";
$messages_server = "Server";
$messages_message = "Nachricht";

# Assets
$messages_footer = "Advanced Ban - 2024 von Delta203";

$messages_navbar_home = "Home";

$messages_sidebar_logged = "Angemeldeter Benutzer: <b>%player%</b>";
$messages_sidebar_uuid = "UUID: <b>%uuid%</b>";
$messages_sidebar_session = "Sitzung: <b>%key%</b>";
$messages_sidebar_server = "Server: <b>%server%</b>";

# Error
$messages_invalid_session = "Ungültige Sitzung";
$messages_invalid_session_info = "Entweder stimmt die UUID nicht mit dem Schlüssel überein, der Schlüssel ist falsch oder die Sitzung ist abgelaufen.";

$messages_not_found = "Nicht gefunden";
$messages_not_found_info = "Die angeforderte URL wurde auf diesem Server nicht gefunden.";

# Alerts
$messages_alert_success = "Du hast den Report erfolgreich bearbeitet!";
$messages_alert_jumped = "Du wirst nun zum Server <b>%server%</b> springen!";
$messages_alert_unbanned = "Du hast <b>%player%</b> erfolgreich entbannt!<br>Bitte warte einige Sekunden, damit der Unban Vorgang abgeschlossen werden kann.";
$messages_alert_unmuted = "Du hast <b>%player%</b> erfolgreich entmuted!<br>Bitte warte einige Sekunden, damit der Unmute Vorgang abgeschlossen werden kann.";

# Index
$messages_index_manage_reports = "Reports verwalten";
$messages_index_manage_reports_info = "Derzeit sind %reports% offene Reports vorhanden.";
$messages_index_manage_reports_button = "Report öffnen";
$messages_index_check_player = "Spieler überprüfen";
$messages_index_check_player_info = "Suche nach Spieler UUID oder Namen.";
$messages_index_global_bans = "Globale Bans:";
$messages_index_global_mutes = "Globale Mutes:";

# Ban/Mute list
$messages_banlist = "Gebannte Spieler";
$messages_mutelist = "Gemutete Spieler";

# Check
$messages_check = "Spieler überprüfen";
$messages_check_unban = "Spieler entbannen";
$messages_check_unmute = "Spieler entmuten";
$messages_check_name = "Name: <b>%player%</b>";
$messages_check_uuid = "UUID: <b>%uuid%</b>";
$messages_check_namemc = "NameMC Profil:";
$messages_check_reports = "Reports:";
$messages_check_registered = "Registriert: <b>%date%</b>";
$messages_check_server = "Server: <b>%server%</b>";
$messages_check_banned = "Gebannt:";
$messages_check_muted = "Gemuted:";
$messages_check_ban_mute = "Ban & Mute Verlauf";
$messages_check_ban_mute_bans = "Bans: <b>%bans%</b>";
$messages_check_ban_mute_mutes = "Mutes: <b>%mutes%</b>";

# Report
$messages_report = "Report";
$messages_report_jump = "Zum Server springen";
$messages_report_close = "Report schließen";
$messages_report_reason = "Grund: <b>%reason%</b>";
$messages_report_name = "Name:";
$messages_report_uuid = "UUID: <b>%uuid%</b>";
$messages_report_date = "Datum: <b>%date%</b>";
$messages_report_server = "Server: <b>%server%</b>";
$messages_report_from = "Von: <b>%from%</b>";
$messages_report_from_uuid = "Von UUID: <b>%fromUUID%</b>";
$messages_report_punish = "Bestrafe den Spieler <b>%player%</b>!";
$messages_report_punish_checked = "Alles überprüft";
$messages_report_punish_button = "Spieler bestrafen";
$messages_report_chatlog = "Chat Log";
?>
