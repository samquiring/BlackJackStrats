import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

//TODO: prevent player from going negitive in money
public class BlackJackPlayer {
	
	private List<Integer> currHand;
	private List<Integer> dealerHand;
	private List<Integer> cardsPlayed;
	private int shuffleType;
	private Scanner myScanner;
	private boolean playsGame;
	private int money;
	private int bet;
	public int handTotal;
	public int altHandTotal;
	public boolean userAce;
	private int endDealerTotal;
	private int endUserTotal;
	private int deckSize;
	final boolean IS_USER = true;
	private boolean deckShuffled;
	private boolean endRound;
	
	
	//Initializes the game of blackJack
	//Pass in an integer of how much money the user 
	//will start with
	public BlackJackPlayer(int startingAmount, int startingBet, int deckSizeSet) {
		money = startingAmount;
		handTotal = 0;
		//TODO: figure out best way to ask user for deckSize
		deckSize = deckSizeSet;
		altHandTotal = 0;
		bet = startingBet;
		endDealerTotal = 0;
		endUserTotal = 0;
		currHand = new ArrayList<Integer>();
		dealerHand = new ArrayList<Integer>();
		cardsPlayed = new ArrayList<Integer>();
		myScanner = new Scanner(System.in);
		shuffleType = this.shuffleType();
		deckShuffled = false;
		endRound = false;
		this.setPlaysGame();
	}
	
	//lets the user set the betSize between each hand
	//TODO: protect the user from changing bet size mid hand
	public void setBet(int betSize) {
		bet = betSize;
	}	
	
	//returns a boolean that is true if the user wants to play one round
	public boolean getplaysGame() {
		return playsGame;
	}
	
	//clears out the previous hand from the game to start a new one
	public void newGame() {
		currHand.clear();
		dealerHand.clear();
		endRound = false;
	}
	
	public int getBet() {
		return bet;
	}
	//returns the amount of money the user has left
	public int getMoney() {
		return money;
	}
	
	public boolean getDeckShuffled() {
		return deckShuffled;
	}
	//shows the dealers second card so the player can bet accordingly
	//returns an int(this is for the computer to read)
	public int getDealerCardInt() {
		return dealerHand.get(1);
	}
	
	//if the round is over returns a list of the dealers hand
	//otherwise returns null
	public List<Integer> getDealerHand(){
		if(endRound) {
			return dealerHand;
		}
		System.out.println("please wait until the end of round to see the dealers hand.");
		return null;
	}
	
	//returns a list of the players hand
	public List<Integer> getPlayerHand(){
		return currHand;
	}
	
	//shows the dealers second card so the player can bet accordingly
	//returns a string for the user to look at
	public String getDealerCardStr() {
		if(!dealerHand.isEmpty()) {
			if(dealerHand.get(1) > 10) {
				if(dealerHand.get(1) == 11) {
					return ("Jack");
				} else if(dealerHand.get(1) == 12) {
					return ("Queen");
				} else if(dealerHand.get(1) == 13) {
					return ("King");
				} else{
					return ("Ace");
				}
			} else {
				return String.valueOf(dealerHand.get(1));
			}
		}
		return null;
	}
	
	//prompts the user to choose what type of shuffling that it wants to use
	//Currently fully random is available which simulates a game with
	//infinite decks 
	//TODO: create auto shuffle deck and create hand shuffled deck
	private int shuffleType() {
		System.out.println("What type of shuffling do you want?");
		System.out.println("1 = autoShuffle - simulates infinte decks so equal probability of any card");
		System.out.println("2 = handShuffled - simulates playing till end of all decks in play."
				+ "So each hand has a different probability");
		System.out.println("3 = AutoShuffler - simulates an autoShuffler."
				+ " So each hand the deck(s) is reshuffled");
		int shuffle;
		//TODO: error trap for numbers that aren't tied to shuffleTypes
		shuffle = myScanner.nextInt();
		myScanner.nextLine();
		return (shuffle);
		
	}
	
