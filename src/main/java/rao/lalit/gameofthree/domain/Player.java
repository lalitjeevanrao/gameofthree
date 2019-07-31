package rao.lalit.gameofthree.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import rao.lalit.gameofthree.enums.GamePlayAction;

/**
 * @author lalitrao
 *
 */
public class Player implements GamePlay {

    private static final Logger LOG = LoggerFactory.getLogger(Player.class);

    private String playerName;

    @JsonCreator
    public Player(@JsonProperty("playerName") String playerName) {
        this.playerName = playerName;
    }

    @Override
    public String playGame(String message) {
        String reply = GamePlayAction.IGNORE.name();
        if (GamePlayAction.WON.name().equals(message)) {
            return reply;
        }

        int number = Integer.parseInt(message);
        LOG.info("Recieved number: {}", number);
        
        validateInput(number);
        
        int nextNumber;
        if (number % 3 == 0) {
            nextNumber = number / 3;
            LOG.info("Adding {} and returning {}", 0, nextNumber);
        } else if ((number + 1) % 3 == 0) {
            nextNumber = (number + 1) / 3;
            LOG.info("Adding {} and returning {}", 1, nextNumber);
        } else {
            nextNumber = (number - 1) / 3;
            LOG.info("Substracting {} and returning {}", 1, nextNumber);
        }
        if (nextNumber == 1) {
            reply = GamePlayAction.WON.name();
        } else {
            reply = String.valueOf(nextNumber);
        }

        return reply;
    }

    private void validateInput(int number) {
        if (number < 0) {
            throw new RuntimeException("Expecting a whole number");
        }
        
        if (number == 0) {
            throw new RuntimeException("Cannot play with number 0");
        }
        
        if (number == 1) {
            throw new RuntimeException("Shouldnt have recieved 1 as the other player has already won");
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((playerName == null) ? 0 : playerName.hashCode());
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
        Player other = (Player) obj;
        if (playerName == null) {
            if (other.playerName != null)
                return false;
        } else if (!playerName.equals(other.playerName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Player [playerName=" + playerName + "]";
    }

}