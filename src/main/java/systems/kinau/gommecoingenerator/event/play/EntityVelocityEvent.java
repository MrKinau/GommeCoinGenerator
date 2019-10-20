/*
 * Created by David Luedtke (MrKinau)
 * 2019/10/18
 */

package systems.kinau.gommecoingenerator.event.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.kinau.gommecoingenerator.event.Event;

@AllArgsConstructor
public class EntityVelocityEvent extends Event {

    @Getter private short x;
    @Getter private short y;
    @Getter private short z;
    @Getter private int eid;
}