	//prompts the user if they want to play a game manually
	public void setPlaysGame() {
		System.out.println("would you like to play a game?");
		System.out.println("y/n");
		//TODO: error trap for things other than Y or N
		String holder = myScanner.nextLine();
		if(holder.equalsIgnoreCase("y")) {
			playsGame = true;
		} else {
			playsGame = false;
		}
	}
	//simulates a deck that is perfectly shuffled every hand i.e true randomness
	private int autoShuffle() {
		//always a new deck
		deckShuffled = true;
		int min = 2;
		//Jack = 11, Queen = 12, King = 13, Ace = 14
		int max = 14;
		int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
		return (randomNum);
		
	}
	
	//Simulates a deck that is shuffled then played until no cards are left then shuffled
	private int shuffledDeck() {
		int min = 0;
		//creates a possible card from 0 to the deck max
		int max = 51 * deckSize;
		int randomNumb = ThreadLocalRandom.current().nextInt(min, max + 1);
		//"shuffles" the deck after all cards have been picked
		if(!cardsPlayed.isEmpty() && cardsPlayed.size() == 52) {
			cardsPlayed.clear();
			deckShuffled = true;
		} else {
			deckShuffled = false;
		}
		//keeps running until it gets a randomNumb that hasn't been played
		while(!cardsPlayed.isEmpty() && cardsPlayed.contains(randomNumb)) {
			randomNumb = ThreadLocalRandom.current().nextInt(min, max + 1);
		}
		cardsPlayed.add(randomNumb);
		return(randomNumb/(4*deckSize) + 2);
	}
	
	//simulates an autoShuffler at a Casino
	//or a person shuffling after every hand
	private int autoShufflerDeck() {
		int min = 0;
		//creates a possible card from 0 to the deck max
		int max = 51 * deckSize;
		int randomNumb = ThreadLocalRandom.current().nextInt(min, max + 1);
		
		//Checks if it is the beginning of the hand by seeing if userHand is empty
		//if so it removes all cardsPlayed making all hands possible agian
		if(!cardsPlayed.isEmpty() && currHand.isEmpty()) {
			cardsPlayed.clear();
			deckShuffled = true;
		} else {
			deckShuffled = false;
		}
		//keeps running until it gets a randomNumb that hasn't been played
		while(!cardsPlayed.isEmpty() && cardsPlayed.contains(randomNumb)) {
			randomNumb = ThreadLocalRandom.current().nextInt(min, max + 1);
		}
		cardsPlayed.add(randomNumb);
		return(randomNumb/(4*deckSize) + 2);
	}
	
	//This gives the player or dealer a random card based on the shuffle type
	public void hit(boolean isUser) {
		if(isUser) {
			if(shuffleType == 1) {
				currHand.add(autoShuffle());
			} else if(shuffleType == 2) {
				currHand.add(shuffledDeck());
			} else if(shuffleType == 3) {
				currHand.add(autoShufflerDeck());
			}
		} else {
			if(shuffleType == 1) {
				dealerHand.add(autoShuffle());
			} else if(shuffleType == 2) {
				dealerHand.add(shuffledDeck());
			} else if(shuffleType == 3) {
				dealerHand.add(autoShufflerDeck());
			}
		}
	}
	
	//returns the a string of current hand
	//Accepts a list of numbers from 2-14
	//with 11-14 representing face Cards
	private String getCurrHand(List<Integer> list) {
		String holder = "";
		for(int i = 0; i < list.size(); i++) {
			int curr = list.get(i);
			if(curr > 10) {
				if(curr == 11) {
					holder += "Jack ";
				} else if(curr == 12) {
					holder += "Queen ";
				} else if(curr == 13) {
					holder += "King ";
				} else{
					holder += "Ace ";
				}
			} else {
				holder += curr + " ";
			}
		}
		return holder;
	}
	
	public List<Integer> getUserHand() {
		return currHand;
	}
	
