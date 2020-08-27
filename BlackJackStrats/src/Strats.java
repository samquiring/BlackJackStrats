
public class Strats {
	private int betSize;
	private int startingBet;
	private int startingMoney;
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
	
	public int getBetSize() {
		return betSize;
	}
	
	public int getStartingBet() {
		return startingBet;
	}
	
	public int getStartingMoney() {
		return startingMoney;
	}
	
	public void setBetSize(int bet) {
		betSize = bet;
	}
	
	//Call this for the user to play a game manually
	public void userPlays(JackBlackPlayer cardGame) {
		cardGame.newGame();
		cardGame.userPlaying();
		cardGame.whoWon();
	}
	
	//This is the Strat where you double your bet.
	//It is not dependent on ShuffleType
	public void autoMartigale() {
		int holder = cardGame.compareUser();
		if(holder > 0) {
			betSize = startingBet;
		} else if(holder < 0) {
			betSize = betSize*2;
		}
		cardGame.setBet(betSize);
	}
	
	//runs the loop and holds all the information for each round and how many are left
	//allows the automation version to just focus on single round logic
	//TODO: bet number is off :(
	public void autoRounds(int amountOfRounds, int bettingStrat, int autoStrat) {
		int roundsPlayed = 0;
		while(roundsPlayed < amountOfRounds && cardGame.getMoney() > cardGame.getBet()){
			cardGame.startingHand();
			if(autoStrat == 1) {
				basicAutoChart();
			}//more strats coming soon!!
			if(bettingStrat == 2) {
				autoMartigale();
			} //More betting strats coming soon!!
			cardGame.whoWon();
			cardGame.newGame();
			roundsPlayed += 1;
			System.out.println("current round: " + roundsPlayed);
			System.out.println("current total money: $" + cardGame.getMoney());
			System.out.println("current bet: $" + betSize);
			System.out.println();
		}
		if(!(cardGame.getMoney() > cardGame.getBet())){
			System.out.println("Your bet is currently more than the money that you have. I'd just call it");
			System.out.println("You only have $" + cardGame.getMoney() + " and you tried to bet $" + cardGame.getBet());
		} else {
			System.out.println("Congrats!! You finished " + amountOfRounds + " rounds and ended up with $" + cardGame.getMoney());
		}
	}
	//this is that super basic strat that you see everywhere with the chart
	//no card counting required just basic memorization
	//takes an integer of how many rounds you want to go until,
	//and an integer for what betting strategy you want to employ
	//currently: 1 = Same bet every hand, 2 = Martigale 
	//Not dependent on ShuffleType
	//for user hand 11 and under hit
	//12 if dealer has 2,3 or greater than 7 hit
	//13-16 only hit if the dealer has a 7 or higher showing
	//Ace and 2-6 always hit
	//Ace 7 hit if dealer has a 9 or above
	//all other plays stay
	private void basicAutoChart() {
		boolean stand = false;
		cardGame.setTotal();
		int dealer = cardGame.getDealerCardInt();
		while(stand) {
			if(!cardGame.userAce) {
				if(cardGame.handTotal < 17) {
					if(cardGame.handTotal > 12 && dealer < 7) {
						if(dealer == 2 || dealer == 3) {
							cardGame.hit(cardGame.IS_USER);
						} else {
							stand = true;
						}
					} else {
						cardGame.hit(cardGame.IS_USER);
					}
				} else {
					stand = true;
				}
			} else {
				if(cardGame.handTotal < 8) {
					cardGame.hit(cardGame.IS_USER);
				}
				else if(cardGame.handTotal == 8 && dealer > 8) {
					cardGame.hit(cardGame.IS_USER);
				} else {
					stand = true;
				}
			}
		}
		
	}

}
