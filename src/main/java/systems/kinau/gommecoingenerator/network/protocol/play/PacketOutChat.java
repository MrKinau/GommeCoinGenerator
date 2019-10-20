/*
 * Created by David Luedtke (MrKinau)
 * 2019/5/5
 */

package systems.kinau.gommecoingenerator.network.protocol.play;

import com.google.common.io.ByteArrayDataOutput;
import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.kinau.gommecoingenerator.GommeCoinGenerator;
import systems.kinau.gommecoingenerator.io.discord.DiscordDetails;
import systems.kinau.gommecoingenerator.network.protocol.NetworkHandler;
import systems.kinau.gommecoingenerator.network.protocol.Packet;
import systems.kinau.gommecoingenerator.network.utils.ByteArrayDataInputWrapper;

@AllArgsConstructor
public class PacketOutChat extends Packet {

    @Getter private String message;

    @Override
    public void write(ByteArrayDataOutput out, int protocolId) {
        writeString(getMessage(), out);
        if(!GommeCoinGenerator.getInstance().getConfig().getWebHook().equalsIgnoreCase("false"))
            GommeCoinGenerator.getInstance().getDiscord().dispatchMessage("`" + getMessage() + "`",
                    new DiscordDetails("GommeCoinGenerator", "https://www.publicdomainpictures.net/pictures/260000/nahled/gold-coin.jpg"));

    }

    @Override
    public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) { }
}
