package rao.lalit.gameofthree.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import rao.lalit.gameofthree.enums.GamePlayAction;

/**
 * A concrete class implementing {@link Player} for 'Game of three' game
 * 
 * @author lalitrao
 */
public class GOTPlayer implements Player {

    private static final Logger LOG = LoggerFactory.getLogger(GOTPlayer.class);

    private String playerName;

    @JsonCreator
    public GOTPlayer(@JsonProperty("playerName") String playerName) {
        this.playerName = playerName;
    }

    /**
     * Method holding business logic for Game of Three gameplay.<br>
     * It choose between adding one of {-1, 0, 1} to get to a number that is divisible by 3, divides it by three. <br>
     * The resulting whole number is then returned.
     * 
     * @param message A whole number or @
     * @return Either the number divisible by 3 or {@link GamePlayAction}
     * 
     * @author lalitrao
     * */
    @Override
    public String playGame(String message) {
        String reply = GamePlayAction.IGNORE.name();
        if (GamePlayAction.WON.name().equals(message)) {
            return reply;
        }

        int number = Integer.parseInt(message);
        
        validateInput(number);
        
        int nextNumber;
        if (number % 3 == 0) {
            nextNumber = number / 3;
            LOG.info("Recieved number: {} Adding {} and returning {}", number, 0, nextNumber);
        } else if ((number + 1) % 3 == 0) {
            nextNumber = (number + 1) / 3;
            LOG.info("Recieved number: {} Adding {} and returning {}", number, 1, nextNumber);
        } else {
            nextNumber = (number - 1) / 3;
            LOG.info("Recieved number: {} Substracting {} and returning {}", number, 1, nextNumber);
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
        GOTPlayer other = (GOTPlayer) obj;
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
