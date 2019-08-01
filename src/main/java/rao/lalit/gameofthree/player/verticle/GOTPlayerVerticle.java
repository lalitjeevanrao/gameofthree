package rao.lalit.gameofthree.player.verticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.messages.MqttPublishMessage;
import rao.lalit.gameofthree.domain.GOTPlayer;
import rao.lalit.gameofthree.enums.GamePlayAction;
import rao.lalit.gameofthree.payload.Payload;
import rao.lalit.gameofthree.util.JSONBinder;

/**
 * 
 * @author lalitrao
 */
public class GOTPlayerVerticle extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(GOTPlayerVerticle.class);
    private static final String TOPIC_GAME_OF_THREE = "gameofthree/game";
    private static final String TOPIC_GAME_OF_THREE_PRESENCE = "gameofthree/presence1";
    private static final String TOPIC_GAME_OF_THREE_RESULT = "gameofthree/result";

    private final GOTPlayer player;
    private final MqttClient client;
    private final int numberToStartTheGame;

    public GOTPlayerVerticle(MqttClient client, GOTPlayer player, int numberToStartTheGame) {
        this.client = client;
        this.player = player;
        this.numberToStartTheGame = numberToStartTheGame;
        LOG.info("Creating player with name {}", this.player.getPlayerName());
    }

    /* (non-Javadoc)
     * @see io.vertx.core.AbstractVerticle#start()
     */
    @Override
    public void start() throws Exception {
        // Subscribe to Topics
        client.subscribe(TOPIC_GAME_OF_THREE, MqttQoS.EXACTLY_ONCE.value());
        client.subscribe(TOPIC_GAME_OF_THREE_PRESENCE, MqttQoS.AT_LEAST_ONCE.value());
        client.subscribe(TOPIC_GAME_OF_THREE_RESULT, MqttQoS.AT_LEAST_ONCE.value());

        // Adding handler which will be called each time server publish something to client
        client.publishHandler(this::handleIncomingMessage);
        // Adding exception handler for the client, that will be called when an error happens
        client.exceptionHandler(this::handleException);

        publishPresenceMessage();
    }

    private void publishPresenceMessage() {
        Payload payload = new Payload(player, "Hello");
        client.publish(TOPIC_GAME_OF_THREE_PRESENCE, Buffer.buffer(JSONBinder.toJSON(payload)), 
            MqttQoS.AT_LEAST_ONCE, false, false);
    }

    private void handleIncomingMessage(MqttPublishMessage message) {
        try {
            Payload presencePayload = JSONBinder.fromJSON(Payload.class, message.payload().toString());
            String playerName = presencePayload.getPlayer().getPlayerName();
            boolean isMyMessage = player.getPlayerName().equals(playerName);

            if (TOPIC_GAME_OF_THREE_PRESENCE.equals(message.topicName()) && !isMyMessage) {
                startGame(playerName);
            }
            if (TOPIC_GAME_OF_THREE.equals(message.topicName()) && !isMyMessage) {
                playGame(presencePayload);
            }
            if (TOPIC_GAME_OF_THREE_RESULT.equals(message.topicName())) {
                LOG.info("Player {} won the game!", playerName);
            }
        } catch (Exception e) {
            LOG.error("Exception in handleIncomingMessage. ", e);
        }
    }

    private void startGame(String playerName) {
        LOG.info("Player {} joined the game", playerName);
        LOG.info("Starting the game");
        
        Payload payload = new Payload(this.player, String.valueOf(numberToStartTheGame));
        client.publish(TOPIC_GAME_OF_THREE, Buffer.buffer(JSONBinder.toJSON(payload)), 
            MqttQoS.EXACTLY_ONCE, false, false);
    }

    private void playGame(Payload gamePayload) {
        try {
            String message = player.playGame(gamePayload.getMessage());
            
            if (GamePlayAction.WON.name().equals(message)) {
                Payload payload = new Payload(this.player, message);
                client.publish(TOPIC_GAME_OF_THREE_RESULT, Buffer.buffer(JSONBinder.toJSON(payload)), 
                    MqttQoS.AT_LEAST_ONCE, false, false);
            }
            
            if (!GamePlayAction.IGNORE.name().equals(message)) {
                Payload payload = new Payload(this.player, message);
                client.publish(TOPIC_GAME_OF_THREE, Buffer.buffer(JSONBinder.toJSON(payload)), 
                    MqttQoS.EXACTLY_ONCE, false, false);
            }
        } catch (Exception e) {
            LOG.error("Exception in playGame(). ", e);
        }
    }

    private void handleException(Throwable t) {
        LOG.error("Error in MQTTClient. ", t);
    }

    @Override
    public void stop() throws Exception {
        client.disconnect();
    }
}
