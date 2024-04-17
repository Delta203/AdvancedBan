# AdvancedBan
Comprehensive ban, mute and report system with web panel.

## Requirements:
- java 1.17
- PHP 8
- Bungeecord
- MySQl (MariaDB)
- HTTP Server (Apache)

## Installation:
1. Unzip .zip file
2. Setup **bungeecord**
3. Setup **web panel**
### Bungeecord:
1. Put the .jar file into your plugins folder.
2. Start your server and stop it after the configuration files have been created.
3. Insert mysql connection data into `config.yml`.
4. Run your server -> **done**
### Web Panel:
1. Put the abpanel folder into your root directory.
2. Open the `config.php` file and insert your mysql connection data.
3. Restart your web server -> **done**

## Functions:
- Main command: `/advancedban`
- Ban players permanently or temporarily.
- Mute players permanently or temporarily.
- Report players who do not behave.
- Manage reports and players with a web panel
- Monitor the chat using the chat log system.

## Languages:
- English (EN)
- Deutsch (DE)

## Login key:
````
Key Generation:
key = uuid + name + currentTimeMillis();
key += key.reverse();
key = md5(key);
````
- Every time a player joins, a new login key is generated.
- The web panel session is valid if the UUID matches the login key.
- The registered player must be permanently online on the server in order to perform the web panel tasks ingame. (`dispatchCommand`)
- Make sure that the login key is kept secret.

## Permissions:
- ab.ban (Usage: /ban)
- ab.tempban (Usage: /tempban)
- ab.unban (Usage: /unban)
- ab.mute (Usage: /mute)
- ab.tempmute (Usage: /tempmute)
- ab.unmute (Usage: /unmute)
- ab.check (Usage: /check)
- ab.panel (Usage: /abpanel)
- ab.cantbereported (Player can not be reported)

## Download:
