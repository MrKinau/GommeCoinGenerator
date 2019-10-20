/*
 * Created by David Luedtke (MrKinau)
 * 2019/5/3
 */

package systems.kinau.gommecoingenerator;

import lombok.Getter;
import lombok.Setter;
import systems.kinau.gommecoingenerator.auth.AuthData;
import systems.kinau.gommecoingenerator.auth.Authenticator;
import systems.kinau.gommecoingenerator.bot.Player;
import systems.kinau.gommecoingenerator.event.EventManager;
import systems.kinau.gommecoingenerator.io.LogFormatter;
import systems.kinau.gommecoingenerator.io.SettingsConfig;
import systems.kinau.gommecoingenerator.io.discord.DiscordMessageDispatcher;
import systems.kinau.gommecoingenerator.modules.*;
import systems.kinau.gommecoingenerator.network.ping.ServerPinger;
import systems.kinau.gommecoingenerator.network.protocol.NetworkHandler;
import systems.kinau.gommecoingenerator.network.protocol.ProtocolConstants;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GommeCoinGenerator {

    public static final String PREFIX = "GommeCoinGenerator v2.5 - ";
    @Getter private static GommeCoinGenerator instance;
    @Getter public static Logger log = Logger.getLogger(GommeCoinGenerator.class.getSimpleName());

    @Getter @Setter private boolean running;
    @Getter private SettingsConfig config;
    @Getter private DiscordMessageDispatcher discord;
    @Getter @Setter private int serverProtocol = ProtocolConstants.MINECRAFT_1_12_2; //default 1.8
    @Getter @Setter private String serverHost;
    @Getter @Setter private int serverPort;
    @Getter @Setter private AuthData authData;
    @Getter         private EventManager eventManager;
    @Getter         private Player player;
    @Getter         private ClientDefaultsModule clientModule;

    @Getter         private Socket socket;
    @Getter         private NetworkHandler net;

    private File logsFolder = new File("logs");

    public GommeCoinGenerator() {
        instance = this;

        //Initialize Logger
        log.setLevel(Level.ALL);
        ConsoleHandler ch;
        log.addHandler(ch = new ConsoleHandler());
        log.setUseParentHandlers(false);
        LogFormatter formatter = new LogFormatter();
        ch.setFormatter(formatter);

        //Generate/Load config
        this.config = new SettingsConfig();

        //Set logger file handler
        try {
            FileHandler fh;
            if(!logsFolder.exists() && !logsFolder.mkdir() && logsFolder.isDirectory())
                throw new IOException("Could not create logs folder!");
            log.addHandler(fh = new FileHandler("logs/log%g.log", 0 /* 0 = infinity */, getConfig().getLogCount()));
            fh.setFormatter(new LogFormatter());
        } catch (IOException e) {
            System.err.println("Could not create log!");
            System.exit(1);
        }

        //Authenticate player if online-mode is set
        authenticate();

//        String ip = "127.0.0.1";
        String ip = "mc.gommehd.net";
        int port = 25565;
//        int port = 25565;

        //Ping server
        getLog().info("Pinging " + ip + ":" + port + " with protocol of MC-" + getConfig().getDefaultProtocol());
        ServerPinger sp = new ServerPinger(ip, port, this);
        sp.ping();

        //Activate Discord webHook
        if(!getConfig().getWebHook().equalsIgnoreCase("false"))
            this.discord = new DiscordMessageDispatcher(getConfig().getWebHook());
    }

    public void start() {
        if(isRunning())
            return;
        connect();
    }

    private boolean authenticate() {
        Authenticator authenticator = new Authenticator(getConfig().getUserName(), getConfig().getPassword());
        AuthData authData = authenticator.authenticate();
        if(authData == null) {
            setAuthData(new AuthData(null, null, null, getConfig().getUserName()));
            return false;
        }
        setAuthData(authData);
        return true;
    }

    private void connect() {
        String serverName = getServerHost();
        int port = getServerPort();

        do {
            try {
                setRunning(true);
                this.socket = new Socket(serverName, port);

                //Load EventManager
                this.eventManager = new EventManager();

                getLog().info(getServerHost());
                this.net = new NetworkHandler();

                new HandshakeModule(serverName, port).enable();
                new LoginModule(getAuthData().getUsername()).enable();
                if (getConfig().isProxyChat())
                    new ChatProxyModule().enable();
                this.clientModule = new ClientDefaultsModule();
                getClientModule().enable();
                this.player = new Player();
                new GommeModule().enable();

                while (running) {
                    try {
                        net.readData();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        getLog().warning("Could not receive packet! Shutting down!");
                        break;
                    }
                }
            } catch (IOException e) {
                getLog().severe("Could not start bot: " + e.getMessage());
            } finally {
                try {
                    if (socket != null)
                        this.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.socket = null;
                this.net = null;
            }
            if (getConfig().isAutoReconnect()) {
                getLog().info("GommeCoinGenerator restarts in " + getConfig().getAutoReconnectTime() + " seconds...");
                try {
                    Thread.sleep(getConfig().getAutoReconnectTime() * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (getAuthData() == null) {
                    authenticate();
                }
            }
        } while (getConfig().isAutoReconnect());
    }
}
