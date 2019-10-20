/*
 * Created by David Luedtke (MrKinau)
 * 2019/5/3
 */

package systems.kinau.gommecoingenerator.network.protocol.handshake;

import com.google.common.io.ByteArrayDataOutput;
import lombok.AllArgsConstructor;
import systems.kinau.gommecoingenerator.GommeCoinGenerator;
import systems.kinau.gommecoingenerator.network.protocol.NetworkHandler;
import systems.kinau.gommecoingenerator.network.protocol.Packet;
import systems.kinau.gommecoingenerator.network.utils.ByteArrayDataInputWrapper;

@AllArgsConstructor
public class PacketOutHandshake extends Packet {

    private String serverName;
    private int serverPort;

    @Override
    public void write(ByteArrayDataOutput out, int protocolId) {
        writeVarInt(GommeCoinGenerator.getInstance().getServerProtocol(), out);
        writeString(serverName, out);
        out.writeShort(serverPort);
        writeVarInt(2, out); //next State = 2 -> LOGIN
    }

    @Override
    public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) { }
}
