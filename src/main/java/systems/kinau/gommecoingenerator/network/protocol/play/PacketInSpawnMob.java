/*
 * Created by David Luedtke (MrKinau)
 * 2019/10/19
 */

package systems.kinau.gommecoingenerator.network.protocol.play;

import com.google.common.io.ByteArrayDataOutput;
import lombok.Getter;
import systems.kinau.gommecoingenerator.GommeCoinGenerator;
import systems.kinau.gommecoingenerator.event.play.SpawnMobEvent;
import systems.kinau.gommecoingenerator.network.protocol.NetworkHandler;
import systems.kinau.gommecoingenerator.network.protocol.Packet;
import systems.kinau.gommecoingenerator.network.utils.ByteArrayDataInputWrapper;

import java.io.IOException;
import java.util.UUID;

public class PacketInSpawnMob extends Packet {

    @Getter private int eID;
    @Getter private UUID uuid;
    @Getter private int type;
    @Getter private double x;
    @Getter private double y;
    @Getter private double z;
    @Getter private byte yaw;
    @Getter private byte pitch;
    @Getter private byte headPitch;
    @Getter private short velocityX;
    @Getter private short velocityY;
    @Getter private short velocityZ;

    @Override
    public void write(ByteArrayDataOutput out, int protocolId) throws IOException {
        //Only incoming packet
    }

    @Override
    public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) throws IOException {
        this.eID = readVarInt(in);
        this.uuid = readUUID(in);
        this.type = readVarInt(in);
        this.x = in.readDouble();
        this.y = in.readDouble();
        this.z = in.readDouble();
        this.yaw = in.readByte();
        this.pitch = in.readByte();
        this.headPitch = in.readByte();
        this.velocityX = in.readShort();
        this.velocityY = in.readShort();
        this.velocityZ = in.readShort();

        GommeCoinGenerator.getInstance().getEventManager().callEvent(new SpawnMobEvent(eID, uuid, type, x, y, z, yaw, pitch, headPitch, velocityX, velocityY, velocityZ));
    }
}
