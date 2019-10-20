/*
 * Created by David Luedtke (MrKinau)
 * 2019/5/5
 */

package systems.kinau.gommecoingenerator.network.protocol.login;

import com.google.common.io.ByteArrayDataOutput;
import systems.kinau.gommecoingenerator.GommeCoinGenerator;
import systems.kinau.gommecoingenerator.event.login.LoginDisconnectEvent;
import systems.kinau.gommecoingenerator.network.protocol.NetworkHandler;
import systems.kinau.gommecoingenerator.network.protocol.Packet;
import systems.kinau.gommecoingenerator.network.utils.ByteArrayDataInputWrapper;

import java.io.IOException;

public class PacketInLoginDisconnect extends Packet {

    @Override
    public void write(ByteArrayDataOutput out, int protocolId) throws IOException { }

    @Override
    public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) throws IOException {
        String errorMessage = readString(in);
        GommeCoinGenerator.getInstance().getEventManager().callEvent(new LoginDisconnectEvent(errorMessage));
    }
}
