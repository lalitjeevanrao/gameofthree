package rao.lalit.gameofthree.domain.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import rao.lalit.gameofthree.domain.GOTPlayer;
import rao.lalit.gameofthree.domain.Player;
import rao.lalit.gameofthree.enums.GamePlayAction;

/**
 * @author lalitrao
 *
 */
public class GamePlayTest {
    
    private Player player = new GOTPlayer("some player name");
    
    @Rule
    public final ExpectedException exception = ExpectedException.none();
    
    @Test
    public void testPlayGameForIgnore() {
        String result = player.playGame(GamePlayAction.WON.name());
        assertEquals(GamePlayAction.IGNORE.name(), result);
    }

    @Test
    public void testWhenMessageIsFour() {
        String result = player.playGame("4");
        assertEquals(GamePlayAction.WON.name(), result);
    }

    @Test
    public void testWhenPlayerWon() {
        String result = player.playGame("3");
        assertEquals(GamePlayAction.WON.name(), result);
    }

    @Test
    public void testWhenMessageIsOne() {
        exception.expect(RuntimeException.class);
        player.playGame("1");
    }
    
    @Test
    public void testWhenMessageIsZero() {
        exception.expect(RuntimeException.class);
        player.playGame("0");
    }
    
    @Test
    public void testWhenMessageIsNegative() {
        exception.expect(RuntimeException.class);
        player.playGame("-1");
    }
    
    @Test
    public void testWhenMessageIsFiftySix() {
        String result = player.playGame("56");
        assertEquals("19", result);
    }
    
    @Test
    public void testWhenMessageIsNineteen() {
        String result = player.playGame("19");
        assertEquals("6", result);
    }
    
    @Test
    public void testWhenMessageIsSix() {
        String result = player.playGame("6");
        assertEquals("2", result);
    }
    
    @Test
    public void testWhenMessageIsTwo() {
        String result = player.playGame("2");
        assertEquals(GamePlayAction.WON.name(), result);
    }
    
    @Test
    public void testForNotEquals() {
        GOTPlayer player1 = new GOTPlayer("player1");
        GOTPlayer player2 = new GOTPlayer("player2");
        assertNotEquals(Boolean.TRUE, player1.equals(player2));
    }
    
    @Test
    public void testForEquals() {
        GOTPlayer player1 = new GOTPlayer("player1");
        GOTPlayer player2 = new GOTPlayer("player1");
        assertEquals(Boolean.TRUE, player1.equals(player2));
    }
    
    @Test
    public void testForHashCode() {
        GOTPlayer player1 = new GOTPlayer("player1");
        GOTPlayer player2 = new GOTPlayer("player1");
        assertEquals(Boolean.TRUE, player1.hashCode() == player2.hashCode());
    }
}
