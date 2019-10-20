/*
 * Created by David Luedtke (MrKinau)
 * 2019/10/18
 */

package systems.kinau.gommecoingenerator.event.play;

import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.kinau.gommecoingenerator.event.Event;

@AllArgsConstructor
public class JoinGameEvent extends Event {

    @Getter private int eid;
    @Getter private int gamemode;
    @Getter private int dimension;
    @Getter private int difficulty;
    @Getter private int maxPlayers;
    @Getter private int viewDistance;
    @Getter private String levelType;
    @Getter private boolean reducedDebugInfo;

}
