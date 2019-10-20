/*
 * Created by David Luedtke (MrKinau)
 * 2019/10/18
 */

package systems.kinau.gommecoingenerator.event.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.kinau.gommecoingenerator.event.Event;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
public class UpdatePlayerListEvent extends Event {

    @Getter private Set<UUID> players;
}
