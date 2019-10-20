package systems.kinau.gommecoingenerator.network.protocol.play;

import com.google.common.io.ByteArrayDataOutput;
import lombok.Getter;
import systems.kinau.gommecoingenerator.GommeCoinGenerator;
import systems.kinau.gommecoingenerator.event.play.OpenWindowEvent;
import systems.kinau.gommecoingenerator.network.protocol.NetworkHandler;
import systems.kinau.gommecoingenerator.network.protocol.Packet;
import systems.kinau.gommecoingenerator.network.utils.ByteArrayDataInputWrapper;

import java.io.IOException;

public class PacketInOpenWindow extends Packet {

    @Getter private int windowId;
    @Getter private String windowType;
    @Getter private String windowTitle;
    @Getter private int slotCount;
    @Getter private int eId;

    @Override
    public void write(ByteArrayDataOutput out, int protocolId) throws IOException {
        //Only incoming packet
    }

    @Override
    public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) throws IOException {
        this.windowId = in.readUnsignedByte();
        this.windowType = readString(in);
        this.windowTitle = readString(in);
        this.slotCount = in.readUnsignedByte();
        if(getWindowType().equalsIgnoreCase("EntityHorse"))
            this.eId = readVarInt(in);

        GommeCoinGenerator.getInstance().getEventManager().callEvent(new OpenWindowEvent(windowId, windowType, windowTitle, slotCount, eId));
    }
}
