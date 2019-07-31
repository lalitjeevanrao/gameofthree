package rao.lalit.gameofthree.payload;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import rao.lalit.gameofthree.domain.Player;

public class Payload {
    
    private Player player;
    
    private String message;
    
    @JsonCreator
    public Payload(@JsonProperty("player") Player player, @JsonProperty("message") String message) {
        this.player = player;
        this.message = message;
    }

    public Player getPlayer() {
        return player;
    }
    
    public void setPlayer(Player player) {
        this.player = player;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        result = prime * result + ((player == null) ? 0 : player.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Payload other = (Payload) obj;
        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
            return false;
        if (player == null) {
            if (other.player != null)
                return false;
        } else if (!player.equals(other.player))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return "Payload [player=" + player + ", message=" + message + "]";
    }
}
