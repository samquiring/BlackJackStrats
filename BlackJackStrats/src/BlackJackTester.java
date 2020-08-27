
public class BlackJackTester {

	public static void main(String args[]) {
		Strats stratTester = new Strats(50000, 100);
		/*while(stratTester.cardGame.getplaysGame()) {
			stratTester.userPlays(stratTester.cardGame);
			stratTester.autoMartigale();
			stratTester.cardGame.setPlaysGame();
		}*/
		stratTester.autoRounds(5,2,1);
	}
}
