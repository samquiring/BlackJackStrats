
public class BlackJackTester {

	public static void main(String args[]) {
		Strats stratTester = new Strats(5000, 100);
		stratTester.autoMartigale(stratTester.cardGame);
	}
}
