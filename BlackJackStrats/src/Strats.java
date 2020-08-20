
public class Strats {
	
	public static void main(String args[]) {
		JackBlackPlayer cardGame = new JackBlackPlayer(5000);
		while(cardGame.getplaysGame()) {
			cardGame.newGame();
			cardGame.userPlaying();
			cardGame.whoWon();
			cardGame.setPlaysGame();
		}
	}
	
	//This is the Strat where you double your bet.
	//It is not dependent on ShuffleType
	public void martigale() {
		
	}

}
