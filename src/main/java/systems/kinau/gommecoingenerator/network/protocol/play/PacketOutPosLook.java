/*
 * Created by David Luedtke (MrKinau)
 * 2019/10/19
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
public class PacketOutPosLook extends Packet {

    @Getter private double x;
    @Getter private double y;
    @Getter private double z;
    @Getter private float yaw;
    @Getter private float pitch;
    @Getter private boolean onGround;

    @Override
    public void write(ByteArrayDataOutput out, int protocolId) throws IOException {
        out.writeDouble(getX());
        out.writeDouble(getY());
        out.writeDouble(getZ());
        out.writeFloat(getYaw());
        out.writeFloat(getPitch());
        out.writeBoolean(isOnGround());
    }

    @Override
    public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) throws IOException {

    }
}
