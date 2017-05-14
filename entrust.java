package number1;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class entrust {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[] entrusterRMs = {0}; //0: ace strike, 1: mako might, 2: TGM
		boolean isMorrow = true; //index 0 is Morrow with LM2
		int goalSBcount = 4;
		
		int entrusterCount = entrusterRMs.length;
		
		Character[] myChars = new Character[entrusterCount +1];
		for(int i = 0; i < entrusterCount; i++) {
			Character a = new Character("Entruster " + i,187,entrusterRMs[i]);
			a.abilities.add(new Ability("Wrath",1.65,180.0));
			a.abilities.add(new Ability("Entrust",1.65,0.0));
			if(a.rm == 0) {
				a.abilities.get(1).sb *= 1.5;
			} else if (a.rm == 1) {
				a.currentSB += 500.0;
			} else if (a.rm == 2) {
				a.castModifier = 3.0;
			}
			if(isMorrow && i==0) {
				a.atbModifier = 2.0;
				a.powerchain = 3;
			}
			myChars[i] = a;
		}
		
		Character attacker = new Character("Attacker",170,-1);
		attacker.abilities.add(new Ability("Lifesiphon",1.65,150));
		attacker.abilities.add(new Ability("Powerchain",1.2,65,true,false));
		attacker.abilities.add(new Ability("targetSB",2.5,-500,false,true));
		myChars[myChars.length-1] = attacker;
		
		int[][] mySeq = 
			{	{1,1,1,2,1,1,1,2,1,1,2},
				{2,2,2,2500,2,2500,2,2500,2,2500}};
		
		System.out.println(eval(myChars,mySeq,10.0,4,100.0));

	}
	
	
	//rm = 0: ace striker, 1: mako might 2: TGM
	//seq[0 - len-2] = 1: wrath, 2: entrust, other: CT*100 (RW)
	//seq[len-1] = 1: ability 1, 2: ability 2, other: SB CT*100
	//morrow: char at index 0 starts with IC3+haste
	public static double eval(Character[] myChars, int[][] mySeq, double bossInterval, int goal, double maxTime) {
		double inputDelay = .2;
		int targetSBcount = 0;
		
		for(int i = 0; i < myChars.length; i++) { //characters start with bar half full
			myChars[i].timeToNextState = (4.5 - (myChars[i].speed / 150.0))/myChars[i].atbModifier /2;
		}
		double timeElapsed = 0.0;
		double hasteTime = 5.0;
		boolean hasted = false;
		double TGMexpiry = 25.0;
		boolean TGMexpired = false;
		double bossTime = bossInterval;
		while(true){//CHANGETHIS
			Character nextEventChar = myChars[0];
			int[] nextCharSeq = mySeq[0];
			for(int i = 0; i < myChars.length; i++) {
				if(myChars[i].timeToNextState < nextEventChar.timeToNextState) {
					nextEventChar = myChars[i];
					nextCharSeq = mySeq[i];
				}
				
			}
			if(timeElapsed + nextEventChar.timeToNextState >= 100.0)
				return 100.0;
			if(!hasted && timeElapsed +nextEventChar.timeToNextState > hasteTime) {
				double timePassing = hasteTime - timeElapsed;
				timeElapsed = hasteTime;
				for(int i = 0; i < myChars.length; i++) {
					myChars[i].timeToNextState -= timePassing;
					myChars[i].timeToNextState /= 2.0/myChars[i].atbModifier;
					myChars[i].atbModifier = 2.0;
				}
				hasted = true;
				continue;
			}
			if(!TGMexpired && timeElapsed + nextEventChar.timeToNextState > TGMexpiry) {
				double timePassing = TGMexpiry - timeElapsed;
				timeElapsed = TGMexpiry;
				for(int i = 0; i < myChars.length; i++) {
					myChars[i].timeToNextState -= timePassing;
					myChars[i].castModifier = 1.0;
				}
				TGMexpired = true;
				continue;
			}
			if(timeElapsed + nextEventChar.timeToNextState > bossTime) {
				double timePassing = bossTime - timeElapsed;
				timeElapsed = bossTime;
				bossTime += bossInterval;
				for(int i = 0; i < myChars.length; i++) {
					myChars[i].timeToNextState -= timePassing;
					myChars[i].currentSB = Math.min(myChars[i].currentSB +50.0, 1500.0);
				}
				continue;
			}
			
			double timePassing = nextEventChar.timeToNextState;
			timeElapsed += timePassing;
			if(timeElapsed >= maxTime)
				return maxTime+1;
			for(int i = 0; i < myChars.length; i++) {
				myChars[i].timeToNextState -= timePassing;
			}
			
			if(nextEventChar.state == 0) {
				Ability myAbil = parseAbility(nextEventChar, nextCharSeq[nextEventChar.nextActionIndex]);
				if(nextEventChar.currentSB + myAbil.sb < 0.0) {
					double subsequentTime = 100.0;
					for( int i = 0; i < myChars.length; i++) {
						if(myChars[i] == nextEventChar)
							continue;
						if(myChars[i].timeToNextState < subsequentTime)
							subsequentTime = myChars[i].timeToNextState;
					}
					subsequentTime = Math.min(subsequentTime, bossTime-timeElapsed);
					nextEventChar.timeToNextState = subsequentTime + .035;
					continue;
				}
				nextEventChar.state = 1;
				if(nextEventChar.powerchain >0) {
					nextEventChar.powerchain--;
					nextEventChar.timeToNextState = inputDelay;
				} else {
					nextEventChar.timeToNextState = myAbil.ct/nextEventChar.castModifier + inputDelay;
				}
				nextEventChar.currentlyCasting = myAbil;
			} else if(nextEventChar.state == 1) {
				nextEventChar.state = 0;
				Ability myAbil = nextEventChar.currentlyCasting;
				if(myAbil.isTarget)
					targetSBcount++;
				if(targetSBcount>=goal)
					return timeElapsed;
				
				if(myAbil.grantPC)
					nextEventChar.powerchain = 1;
				
				if(myAbil.name.equals("Entrust")) {
					Character targetChar = myChars[myChars.length-1];
					double a = nextEventChar.currentSB;
					double b = targetChar.currentSB;
					targetChar.currentSB = Math.min(a+b, 1500.0);
					nextEventChar.currentSB = a+b - targetChar.currentSB;
				} else {
					nextEventChar.currentSB += myAbil.sb;
				}
				nextEventChar.currentlyCasting = null;
				nextEventChar.nextActionIndex++;
				nextEventChar.timeToNextState = (4.5 - nextEventChar.speed / 150.0)/nextEventChar.atbModifier;
				if(nextEventChar.nextActionIndex >= nextCharSeq.length)
					nextEventChar.timeToNextState = 100.0;
				
				System.out.println(nextEventChar.name + " " + myAbil.name + " " + nextEventChar.currentSB + " " + timeElapsed);
			}
			
		}
	}
	
	private static Ability parseAbility(Character a, int arg) {
		if(arg == 1)
			return a.abilities.get(1);
		if(arg == 2)
			return a.abilities.get(2);
		if(a.abilities.get(2).name.equals("Entrust"))
			return new Ability("RW",arg*.01, 0.0);
		return new Ability("damageSB",arg*.01, -500.0, false, true);
	}
	
	private static class Character {
		String name;
		double speed;
		double atbModifier = 1.0;// 2.0=haste;
		double castModifier = 1.0;
		ArrayList<Ability> abilities;
		double currentSB = 0;
		double sbModifier = 1.0;
		int state = 0; //0 = ATB filling, 1 = casting
		double timeToNextState = 0;
		Ability currentlyCasting;
		int powerchain = 0;
		int nextActionIndex = 0;
		int rm;


		public Character(String name, double spd, int rm) {
			this.name = name;
			speed = spd;
			atbModifier = 1.0;
			abilities = new ArrayList<Ability>();
			this.rm = rm;
			abilities.add(new Ability("Attack", 1.5,50.0));
		}

	}

	private static class Ability {
		String name;
		double ct;
		double sb;
		boolean grantPC;
		boolean isTarget;

		public Ability(String name, double ct,double sb, boolean grantPC, boolean target) {
			this.name = name;
			this.ct = ct;
			this.sb = sb;
			this.grantPC = grantPC;
			isTarget = target;
		}
		
		public Ability(String name, double ct,double sb) {
			this.name = name;
			this.ct = ct;
			this.sb = sb;
			this.grantPC = false;
			isTarget = false;
		}
	}

}
