package number1;
import java.util.Random;

public class martingale {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int trials = 100000000;
		int successes = 0;
		int startBank = 100000;
		int lowestBet = 1000;
		int goal = 101000;
		int highestBank = startBank;
		double successTotal = 0.0;
		
		Random rand = new Random();
		a:
		for(int i = 0; i < trials; i++) {
			int bank = startBank;
			int betsMade = 0;
			while(bank < goal){
				int bet = lowestBet;
				while(true){
					if(bank - bet < 0) {
						if(bank >= 100) {
							bet = bank;
						} else {
							continue a;
						}
					}
					bank -= bet;
					int thisRoll = rand.nextInt(100)+1;
					betsMade++;
					if(thisRoll >= 61 && thisRoll <= 98) {
						bank += 2*bet;
						break;
					}
					if(thisRoll >= 99 && thisRoll <= 100) {
						bank += 3*bet;
						break;
					}
					bet *= 2;
				}
				highestBank = Math.max(bank,  highestBank);
				
			}
			successes++;
			successTotal += bank;
		}
		System.out.println("successes: " + successes);
		System.out.println("rate: " + (((double)successes)/trials*100));
		System.out.println("highest: " + highestBank);
		System.out.println("avg success bank: " + (successTotal / successes));

	}

}
