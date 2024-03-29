/*
 * Created by David Luedtke (MrKinau)
 * 2019/10/18
 */

package systems.kinau.gommecoingenerator.network.protocol.play;

import com.google.common.io.ByteArrayDataOutput;
import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.kinau.gommecoingenerator.network.protocol.NetworkHandler;
import systems.kinau.gommecoingenerator.network.protocol.Packet;
import systems.kinau.gommecoingenerator.network.utils.ByteArrayDataInputWrapper;

import java.io.IOException;

@AllArgsConstructor
public class PacketOutTeleportConfirm extends Packet {

    @Getter private int teleportId;

    @Override
    public void write(ByteArrayDataOutput out, int protocolId) throws IOException {
        writeVarInt(teleportId, out);
    }

    @Override
    public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) throws IOException {

    }
}
