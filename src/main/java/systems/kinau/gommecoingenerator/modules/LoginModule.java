/*
 * Created by David Luedtke (MrKinau)
 * 2019/10/18
 */

package systems.kinau.gommecoingenerator.modules;

import lombok.Getter;
import systems.kinau.gommecoingenerator.GommeCoinGenerator;
import systems.kinau.gommecoingenerator.event.EventHandler;
import systems.kinau.gommecoingenerator.event.Listener;
import systems.kinau.gommecoingenerator.event.login.EncryptionRequestEvent;
import systems.kinau.gommecoingenerator.event.login.LoginDisconnectEvent;
import systems.kinau.gommecoingenerator.event.login.LoginSuccessEvent;
import systems.kinau.gommecoingenerator.event.login.SetCompressionEvent;
import systems.kinau.gommecoingenerator.network.protocol.NetworkHandler;
import systems.kinau.gommecoingenerator.network.protocol.State;
import systems.kinau.gommecoingenerator.network.protocol.login.PacketOutEncryptionResponse;
import systems.kinau.gommecoingenerator.network.protocol.login.PacketOutLoginStart;
import systems.kinau.gommecoingenerator.network.utils.CryptManager;

import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLEncoder;

public class LoginModule extends Module implements Listener {

    @Getter private String userName;

    public LoginModule(String userName) {
        this.userName = userName;
        GommeCoinGenerator.getInstance().getEventManager().registerListener(this);
    }

    @Override
    public void onEnable() {
        GommeCoinGenerator.getInstance().getNet().sendPacket(new PacketOutLoginStart(getUserName()));
    }

    @Override
    public void onDisable() {
        GommeCoinGenerator.getLog().warning("Tried to disable " + this.getClass().getSimpleName() + ", can not disable it!");
    }

    @EventHandler
    public void onEncryptionRequest(EncryptionRequestEvent event) {
        NetworkHandler networkHandler = GommeCoinGenerator.getInstance().getNet();

        //Set public key
        networkHandler.setPublicKey(event.getPublicKey());

        //Generate & Set secret key
        SecretKey secretKey = CryptManager.createNewSharedKey();
        networkHandler.setSecretKey(secretKey);

        byte[] serverIdHash = CryptManager.getServerIdHash(event.getServerId().trim(), event.getPublicKey(), secretKey);
        if(serverIdHash == null) {
            GommeCoinGenerator.getLog().severe("Cannot hash server id: exiting!");
            GommeCoinGenerator.getInstance().setRunning(false);
            return;
        }

        String var5 = (new BigInteger(serverIdHash)).toString(16);
        String var6 = sendSessionRequest(GommeCoinGenerator.getInstance().getAuthData().getUsername(), "token:" + GommeCoinGenerator.getInstance().getAuthData().getAccessToken() + ":" + GommeCoinGenerator.getInstance().getAuthData().getProfile(), var5);

        networkHandler.sendPacket(new PacketOutEncryptionResponse(event.getServerId(), event.getPublicKey(), event.getVerifyToken(), secretKey));
        networkHandler.activateEncryption();
        networkHandler.decryptInputStream();
    }

    @EventHandler
    public void onLoginDisconnect(LoginDisconnectEvent event) {
        GommeCoinGenerator.getLog().severe("Login failed: " + event.getErrorMessage());
        GommeCoinGenerator.getInstance().setRunning(false);
        GommeCoinGenerator.getInstance().setAuthData(null);
    }

    @EventHandler
    public void onSetCompression(SetCompressionEvent event) {
        GommeCoinGenerator.getInstance().getNet().setThreshold(event.getThreshold());
    }

    @EventHandler
    public void onLoginSuccess(LoginSuccessEvent event) {
        GommeCoinGenerator.getLog().info("Login successful!");
        GommeCoinGenerator.getLog().info("Name: " + event.getUserName());
        GommeCoinGenerator.getLog().info("UUID: " + event.getUuid());
        GommeCoinGenerator.getInstance().getNet().setState(State.PLAY);
    }

    private String sendSessionRequest(String user, String session, String serverid) {
        try {
            return sendGetRequest("http://session.minecraft.net/game/joinserver.jsp"
                    + "?user=" + urlEncode(user)
                    + "&sessionId=" + urlEncode(session)
                    + "&serverId=" + urlEncode(serverid));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String sendGetRequest(String url) {
        try {
            URL var4 = new URL(url);
            BufferedReader var5 = new BufferedReader(new InputStreamReader(var4.openStream()));
            String var6 = var5.readLine();
            var5.close();
            return var6;
        } catch (IOException var7) {
            return var7.toString();
        }
    }

    private String urlEncode(String par0Str) throws IOException {
        return URLEncoder.encode(par0Str, "UTF-8");
    }
}