	//sets the users hand total and Alt hand total
	//if an ace is present
	public void setTotal() {
		int count = 0;
		handTotal = 0;
		altHandTotal = 0;
		for(int i = 0; i < currHand.size(); i++) {
			if(currHand.get(i) < 11) {
				count+= currHand.get(i);
			} else if(currHand.get(i) > 10 && currHand.get(i) < 14) {
				count += 10;
			} else {
				count += 1;
				altHandTotal = 10;
			}
		}
		handTotal = count;
		altHandTotal += count;
		if(altHandTotal > 21) {
			altHandTotal = handTotal;
		}
		if(altHandTotal != handTotal) {
			userAce = true;
		}
		
	}
	//prints the current total of your hand
	public int printTotal() {
		System.out.println("Your current total is: ");
		if(handTotal > 21) {
			System.out.println(handTotal + "(Bust)");
		}
		if(altHandTotal == 21 || handTotal == 21) {
			System.out.println("21");
			return 21;
		} else if(altHandTotal != handTotal) {
			System.out.println("Either: " + handTotal + " or: " + altHandTotal);
		} else {
			System.out.println(handTotal);
		}
		return handTotal;
	}
	public void startingHand() {
		hit(IS_USER);
		hit(!IS_USER);
		hit(IS_USER);
		hit(!IS_USER);
	}
	
	//user should never know the dealers total
	//but this method returns that number
	private int dealerTotal(){
		int count = 0;
		int countAce = 0;
		for(int i = 0; i < dealerHand.size(); i++) {
			if(dealerHand.get(i) < 11) {
				count += dealerHand.get(i);
			} else if(dealerHand.get(i) != 14) {
				count += 10;
			} else {
				count += 11;
				countAce += 1;
			}
		}
		countAce += count - 11;
		if(count <= 21 || count == (countAce + 11)) {
			return count;
		} else {
			return countAce;
		}
	}
	
	//sets the user total at the end of the game
	//based off the current user total and alt total
	private void setEndUserTotal() {
		setTotal();
		if(altHandTotal > 21) {
			endUserTotal = handTotal;
		} else if(handTotal < altHandTotal){
			endUserTotal = altHandTotal;
		} else {
			endUserTotal = handTotal;
		}
	}
	//Dealer hits until 17 and Ace always counts as 11
	//unless it busts the hand
	private void dealerHit() {
		while(dealerTotal() < 17) {
			hit(!IS_USER);
		}
		endDealerTotal = dealerTotal();
	}
	
	//returns positive number if the user won
	//negative if lost
	//and zero if tie
	public int compareUser() {
		if(endUserTotal > 21 && endDealerTotal <= 21) {
			return -1;
		} else if(endDealerTotal > 21 && endUserTotal <= 21) {
			return 1;
		} else if(endUserTotal > 21 && endDealerTotal > 21) {
			return 0;
		}
		return (endUserTotal - endDealerTotal);
	}
	
	//prints if the dealer won or the user
	public void whoWon() {
		setEndUserTotal();
		dealerHit();
		int compared = compareUser();
		if(compared > 0) {
			System.out.println("You won!");
			money += bet;
		} else if(compared == 0) {
			System.out.println("You tied.");
		} else {
			System.out.println("You lost.");
			money -= bet;
		}
		System.out.println("You had: " + getCurrHand(currHand) + "= " + printTotal()); 
		System.out.println("the dealer had: " + getCurrHand(dealerHand) + "= " + dealerTotal());
		endRound = true;
	}
	
	//for if the user is playing a single game	
	public void userPlaying() {
		System.out.println("Your current Total is: $" + money);
		startingHand();
		setTotal();
		System.out.println("Your starting cards are: ");
		System.out.println(getCurrHand(currHand));
		printTotal();
		System.out.println("The Dealer is showing a(n) " + getDealerCardStr());
		while(handTotal < 21) {
			System.out.println("Would you like to hit? y/n");
			String answer = myScanner.nextLine();
			if(answer.equalsIgnoreCase("n")) {
				break;
			}
			hit(IS_USER);
			setTotal();
			System.out.println("Your current cards are: ");
			System.out.println(getCurrHand(currHand));
			printTotal();
		}
		setEndUserTotal();
		dealerHit();
		System.out.println("the end user total is: " + endUserTotal);
		System.out.println("the end Dealer total is: " + endDealerTotal);
	}

}
