/*
 * Created by David Luedtke (MrKinau)
 * 2019/8/29
 */
package systems.kinau.gommecoingenerator.io;

import lombok.Getter;
import systems.kinau.gommecoingenerator.network.protocol.ProtocolConstants;

@Getter
public class SettingsConfig implements Config {

    @Property(key = "auto-reconnect") private boolean autoReconnect = false;
    @Property(key = "auto-reconnect-at") private int autoReconnectTime = 5;

    @Property(key = "account-username") private String userName = "GommeCoinGenerator";
    @Property(key = "account-password") private String password = "CHANGEME";

    @Property(key = "log-count") private int logCount = 15;
    @Property(key = "proxy-chat") private boolean proxyChat = false;

    @Property(key = "default-protocol") private String defaultProtocol = ProtocolConstants.getVersionString(ProtocolConstants.MINECRAFT_1_8);

    @Property(key = "discord-webHook") private String webHook = "false";

    public SettingsConfig() {
        String comments = "#auto-reconnect:\tAuto-Reconnect if bot get kicked/time out etc\n" +
                "#auto-reconnect-time:\tThe time (in seconds) the bot waits after kick to reconnect (only usable if auto-reconnect is set to true)\n" +
                "#log-count:\t\t\t\tThe number of logs the bot generate\n" +
                "#discord-webHook:\tUse this to send all chat messages from the bot to a Discord webhook\n" +
                "#account-username:\tThe username / e-mail of the account\n" +
                "#account-password:\tThe password of the account (ignored in offline-mode)\n" +
                "#default-protocol:\tOnly needed for Multi-Version servers. The Minecraft-Version for the ping request to the server. Possible values: (1.8, 1.9, 1.9.2, 1.9.2, 1.9.4, ...)\n" +
                "#proxy-chat:\tWhether to function as a chat client (printing incoming chat messages to the console and sending input as chat)";

        init("config.properties", comments);
    }
}
