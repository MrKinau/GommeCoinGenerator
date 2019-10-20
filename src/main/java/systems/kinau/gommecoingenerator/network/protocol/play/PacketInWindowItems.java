package systems.kinau.gommecoingenerator.network.protocol.play;

import com.google.common.io.ByteArrayDataOutput;
import lombok.Getter;
import systems.kinau.gommecoingenerator.GommeCoinGenerator;
import systems.kinau.gommecoingenerator.bot.Slot;
import systems.kinau.gommecoingenerator.event.play.WindowItemsEvent;
import systems.kinau.gommecoingenerator.network.protocol.NetworkHandler;
import systems.kinau.gommecoingenerator.network.protocol.Packet;
import systems.kinau.gommecoingenerator.network.utils.ByteArrayDataInputWrapper;
import systems.kinau.gommecoingenerator.network.utils.NBTUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PacketInWindowItems extends Packet {

    @Getter private int windowId;
    @Getter private Map<Integer, Slot> slotData = new HashMap<>();

    @Override
    public void write(ByteArrayDataOutput out, int protocolId) throws IOException {
        //Only incoming packet
    }

    @Override
    public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) throws IOException {
        this.windowId = in.readUnsignedByte();
        int count = in.readShort();
        for (int i = 0; i < count; i++) {
            short itemId = in.readShort();
            if (itemId != -1) {
                byte itemCount = in.readByte();
                short itemDamage = in.readShort();

                int bytes = NBTUtils.readNBT(in.clone());
                byte[] data = new byte[bytes];
                in.readBytes(data);
                slotData.put(i, new Slot(itemId, itemCount, itemDamage, data));
            }
        }

        GommeCoinGenerator.getInstance().getEventManager().callEvent(new WindowItemsEvent(getWindowId(), getSlotData()));
    }
}
