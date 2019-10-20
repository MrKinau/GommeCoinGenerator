/*
 * Created by David Luedtke (MrKinau)
 * 2019/10/18
 */

package systems.kinau.gommecoingenerator.event.play;

import com.google.common.io.ByteArrayDataOutput;
import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.kinau.gommecoingenerator.event.Event;

@AllArgsConstructor
public class UpdateSlotEvent extends Event {

    @Getter private int windowId;
    @Getter private short slotId;
    @Getter private ByteArrayDataOutput slotData;

}
