import java.util.*;
public class Strats {
	private int betSize;
	private int startingBet;
	private int startingMoney;
	private int decksInPlay;
	public BlackJackPlayer cardGame;
	private int currWeight;
	
	//Initializes the Strats object
	//takes an int of the bet you want to start with and an int with the starting money
	//Creates a JackBlackPlayer object with the starting money
	public Strats(int money, int bet, int deckSize) {
		betSize = bet;
		startingBet = bet;
		startingMoney = money;
		decksInPlay = deckSize;
		currWeight = 0;
		cardGame = new BlackJackPlayer(startingMoney, startingBet, decksInPlay);
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
	public void userPlays(BlackJackPlayer cardGame) {
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
	
	//places bets based off of the weight of the deck
	//2-6 = +1 7-9 = 0 10-A = -1
	//if points are higher bet is higher, if points are lower bet is lowered
	//pass in two integers and increaseVal that increases the bet by a factor
	//times the weight of the deck and a decreaseVal. That decreases the deck
	//by the times of the weight of the deck. 
	//the weight of the deck is also divided by the amount of decks in play
	public void weightedDeckBet(int increaseVal, int decreaseVal) {
		findWeightedDeck();
		if(currWeight > 0) {
			betSize = (currWeight*increaseVal + startingBet);
		} else if(currWeight < 0) {
			betSize = startingBet - currWeight*decreaseVal;
		}
		else {
			betSize = startingBet;
		}
		cardGame.setBet(betSize);
	}
	
	private void findWeightedDeck() {
		//when deck is reshuffled the point system is reset
		int holder = 0;
		if(cardGame.getDeckShuffled()) {
			currWeight = 0;
		}
		if(cardGame.getDealerHand() != null) {
			holder += createWeights(cardGame.getDealerHand());
			holder += createWeights(cardGame.getUserHand());
		}
		currWeight += holder;
	}
	
	//returns an integer of total weight of a list of cards given
	private int createWeights(List<Integer> cards) {
		int value = 0;
		for(int i = 0; i < cards.size();i++) {
			if(cards.get(i) < 7) {
				value ++;
			} else if(cards.get(i) > 9) {
				value --;
			}
		}
		return value;
	}
	
	//runs the loop and holds all the information for each round and how many are left
	//allows the automation version to just focus on single round logic
	//asks for integer of how many rounds to run, integer for the bettingStrat
	//1 = same bet every round, 2 = martigale, 3 = weightedBet(counting cards)
	//asks for integer automation strat
	//1 = the basic online chart strat
	//asks for double to multiple bets by depending on weight(Currently only for weightedBet)
	//asks for double to divide bets by depending on weight(Currently only for weightedBet)
	//TODO: bet number is off by one :(
	public void autoRounds(int amountOfRounds, int bettingStrat, int autoStrat, int increaseVal, int decreaseVal) {
		int roundsPlayed = 0;
		while(roundsPlayed < amountOfRounds && cardGame.getMoney() > cardGame.getBet()){
			cardGame.startingHand();
			if(autoStrat == 1) {
				basicAutoChart();
			}//more strats coming soon!!
			cardGame.whoWon();
			
			if(bettingStrat == 2) {
				autoMartigale();
			} else if(bettingStrat == 3) {
				weightedDeckBet(increaseVal, decreaseVal);
			}
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
