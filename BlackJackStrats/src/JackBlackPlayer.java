import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class JackBlackPlayer {
	
	private List<Integer> currHand;
	private List<Integer> dealerHand;
	private int shuffleType;
	private Scanner myScanner;
	private boolean playsGame;
	private int money;
	private int bet;
	private int handTotal;
	private int altHandTotal;
	private int endDealerTotal;
	private int endUserTotal;
	boolean isUser;
	
	
	//Initializes the game of blackJack
	//Pass in an integer of how much money the user 
	//will start with
	public JackBlackPlayer(int startingAmount) {
		money = startingAmount;
		handTotal = 0;
		isUser = true;
		altHandTotal = 0;
		bet = 100;
		endDealerTotal = 0;
		endUserTotal = 0;
		currHand = new ArrayList<Integer>();
		dealerHand = new ArrayList<Integer>();
		myScanner = new Scanner(System.in);
		shuffleType = this.shuffleType();
		this.setPlaysGame();
	}
	
	//lets the user set the betSize between each hand
	//TODO: protect the user from changing betsize mid hand
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
	}
	
	//returns the amount of money the user has left
	public int getMoney() {
		return money;
	}
	
	//prompts the user to choose what type of shuffling that it wants to use
	//Currently fully random is available which simulates a game with
	//infinite decks 
	//TODO: create auto shuffle deck and create hand shuffled deck
	private int shuffleType() {
		System.out.println("What type of shuffling do you want?");
		System.out.println("1 = autoShuffle");
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
		int min = 2;
		//Jack = 11, Queen = 12, King = 13, Ace = 14
		int max = 14;
		int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
		return (randomNum);
		
	}
	
	//This gives the player or dealer a random card based on the shuffle type
	public void hit(boolean isUser) {
		if(isUser) {
			if(shuffleType == 1) {
				currHand.add(autoShuffle());
			}
		} else {
			if(shuffleType == 1) {
				dealerHand.add(autoShuffle());
			}
		}
	}
	
	//returns the a string of current hand
	//Accepts a list of numbers from 2-14
	//with 11-14 representing face Cards
	public String getCurrHand(List<Integer> list) {
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
		
	}
	//prints the current total of your hand
	public void printTotal() {
		System.out.println("Your current total is: ");
		if(handTotal > 21) {
			System.out.println(handTotal + "(Bust)");
		}
		if(altHandTotal == 21 || handTotal == 21) {
			System.out.println("21");
		} else if(altHandTotal != handTotal) {
			System.out.println("Either: " + handTotal + " or: " + altHandTotal);
		} else {
			System.out.println(handTotal);
		}
	}
	public void startingHand() {
		hit(isUser);
		hit(!isUser);
		hit(isUser);
		hit(!isUser);
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
		if(handTotal > 21) {
			endUserTotal = altHandTotal;
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
			hit(!isUser);
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
		System.out.println("the dealer had: " + getCurrHand(dealerHand));
	}
	//for if the user is playing a single game	
	public void userPlaying() {
		System.out.println("Your current Total is: $" + money);
		startingHand();
		setTotal();
		System.out.println("Your starting cards are: ");
		System.out.println(getCurrHand(currHand));
		printTotal();
		while(handTotal < 21) {
			System.out.println("Would you like to hit? y/n");
			String answer = myScanner.nextLine();
			if(answer.equalsIgnoreCase("n")) {
				break;
			}
			hit(isUser);
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
