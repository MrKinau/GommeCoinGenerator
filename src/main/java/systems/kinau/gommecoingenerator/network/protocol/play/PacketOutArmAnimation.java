/*
 * Created by David Luedtke (MrKinau)
 * 2019/5/5
 */

package systems.kinau.gommecoingenerator.network.protocol.play;

import com.google.common.io.ByteArrayDataOutput;
import systems.kinau.gommecoingenerator.network.protocol.NetworkHandler;
import systems.kinau.gommecoingenerator.network.protocol.Packet;
import systems.kinau.gommecoingenerator.network.utils.ByteArrayDataInputWrapper;

public class PacketOutArmAnimation extends Packet {

    @Override
    public void write(ByteArrayDataOutput out, int protocolId) {
        //This packet needs no content
    }

    @Override
    public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) {
        //Only outgoing packet
    }
}
