/*
 * Created by David Luedtke (MrKinau)
 * 2019/5/5
 */

package systems.kinau.gommecoingenerator.network.protocol.play;

import com.google.common.io.ByteArrayDataOutput;
import lombok.Getter;
import lombok.NoArgsConstructor;
import systems.kinau.gommecoingenerator.GommeCoinGenerator;
import systems.kinau.gommecoingenerator.event.play.DifficultySetEvent;
import systems.kinau.gommecoingenerator.network.protocol.NetworkHandler;
import systems.kinau.gommecoingenerator.network.protocol.Packet;
import systems.kinau.gommecoingenerator.network.utils.ByteArrayDataInputWrapper;

@NoArgsConstructor
public class PacketInDifficultySet extends Packet {

    @Getter private int difficulty;

    @Override
    public void write(ByteArrayDataOutput out, int protocolId) {
        //Only incoming packet
    }

    @Override
    public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) {
        this.difficulty = in.readUnsignedByte();

        GommeCoinGenerator.getInstance().getEventManager().callEvent(new DifficultySetEvent(getDifficulty()));
    }
}
