/*
 * Created by David Luedtke (MrKinau)
 * 2019/5/5
 */

/*
 * Created by Summerfeeling on May, 5th 2019
 */

package systems.kinau.gommecoingenerator.network.protocol.play;

import com.google.common.io.ByteArrayDataOutput;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import systems.kinau.gommecoingenerator.GommeCoinGenerator;
import systems.kinau.gommecoingenerator.event.play.ChatEvent;
import systems.kinau.gommecoingenerator.network.protocol.NetworkHandler;
import systems.kinau.gommecoingenerator.network.protocol.Packet;
import systems.kinau.gommecoingenerator.network.utils.ByteArrayDataInputWrapper;
import systems.kinau.gommecoingenerator.network.utils.TextComponent;

public class PacketInChat extends Packet {

	@Getter private String text;
	private final JsonParser PARSER = new JsonParser();

	@Override
	public void write(ByteArrayDataOutput out, int protocolId) {
		//Only incoming packet
	}
	
	@Override
	public void read(ByteArrayDataInputWrapper in, NetworkHandler networkHandler, int length, int protocolId) {
		this.text = readString(in);
		try {
			JsonObject object = PARSER.parse(text).getAsJsonObject();
			this.text = TextComponent.toPlainText(object);
		} catch (IllegalStateException ignored) {
			//Ignored
		}

		GommeCoinGenerator.getInstance().getEventManager().callEvent(new ChatEvent(getText()));
	}
}
