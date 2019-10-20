/*
 * Created by David Luedtke (MrKinau)
 * 2019/5/5
 */

package systems.kinau.gommecoingenerator.network.protocol.login;

import com.google.common.io.ByteArrayDataOutput;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import systems.kinau.gommecoingenerator.GommeCoinGenerator;
import systems.kinau.gommecoingenerator.event.login.EncryptionRequestEvent;
import systems.kinau.gommecoingenerator.network.protocol.NetworkHandler;
import systems.kinau.gommecoingenerator.network.protocol.Packet;
import systems.kinau.gommecoingenerator.network.utils.ByteArrayDataInputWrapper;
import systems.kinau.gommecoingenerator.network.utils.CryptManager;

import java.io.IOException;
import java.security.PublicKey;

@AllArgsConstructor
@NoArgsConstructor
public class PacketInEncryptionRequest extends Packet {

    @Getter private String serverId;
    @Getter private PublicKey publicKey;
    @Getter private byte[] verifyToken;

    @Override
    public void write(ByteArrayDataOutput out, int protocolId) throws IOException { }

    @Override
    public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) throws IOException {
        this.serverId = readString(in);
        this.publicKey = CryptManager.decodePublicKey(readBytesFromStream(in));
        this.verifyToken = readBytesFromStream(in);

        GommeCoinGenerator.getInstance().getEventManager().callEvent(new EncryptionRequestEvent(serverId, publicKey, verifyToken));
    }
}
