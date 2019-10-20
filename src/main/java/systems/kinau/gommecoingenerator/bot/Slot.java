package systems.kinau.gommecoingenerator.bot;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Slot {

    @Getter private short itemId;
    @Getter private byte count;
    @Getter private short damage;
    @Getter private byte[] nbtData;
}
