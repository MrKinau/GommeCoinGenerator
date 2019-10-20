/*
 * Created by David Luedtke (MrKinau)
 * 2019/5/5
 */

package systems.kinau.gommecoingenerator.network.protocol.play;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import systems.kinau.gommecoingenerator.GommeCoinGenerator;
import systems.kinau.gommecoingenerator.event.play.UpdateSlotEvent;
import systems.kinau.gommecoingenerator.network.protocol.NetworkHandler;
import systems.kinau.gommecoingenerator.network.protocol.Packet;
import systems.kinau.gommecoingenerator.network.utils.ByteArrayDataInputWrapper;

public class PacketInSetSlot extends Packet {

    @Getter private int windowId;
    @Getter private short slotId;
    @Getter private ByteArrayDataOutput slotData;

    @Override
    public void write(ByteArrayDataOutput out, int protocolId) { }

    @Override
    public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) {
        this.windowId = in.readByte();
        this.slotId = in.readShort();

        byte[] bytes = new byte[in.getAvailable()];
        in.readBytes(bytes);
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.write(bytes.clone());
        this.slotData = out;

        GommeCoinGenerator.getInstance().getEventManager().callEvent(new UpdateSlotEvent(windowId, slotId, slotData));
    }
}
