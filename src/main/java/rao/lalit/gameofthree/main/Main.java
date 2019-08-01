package rao.lalit.gameofthree.main;

import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import rao.lalit.gameofthree.config.ConfigStore;
import rao.lalit.gameofthree.domain.GOTPlayer;
import rao.lalit.gameofthree.player.verticle.GOTPlayerVerticle;

/**
 * The entry point of the game.<br>
 * Defines a {@link #main(String[])} method
 * 
 * @author lalitrao
 */
public class Main {
	private static final Logger LOG = LoggerFactory.getLogger(Main.class);
	private static final String MQTT_BROKER = "broker.hivemq.com";

	/**
	 * Initiates gameplay configuration and setting up 
	 * of {@link Vertx} and {@link MqttClient} <br>
	 * 
	 * @author lalitrao
	 * */
	public static void main(String[] args) {
		new Main().initialise();
	}

	private void initialise() {
		ConfigStore store = configureGamePlay();

		Vertx vertx = Vertx.vertx();
		// Force Logging to Slf4JLoggerFactory
		System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.SLF4JLogDelegateFactory");

		// Creating MQTT client using default options.
		MqttClient client = MqttClient.create(vertx);
		
		deployVerticle(store, vertx, client);
		// Clean up to do before shutting down the system
		addShutdownHook(vertx, client);
	}

	private void addShutdownHook(Vertx vertx, MqttClient client) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				LOG.info("----------- Initiating ShutdownHook -----------");
				Set<String> deployedVerticleIds = vertx.deploymentIDs();
				deployedVerticleIds.forEach(vertx::undeploy);
				client.disconnect();
				LOG.info("Disconnected MQTT Client");
				vertx.close();
				LOG.info("Closed Vertx instance");
			}
		});
	}

	private void deployVerticle(ConfigStore store, Vertx vertx, MqttClient client) {
		Verticle verticle = new GOTPlayerVerticle(client, 
				new GOTPlayer(store.getPlayerName()), store.getNumberToBegin());
		
		client.connect(MqttClientOptions.DEFAULT_PORT, MQTT_BROKER, handler -> {
			if (handler.failed()) {
				LOG.error("Failed to connect to MQTT broker");
			} else {
				LOG.info("Successfully connected to MQTT Broker. Code: {}", handler.result().code().toString());
				vertx.deployVerticle(verticle);
			}
		});
	}

	private ConfigStore configureGamePlay() {
		Scanner sc = new Scanner(System.in);
		try {
			LOG.info("Enter 1 for auto play, any other number for manual play");
			int option = Integer.parseInt(sc.nextLine());
			switch (option) {
			case 1:
				return configureAutoPlay();
			default:
				return configureManualPlay(sc);
			}
		} finally {
			sc.close();
		}
	}

	private ConfigStore configureManualPlay(Scanner sc) {
		LOG.info("Enter the name of the player");
		String playerName = sc.nextLine();
		LOG.info("Enter the number to begin play");
		int numberToPlay = Integer.parseInt(sc.nextLine());
		LOG.info("Player name {} and number {}", playerName, numberToPlay);
		return new ConfigStore(playerName, numberToPlay);
	}

	private ConfigStore configureAutoPlay() {
		String playerName = UUID.randomUUID().toString();
		int numberToPlay = new Random().nextInt(Integer.MAX_VALUE);
		LOG.info("Player name {} and number {}", playerName, numberToPlay);
		return new ConfigStore(playerName, numberToPlay);
	}
}
