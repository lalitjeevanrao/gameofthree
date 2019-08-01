package rao.lalit.gameofthree.config;

public class ConfigStore {

	private String playerName;
	
	private int numberToBegin;
	
	public ConfigStore(String playerName, int numberToBegin) {
		this.playerName = playerName;
		this.numberToBegin = numberToBegin;
	}

	public String getPlayerName() {
		return playerName;
	}
	
	public int getNumberToBegin() {
		return numberToBegin;
	}
	
}
