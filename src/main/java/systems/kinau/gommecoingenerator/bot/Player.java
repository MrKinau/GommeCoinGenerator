/*
 * Created by David Luedtke (MrKinau)
 * 2019/10/18
 */

package systems.kinau.gommecoingenerator.bot;

import com.google.common.io.ByteArrayDataOutput;
import lombok.Getter;
import lombok.Setter;
import systems.kinau.gommecoingenerator.GommeCoinGenerator;
import systems.kinau.gommecoingenerator.event.EventHandler;
import systems.kinau.gommecoingenerator.event.Listener;
import systems.kinau.gommecoingenerator.event.play.PosLookChangeEvent;
import systems.kinau.gommecoingenerator.event.play.SetHeldItemEvent;
import systems.kinau.gommecoingenerator.event.play.UpdateExperienceEvent;
import systems.kinau.gommecoingenerator.event.play.UpdateSlotEvent;
import systems.kinau.gommecoingenerator.network.protocol.ProtocolConstants;
import systems.kinau.gommecoingenerator.network.protocol.play.PacketOutTeleportConfirm;

public class Player implements Listener {

    @Getter @Setter private double x;
    @Getter @Setter private double y;
    @Getter @Setter private double z;
    @Getter @Setter private float yaw;
    @Getter @Setter private float pitch;

    @Getter @Setter private int experience;
    @Getter @Setter private int levels;

    @Getter @Setter private int heldSlot;
    @Getter @Setter private ByteArrayDataOutput slotData;

    public Player() {
        GommeCoinGenerator.getInstance().getEventManager().registerListener(this);
    }

    @EventHandler
    public void onPosLookChange(PosLookChangeEvent event) {
        this.x = event.getX();
        this.y = event.getY();
        this.z = event.getZ();
        this.yaw = event.getYaw();
        this.pitch = event.getPitch();
        if (GommeCoinGenerator.getInstance().getServerProtocol() >= ProtocolConstants.MINECRAFT_1_9)
            GommeCoinGenerator.getInstance().getNet().sendPacket(new PacketOutTeleportConfirm(event.getTeleportId()));

    }

    @EventHandler
    public void onUpdateXP(UpdateExperienceEvent event) {
        this.levels = event.getLevel();
        this.experience = event.getExperience();
    }

    @EventHandler
    public void onSetHeldItem(SetHeldItemEvent event) {
        this.heldSlot = event.getSlot();
    }

    @EventHandler
    public void onUpdateSlot(UpdateSlotEvent event) {
        if(event.getWindowId() != 0)
            return;
        if(event.getSlotId() != getHeldSlot())
            return;
        this.slotData = event.getSlotData();
    }
}
