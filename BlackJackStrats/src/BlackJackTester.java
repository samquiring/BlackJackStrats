
public class BlackJackTester {

	public static void main(String args[]) {
		Strats stratTester = new Strats(50000, 1000, 1);
		/*while(stratTester.cardGame.getplaysGame()) {
			stratTester.userPlays(stratTester.cardGame);
			stratTester.autoMartigale();
			stratTester.cardGame.setPlaysGame();
		}*/
		stratTester.autoRounds(100,3,1, 100, 100);
	}
}
