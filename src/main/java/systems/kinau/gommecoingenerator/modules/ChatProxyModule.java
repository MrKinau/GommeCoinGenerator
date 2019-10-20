/*
 * Created by David Luedtke (MrKinau)
 * 2019/10/18
 */

package systems.kinau.gommecoingenerator.modules;

import systems.kinau.gommecoingenerator.GommeCoinGenerator;
import systems.kinau.gommecoingenerator.event.EventHandler;
import systems.kinau.gommecoingenerator.event.Listener;
import systems.kinau.gommecoingenerator.event.play.ChatEvent;
import systems.kinau.gommecoingenerator.network.protocol.play.PacketOutChat;

import java.util.Scanner;

public class ChatProxyModule extends Module implements Listener {

    private Thread chatThread;

    @Override
    public void onEnable() {
        GommeCoinGenerator.getInstance().getEventManager().registerListener(this);
        chatThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while(!chatThread.isInterrupted()){
                String line = scanner.nextLine();
                GommeCoinGenerator.getInstance().getNet().sendPacket(new PacketOutChat(line));
            }
        });
        chatThread.start();
    }

    @Override
    public void onDisable() {
        chatThread.interrupt();
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        if (isEnabled() && !"".equals(event.getText()))
            GommeCoinGenerator.getLog().info("[CHAT] " + event.getText());
    }
}
