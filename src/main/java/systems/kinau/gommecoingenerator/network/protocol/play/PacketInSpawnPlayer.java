/*
 * Created by David Luedtke (MrKinau)
 * 2019/10/19
 */

package systems.kinau.gommecoingenerator.network.protocol.play;

import com.google.common.io.ByteArrayDataOutput;
import lombok.Getter;
import systems.kinau.gommecoingenerator.GommeCoinGenerator;
import systems.kinau.gommecoingenerator.event.play.SpawnPlayerEvent;
import systems.kinau.gommecoingenerator.network.protocol.NetworkHandler;
import systems.kinau.gommecoingenerator.network.protocol.Packet;
import systems.kinau.gommecoingenerator.network.utils.ByteArrayDataInputWrapper;

import java.io.IOException;
import java.util.UUID;

public class PacketInSpawnPlayer extends Packet {

    @Getter private int eID;
    @Getter private UUID uuid;
    @Getter private double x;
    @Getter private double y;
    @Getter private double z;
    @Getter private byte yaw;
    @Getter private byte pitch;

    @Override
    public void write(ByteArrayDataOutput out, int protocolId) throws IOException {
        //Only incoming packet
    }

    @Override
    public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) throws IOException {
        this.eID = readVarInt(in);
        this.uuid = readUUID(in);
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
        this.yaw = in.readByte();
        this.pitch = in.readByte();

        GommeCoinGenerator.getInstance().getEventManager().callEvent(new SpawnPlayerEvent(eID, uuid, x, y, z, yaw, pitch));
    }
}
