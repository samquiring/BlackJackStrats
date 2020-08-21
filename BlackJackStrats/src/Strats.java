
public class Strats {
	public int betSize = 100;
	public int startingBet = 100;
	public int startingMoney;
	public JackBlackPlayer cardGame;
	
	//Initializes the Strats object
	//takes an int of the bet you want to start with and an int with the starting money
	//Creates a JackBlackPlayer object with the starting money
	public Strats(int money, int bet) {
		betSize = bet;
		startingBet = bet;
		startingMoney = money;
		cardGame = new JackBlackPlayer(startingMoney, startingBet);
	}
	
	//Call this for the user to play a game manually
	public void userPlays(JackBlackPlayer cardGame) {
		cardGame.newGame();
		cardGame.userPlaying();
		cardGame.whoWon();
	}
	
	//This is the Strat where you double your bet.
	//It is not dependent on ShuffleType
	public void autoMartigale(JackBlackPlayer cardGame) {
		while(cardGame.getplaysGame()) {
			userPlays(cardGame);
			int holder = cardGame.compareUser();
			if(holder > 0) {
				betSize = startingBet;
			} else if(holder < 0) {
				betSize = betSize*2;
			}
			cardGame.setBet(betSize);
			cardGame.setPlaysGame();
		}
	}

}
