/*
 * Created by David Luedtke (MrKinau)
 * 2019/5/5
 */

package systems.kinau.gommecoingenerator.network.protocol.play;

import com.google.common.io.ByteArrayDataOutput;
import lombok.Getter;
import lombok.NoArgsConstructor;
import systems.kinau.gommecoingenerator.GommeCoinGenerator;
import systems.kinau.gommecoingenerator.event.play.PosLookChangeEvent;
import systems.kinau.gommecoingenerator.network.protocol.NetworkHandler;
import systems.kinau.gommecoingenerator.network.protocol.Packet;
import systems.kinau.gommecoingenerator.network.protocol.ProtocolConstants;
import systems.kinau.gommecoingenerator.network.utils.ByteArrayDataInputWrapper;

@NoArgsConstructor
public class PacketInPlayerPosLook extends Packet {

    @Getter private double x;
    @Getter private double y;
    @Getter private double z;
    @Getter private float yaw;
    @Getter private float pitch;
    @Getter private  int teleportId;

    @Override
    public void write(ByteArrayDataOutput out, int protocolId) {
        //Only incoming packet
    }

    @Override
    public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) {
        double x = in.readDouble();
        double y = in.readDouble();
        double z = in.readDouble();
        float yaw = in.readFloat();
        float pitch = in.readFloat();
        if(in.readByte() == 0) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
            if(protocolId >= ProtocolConstants.MINECRAFT_1_9) {
                this.teleportId = readVarInt(in); //tID
            }
            GommeCoinGenerator.getInstance().getEventManager().callEvent(new PosLookChangeEvent(x, y, z, yaw, pitch, teleportId));
        }
    }
}
