/*
 * Created by David Luedtke (MrKinau)
 * 2019/10/18
 */

package systems.kinau.gommecoingenerator.modules;

import lombok.Getter;
import systems.kinau.gommecoingenerator.GommeCoinGenerator;
import systems.kinau.gommecoingenerator.bot.Player;
import systems.kinau.gommecoingenerator.event.EventHandler;
import systems.kinau.gommecoingenerator.event.Listener;
import systems.kinau.gommecoingenerator.event.play.DifficultySetEvent;
import systems.kinau.gommecoingenerator.event.play.DisconnectEvent;
import systems.kinau.gommecoingenerator.event.play.JoinGameEvent;
import systems.kinau.gommecoingenerator.event.play.KeepAliveEvent;
import systems.kinau.gommecoingenerator.network.protocol.NetworkHandler;
import systems.kinau.gommecoingenerator.network.protocol.play.PacketOutClientSettings;
import systems.kinau.gommecoingenerator.network.protocol.play.PacketOutKeepAlive;
import systems.kinau.gommecoingenerator.network.protocol.play.PacketOutPosition;

public class ClientDefaultsModule extends Module implements Listener {

    @Getter private Thread positionThread;

    @Override
    public void onEnable() {
        GommeCoinGenerator.getInstance().getEventManager().registerListener(this);
    }

    @Override
    public void onDisable() {
        positionThread.interrupt();
    }

    @EventHandler
    public void onSetDifficulty(DifficultySetEvent event) {
        new Thread(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //Start position updates
            startPositionUpdate(GommeCoinGenerator.getInstance().getNet());
        }).start();
    }

    @EventHandler
    public void onDisconnect(DisconnectEvent event) {
        GommeCoinGenerator.getLog().info("Disconnected: " + event.getDisconnectMessage());
        GommeCoinGenerator.getInstance().setRunning(false);
    }

    @EventHandler
    public void onJoinGame(JoinGameEvent event) {
        GommeCoinGenerator.getInstance().getNet().sendPacket(new PacketOutClientSettings());
    }

    @EventHandler
    public void onKeepAlive(KeepAliveEvent event) {
        GommeCoinGenerator.getInstance().getNet().sendPacket(new PacketOutKeepAlive(event.getId()));
    }

    private void startPositionUpdate(NetworkHandler networkHandler) {
        if(positionThread != null)
            positionThread.interrupt();
        positionThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                Player player = GommeCoinGenerator.getInstance().getPlayer();
                networkHandler.sendPacket(new PacketOutPosition(player.getX(), player.getY(), player.getZ(), true));
                try { Thread.sleep(1000); } catch (InterruptedException e) { break; }
            }
        });
        positionThread.start();
    }
}
