package rao.lalit.gameofthree.main;

import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import rao.lalit.gameofthree.domain.Player;
import rao.lalit.gameofthree.player.verticle.PlayerVerticle;

/**
 * @author lalitrao
 *
 */
public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final String MQTT_BROKER = "broker.hivemq.com";

    /**
     * @param args
     */
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        // Force Logging to Slf4JLoggerFactory
        System.setProperty("vertx.logger-delegate-factory-class-name", 
            "io.vertx.core.logging.SLF4JLogDelegateFactory");

        MqttClient client = MqttClient.create(vertx);

        client.connect(MqttClientOptions.DEFAULT_PORT, MQTT_BROKER, handler -> {
            if (handler.failed()) {
                LOG.error("Failed to connect to MQTT broker");
            } else {
                LOG.info("Successfully connected to MQTT Broker. Code: {}", handler.result().code().toString());

                Verticle v1 = new PlayerVerticle(client, new Player(UUID.randomUUID().toString()));
                vertx.deployVerticle(v1);
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                Set<String> deployedVerticleIds = vertx.deploymentIDs();
                deployedVerticleIds.forEach(vertx::undeploy);
                client.disconnect();
                vertx.close();
            }
        });

    }
}
