package systems.kinau.gommecoingenerator.event.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.kinau.gommecoingenerator.bot.Slot;
import systems.kinau.gommecoingenerator.event.Event;

import java.util.Map;

@AllArgsConstructor
public class WindowItemsEvent extends Event {

    @Getter private int windowId;
    @Getter private Map<Integer, Slot> slotData;


}
