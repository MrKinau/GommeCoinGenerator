package systems.kinau.gommecoingenerator.modules;

import lombok.Getter;
import systems.kinau.gommecoingenerator.GommeCoinGenerator;
import systems.kinau.gommecoingenerator.bot.Player;
import systems.kinau.gommecoingenerator.event.EventHandler;
import systems.kinau.gommecoingenerator.event.Listener;
import systems.kinau.gommecoingenerator.event.play.DifficultySetEvent;
import systems.kinau.gommecoingenerator.event.play.OpenWindowEvent;
import systems.kinau.gommecoingenerator.event.play.SpawnMobEvent;
import systems.kinau.gommecoingenerator.event.play.WindowItemsEvent;
import systems.kinau.gommecoingenerator.network.protocol.play.*;

public class GommeModule extends Module implements Listener {

    @Getter private int lastWindowId = -1;
    @Getter private int villagerEid;
    @Getter private boolean teleporterUsed;
    @Getter private boolean arrivedAtVillager;
    @Getter private boolean rewardsClaimed;

    @Override
    public void onEnable() {
        GommeCoinGenerator.getInstance().getEventManager().registerListener(this);
    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void onDifficultySet(DifficultySetEvent event) {
        new Thread(() -> {
            delay(500);
            GommeCoinGenerator.getInstance().getNet().sendPacket(new PacketOutHeldItemChange((short) 0));
            delay(1000);
            GommeCoinGenerator.getInstance().getNet().sendPacket(new PacketOutBlockPlacement(-1, -1, -1, 255, 0, 0, 0, 0));
            GommeCoinGenerator.getInstance().getClientModule().getPositionThread().interrupt();
        }).start();
    }

    @EventHandler
    public void onWindowOpen(OpenWindowEvent event) {
        this.lastWindowId = event.getWindowId();
    }

    @EventHandler
    public void onWindowItems(WindowItemsEvent event) {
        if(event.getWindowId() == getLastWindowId() && !isTeleporterUsed()) {
            teleporterUsed = true;
            new Thread(() -> {
                GommeCoinGenerator.getLog().info("Teleporting to spawn...");
                delay(500);
                GommeCoinGenerator.getInstance().getNet().sendPacket(new PacketOutWindowClick(getLastWindowId(), (short)4, (byte)0, (short)1, 0, event.getSlotData().get(4)));
                delay(1000);
                GommeCoinGenerator.getLog().info("Moving to villager...");
                Player player = GommeCoinGenerator.getInstance().getPlayer();
                while (player.getY() > 5) {
                    player.setY(player.getY() - 0.15);
                    GommeCoinGenerator.getInstance().getNet().sendPacket(new PacketOutPosition(player.getX(), player.getY(), player.getZ(), false));
                    delay(50);
                }
                for (int i = 0; i < 26; i++) {
                    player.setYaw(player.getYaw() + 4);
                    player.setPitch(0.0F);
                    GommeCoinGenerator.getInstance().getNet().sendPacket(new PacketOutLook(player.getYaw(), player.getPitch(), false));
                    delay(50);
                }
                while (player.getX() < -105.5 || player.getZ() < 511.5) {
                    player.setX(player.getX() + 0.13);
                    player.setZ(player.getZ() + 0.13);
                    GommeCoinGenerator.getInstance().getNet().sendPacket(new PacketOutPosition(player.getX(), player.getY(), player.getZ(), false));
                    delay(50);
                }
                arrivedAtVillager = true;
                GommeCoinGenerator.getInstance().getNet().sendPacket(new PacketOutUseEntity(getVillagerEid(), 0, 0, 0, 0, 0));
                GommeCoinGenerator.getInstance().getNet().sendPacket(new PacketOutUseEntity(getVillagerEid(), 1, 0, 0, 0, 0));
            }).start();
        } else if(event.getWindowId() == getLastWindowId() && isArrivedAtVillager() && !isRewardsClaimed()) {
            delay(500);
            GommeCoinGenerator.getLog().info("Getting rewards");
            GommeCoinGenerator.getInstance().getNet().sendPacket(new PacketOutWindowClick(getLastWindowId(), (short)4, (byte)0, (short)1, 0, event.getSlotData().get(4)));
            delay(400);
            GommeCoinGenerator.getInstance().getNet().sendPacket(new PacketOutWindowClick(getLastWindowId(), (short)9, (byte)0, (short)1, 0, event.getSlotData().get(9)));
            delay(400);
            GommeCoinGenerator.getInstance().getNet().sendPacket(new PacketOutWindowClick(getLastWindowId(), (short)11, (byte)0, (short)1, 0, event.getSlotData().get(11)));
            delay(400);
            GommeCoinGenerator.getInstance().getNet().sendPacket(new PacketOutWindowClick(getLastWindowId(), (short)13, (byte)0, (short)1, 0, event.getSlotData().get(13)));
            delay(400);
            GommeCoinGenerator.getInstance().getNet().sendPacket(new PacketOutWindowClick(getLastWindowId(), (short)15, (byte)0, (short)1, 0, event.getSlotData().get(15)));
            delay(400);
            GommeCoinGenerator.getInstance().getNet().sendPacket(new PacketOutWindowClick(getLastWindowId(), (short)17, (byte)0, (short)1, 0, event.getSlotData().get(17)));
            delay(400);
            GommeCoinGenerator.getInstance().getNet().sendPacket(new PacketOutWindowClick(getLastWindowId(), (short)18, (byte)0, (short)1, 0, event.getSlotData().get(18)));
            delay(400);
            GommeCoinGenerator.getInstance().getNet().sendPacket(new PacketOutWindowClick(getLastWindowId(), (short)20, (byte)0, (short)1, 0, event.getSlotData().get(20)));
            delay(400);
            GommeCoinGenerator.getInstance().getNet().sendPacket(new PacketOutWindowClick(getLastWindowId(), (short)24, (byte)0, (short)1, 0, event.getSlotData().get(24)));
            delay(400);
            GommeCoinGenerator.getInstance().getNet().sendPacket(new PacketOutWindowClick(getLastWindowId(), (short)26, (byte)0, (short)1, 0, event.getSlotData().get(26)));
            this.rewardsClaimed = true;
            GommeCoinGenerator.getLog().info("Rewards claimed! Exit!");
            GommeCoinGenerator.getInstance().setRunning(false);
        }
    }

    @EventHandler
    public void onSpawnMob(SpawnMobEvent event) {
        if(event.getType() == 120) {
            this.villagerEid = event.getEID();
        }
    }

    private void delay(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
