package rao.lalit.gameofthree.service;

import io.vertx.mqtt.MqttClient;
import rao.lalit.gameofthree.domain.GamePlay;

public class GameOfThree {
    public final MqttClient mqttClient;

    public GameOfThree(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    };
    
    public void playGame(GamePlay gp) {
        
    }
}
