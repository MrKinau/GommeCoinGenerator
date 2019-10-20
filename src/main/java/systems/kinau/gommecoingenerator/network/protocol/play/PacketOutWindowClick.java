package systems.kinau.gommecoingenerator.network.protocol.play;

import com.google.common.io.ByteArrayDataOutput;
import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.kinau.gommecoingenerator.bot.Slot;
import systems.kinau.gommecoingenerator.network.protocol.NetworkHandler;
import systems.kinau.gommecoingenerator.network.protocol.Packet;
import systems.kinau.gommecoingenerator.network.utils.ByteArrayDataInputWrapper;

import java.io.IOException;

@AllArgsConstructor
public class PacketOutWindowClick extends Packet {

    @Getter private int windowId;
    @Getter private short clickedSlot;
    @Getter private byte button;
    @Getter private short actionNumber;
    @Getter private int invOperationMode;
    @Getter private Slot slot;

    @Override
    public void write(ByteArrayDataOutput out, int protocolId) throws IOException {
        out.writeByte(windowId);
        out.writeShort(clickedSlot);
        out.writeByte(button);
        out.writeShort(actionNumber);
        writeVarInt(invOperationMode, out);
        out.writeShort(slot.getItemId());
        out.writeByte(slot.getCount());
        out.writeShort(slot.getDamage());
        out.write(slot.getNbtData());
    }

    @Override
    public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) throws IOException {

    }
}
