package systems.kinau.gommecoingenerator.event.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.kinau.gommecoingenerator.event.Event;

@AllArgsConstructor
public class OpenWindowEvent extends Event {

    @Getter private int windowId;
    @Getter private String windowType;
    @Getter private String windowTitle;
    @Getter private int slotCount;
    @Getter private int eId;

}
