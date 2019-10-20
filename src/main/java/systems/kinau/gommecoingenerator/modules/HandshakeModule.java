/*
 * Created by David Luedtke (MrKinau)
 * 2019/10/18
 */

package systems.kinau.gommecoingenerator.modules;

import lombok.Getter;
import systems.kinau.gommecoingenerator.GommeCoinGenerator;
import systems.kinau.gommecoingenerator.network.protocol.State;
import systems.kinau.gommecoingenerator.network.protocol.handshake.PacketOutHandshake;

public class HandshakeModule extends Module {

    @Getter private String serverName;
    @Getter private int serverPort;

    public HandshakeModule(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    @Override
    public void onEnable() {
        GommeCoinGenerator.getInstance().getNet().sendPacket(new PacketOutHandshake(serverName, serverPort));
        GommeCoinGenerator.getInstance().getNet().setState(State.LOGIN);
    }

    @Override
    public void onDisable() {
        GommeCoinGenerator.getLog().warning("Tried to disable " + this.getClass().getSimpleName() + ", can not disable it!");
    }
}
