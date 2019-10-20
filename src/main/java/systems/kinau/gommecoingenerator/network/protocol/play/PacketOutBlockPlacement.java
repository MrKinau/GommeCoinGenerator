package systems.kinau.gommecoingenerator.network.protocol.play;

import com.google.common.io.ByteArrayDataOutput;
import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.kinau.gommecoingenerator.network.protocol.NetworkHandler;
import systems.kinau.gommecoingenerator.network.protocol.Packet;
import systems.kinau.gommecoingenerator.network.utils.ByteArrayDataInputWrapper;

import java.io.IOException;

@AllArgsConstructor
public class PacketOutBlockPlacement extends Packet {

    @Getter private int x;
    @Getter private int y;
    @Getter private int z;
    @Getter private int face;
    @Getter private int hand;
    @Getter private float cursorPosX;
    @Getter private float cursorPosY;
    @Getter private float cursorPosZ;

    @Override
    public void write(ByteArrayDataOutput out, int protocolId) throws IOException {
        out.writeLong(((long)(x & 0x3FFFFFF) << 38) | ((long)(y & 0xFFF) << 26) | z & 0x3FFFFFF);
        writeVarInt(face, out);
        writeVarInt(hand, out);
        out.writeFloat(cursorPosX);
        out.writeFloat(cursorPosY);
        out.writeFloat(cursorPosZ);
    }

    @Override
    public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) throws IOException {

    }
}
