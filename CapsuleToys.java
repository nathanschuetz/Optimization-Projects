package number1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class CapsuleToys {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String ticketType = "g";
		double refreshCost = 500.0;
		double drawCost = 30.0;
		double lockCost = 100.0;
		boolean confirm = false; //iterates full database again to confirm stable solution
		
		ticketType = ticketType.toLowerCase();
		int[] combo = new int[1];
		double avgCost = 0;
		switch(ticketType){
		case "bronze":
		case "b":
			int[] a = {1,2,1,0};
			combo = a;
			avgCost = 1568.8;
			break;
		case "silver":
		case "s":
			int[] b = {1,1,2,0};
			combo = b;
			avgCost = 2263.9;
			break;
		case "gold":
		case "g":
			int[] c = {0,0,3,1};
			combo = c;
			avgCost = 5874.97;
			break;
		}
//		int[] combo = {0,0,3,1}; //1210 bronze 1120 silver 0031 gold
		avgCost = 5022.766053275296; //1569.2 b 2263.9 s 5874.97 g
		
		long timer = System.currentTimeMillis();
		//output: first 4: slots to lock, avg diamonds spent, avg tickets found, reset?, real diamond cost
		HashMap<Key,HashMap<Key,double[]>> myMap = new HashMap<Key, HashMap<Key, double[]>>();
		int[] myPools = {34,10,5,1}; //34 10 5 1
		double[] defaultResult = {0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0};
		for(int i = 0; i <= myPools[0]; i++) {
			for(int j = 0; j <= myPools[1]; j++) {
				for(int k = 0; k <= myPools[2]; k++) {
					for(int l = 0; l <= myPools[3]; l++) {
						//System.out.println(i + " " + j + " " +k+" "+l);
						int remaining = i + j + k + l;
						int[] thisPools = new int[4];
						thisPools[0] = i;
						thisPools[1] = j;
						thisPools[2] = k;
						thisPools[3] = l;
						HashMap<Key,double[]> thisMap = new HashMap<Key,double[]>();
						for(int a = 0; a < 5; a++) {
							for(int b = a; b < 5; b++) {
								for(int c = b; c < 5; c++) {
									for(int d = c; d < 5; d++) {
										//System.out.println(a + " " + b + " " + c + " " + d);
										int[] thisSlots = new int[4];
										thisSlots[0] = a;
										thisSlots[1] = b;
										thisSlots[2] = c;
										thisSlots[3] = d;
										int[] slotTotals = new int[4];
										if(a != 0)
											slotTotals[a-1]++;
										if(b != 0)
											slotTotals[b-1]++;
										if(c != 0)
											slotTotals[c-1]++;
										if(d != 0)
											slotTotals[d-1]++;
										if(slotTotals[0]+i > myPools[0] || slotTotals[1]+j > myPools[1] ||
												slotTotals[2]+k > myPools[2] || slotTotals[3]+l > myPools[3])
											continue;
										//System.out.println(a + " " + b + " " + c + " " + d + " | " + j);
										if(slotTotals[0]+i < combo[0] || slotTotals[1]+j < combo[1] ||
												slotTotals[2]+k < combo[2] || slotTotals[3]+l < combo[3]) {

//											double[] temp2 = {0.0,0.0,0.0,0.0,0.0,0.0,1.0};
//											thisMap.put(new Key(thisSlots),temp2);
											
											
											continue;
										}
										if(slotTotals[0] == combo[0] && slotTotals[1] == combo[1] &&
												slotTotals[2] == combo[2] && slotTotals[3] == combo[3])
											continue;
										double bestTickets = 0.0;
										double bestDiamonds = drawCost;
										double bestLockCost = 0.0;
										int[] bestLocks = new int[4];
										for(int x = 0; x < 15; x++) {
											if(a == 0 && x >=1)
												break;
											int[]locks = new int[4];
											locks[0] = x/8;
											locks[1] = (x/4)%2;
											locks[2] = (x/2)%2;
											locks[3] = x%2;
											int numLocks = locks[0]+locks[1]+locks[2]+locks[3];
											double expectedTickets = 0.0;
											double expectedDiamonds = drawCost + lockCost*numLocks;
											double expectedLockCost = lockCost * numLocks;
											for(int y = 0; y < 4; y++) {
												if(thisPools[y]==0)
													continue;
												double thisProbability = thisPools[y]*1.0/remaining;
												int[] newPools = new int[4];
												newPools[0] = i;
												newPools[1] = j;
												newPools[2] = k;
												newPools[3] = l;
												newPools[y] = newPools[y]-1;
												HashMap<Key,double[]> currentMap = myMap.get(new Key(newPools));
												for(int z = 0; z < 4; z++) {
													if(a == 0 && z >=1)
														break;
													if(locks[z]==1)
														continue;
													int numPossible = 4-numLocks;
													if(a == 0)
														numPossible = 1;
													int[] newSlots = new int[4];
													newSlots[0] = a;
													newSlots[1] = b;
													newSlots[2] = c;
													newSlots[3] = d;
													newSlots[z] = y+1;
													newSlots = sortArray(newSlots);
													int[] newSlotTotals = new int[4];
													for(int q = 0; q < 4; q++)
														if(newSlots[q] != 0)
															newSlotTotals[newSlots[q]-1]++;
													if(newSlotTotals[0] == combo[0] && newSlotTotals[1] == combo[1] &&
															newSlotTotals[2] == combo[2] && newSlotTotals[3] == combo[3]) {
														expectedTickets += 1.0 * thisProbability / numPossible;
														newSlots = new int[4];
													}
													if(newSlotTotals[0]+newPools[0] < combo[0] || newSlotTotals[1]+newPools[1] < combo[1] ||
															newSlotTotals[2]+newPools[2] < combo[2] || newSlotTotals[3]+newPools[3] < combo[3]) {
														expectedDiamonds += avgCost * thisProbability / numPossible;
														//expectedTickets +=1;
													}
													
//													System.out.println(numLocks);
//													for(int q = 0; q < 4; q++)
//														System.out.println(newPools[q]);
//													for(int q = 0; q < 4; q++)
//														System.out.println(newSlots[q]);
//													System.out.println(""+a+b+c+d);
//													System.out.println(currentMap.toString());
													double[] currentResults = currentMap.get(new Key(newSlots));
													if(currentResults == null)
														currentResults = defaultResult;
													if(currentResults[6] == 1.0)
														continue;
													expectedDiamonds += currentResults[4] * thisProbability / numPossible;
													expectedTickets += currentResults[5] * thisProbability / numPossible;
													expectedLockCost += currentResults[7] * thisProbability / numPossible;
//													}
													
													
												}
											}
//											for(int q = 0; q < 4; q++)
//												System.out.print(locks[q]+" ");
//											System.out.println(expectedTickets + " " + expectedDiamonds + " | " + (expectedTickets/expectedDiamonds));
											if(expectedTickets/expectedDiamonds > bestTickets/bestDiamonds){
												bestTickets = expectedTickets;
												bestDiamonds = expectedDiamonds;
												bestLockCost = expectedLockCost;
												for(int y = 0; y < 4; y++)
													bestLocks[y] = locks[y];
											}
										}
										double[] thisResults = new double[8];
										for(int x = 0; x < 4; x++)
											thisResults[x] = (double)bestLocks[x];
										thisResults[4] = bestDiamonds;
										thisResults[5] = bestTickets;
										if(bestDiamonds/bestTickets > avgCost + refreshCost)
											thisResults[6] = 1.0;
										thisResults[7] = bestLockCost;
										thisMap.put(new Key(thisSlots),thisResults);
										
//										for(int x = 0; x < 4; x++)
//											System.out.print(thisPools[x] + " ");
//										System.out.print("| ");
//										for(int x = 0; x < 4; x++)
//											System.out.print(thisSlots[x] + " ");
//										System.out.print("| ");
//										for(int x = 0; x < thisResults.length; x++)
//											System.out.print(thisResults[x] + " ");
//										System.out.println();
									}
								}
							}
						}
						myMap.put(new Key(thisPools),thisMap);
					}
				}
			}
		}
		double[] overallData = myMap.get(new Key(myPools)).get(new Key(new int[4]));
		//System.out.println(a[4]+"/"+a[5]);
		System.out.println(overallData[4]/overallData[5]);
		//printMap(myMap,myPools);
		
		double oldEfficiency = overallData[4]/overallData[5];
		double newEfficiency = oldEfficiency;
		int maxHash = myHash(myPools,new int[4]);
		boolean firstLoop = true;
		//System.out.println(maxHash); 1478080
		do {
			oldEfficiency = newEfficiency;
			int lockToSwap = 0;
			int highestH = 0;
			int timesReached = 0;
			myLoop:
			for(int h = 0; h < maxHash; ) {
				if(firstLoop) {
					firstLoop = false;
					h = 1300000;
					if(combo[3]==1)
						h = 500000;
				}
				boolean lockSwapped = false;
				HashMap<Key,HashMap<Key,double[]>> myNewMap = copyMap(myMap,myPools);
//				LinkedList<int[][]> myStates = new LinkedList<int[][]>();
//				myOtherLoop:
				for(int i = 0; i <= myPools[0]; i++) {
					for(int j = 0; j <= myPools[1]; j++) {
						for(int k = 0; k <= myPools[2]; k++) {
							for(int l = 0; l <= myPools[3]; l++) {
								//System.out.println(i + " " + j + " " +k+" "+l);
								int remaining = i + j + k + l;
								int[] thisPools = new int[4];
								thisPools[0] = i;
								thisPools[1] = j;
								thisPools[2] = k;
								thisPools[3] = l;
								
								HashMap<Key,double[]> thisMap = myNewMap.get(new Key(thisPools));
								if(thisMap == null)
									continue;
								if(lockSwapped == false) {
									int[] maxSlots = {3,3,3,4};
									if(h > myHash(thisPools,maxSlots))
										continue;
								}
								for(int a = 0; a < 5; a++) {
									for(int b = a; b < 5; b++) {
										for(int c = b; c < 5; c++) {
											for(int d = c; d < 5; d++) {
												//System.out.println(a + " " + b + " " + c + " " + d);
												int[] thisSlots = new int[4];
												thisSlots[0] = a;
												thisSlots[1] = b;
												thisSlots[2] = c;
												thisSlots[3] = d;
												if(lockSwapped == false) {
													if(h > myHash(thisPools,thisSlots))
														continue;
													if(a == 0)
														continue;
												}
												if(thisMap.get(new Key(thisSlots)) == null)
													continue;
												int thisX = -1;
												if(lockSwapped == false) {
													int[] lockValues = new int[4];
													double[] thisData = thisMap.get(new Key(thisSlots));
													for(int f = 0; f < 4; f++)
														lockValues[f] = ((int)(thisData[f]));
													if(lockValues[lockToSwap] == 1) {
														lockValues[lockToSwap] = 0;
													} else {
														lockValues[lockToSwap] = 1;
													}
													h = myHash(thisPools,thisSlots);
													lockSwapped = true;
													lockToSwap++;
													if(lockToSwap == 4) {
														lockToSwap = 0;
														h++;
													}
													thisX = 8*lockValues[0]+4*lockValues[1]+2*lockValues[2]+lockValues[3];
													if(thisX == 15)
														continue myLoop;
//													for(int x = 0; x < 4; x++)
//														System.out.print(thisPools[x] + " ");
//													System.out.print("| ");
//													for(int x = 0; x < 4; x++)
//														System.out.print(thisSlots[x] + " ");
//													System.out.println();
												}
												int[] slotTotals = new int[4];
												if(a != 0)
													slotTotals[a-1]++;
												if(b != 0)
													slotTotals[b-1]++;
												if(c != 0)
													slotTotals[c-1]++;
												if(d != 0)
													slotTotals[d-1]++;
												if(slotTotals[0]+i > myPools[0] || slotTotals[1]+j > myPools[1] ||
														slotTotals[2]+k > myPools[2] || slotTotals[3]+l > myPools[3])
													continue;
												//System.out.println(a + " " + b + " " + c + " " + d + " | " + j);
												//System.out.println(thisX);
												if(slotTotals[0]+i < combo[0] || slotTotals[1]+j < combo[1] ||
														slotTotals[2]+k < combo[2] || slotTotals[3]+l < combo[3]) {
													double[] temp2 = {0.0,0.0,0.0,0.0,0.0,0.0,1.0};
													thisMap.put(new Key(thisSlots),temp2);
													if(thisX > -1)
														continue myLoop;
													
													continue;
												}
												if(slotTotals[0] == combo[0] && slotTotals[1] == combo[1] &&
														slotTotals[2] == combo[2] && slotTotals[3] == combo[3])
													continue;
												double bestTickets = 0.0;
												double bestDiamonds = drawCost;
												double bestLockCost = 0.0;
												int[] bestLocks = new int[4];
												for(int x = 0; x < 15; x++) {
													if(a == 0 && x >=1)
														break;
													if(thisX > -1 && x != thisX)
														continue;
													int[]locks = new int[4];
													locks[0] = x/8;
													locks[1] = (x/4)%2;
													locks[2] = (x/2)%2;
													locks[3] = x%2;
													int numLocks = locks[0]+locks[1]+locks[2]+locks[3];
													double expectedTickets = 0.0;
													double expectedDiamonds = drawCost + lockCost*numLocks;
													double expectedLockCost = lockCost * numLocks;
													for(int y = 0; y < 4; y++) {
														if(thisPools[y]==0)
															continue;
														double thisProbability = thisPools[y]*1.0/remaining;
														int[] newPools = new int[4];
														newPools[0] = i;
														newPools[1] = j;
														newPools[2] = k;
														newPools[3] = l;
														newPools[y] = newPools[y]-1;
														HashMap<Key,double[]> currentMap = myNewMap.get(new Key(newPools));
														for(int z = 0; z < 4; z++) {
															if(a == 0 && z >=1)
																break;
															if(locks[z]==1)
																continue;
															int numPossible = 4-numLocks;
															if(a == 0)
																numPossible = 1;
															int[] newSlots = new int[4];
															newSlots[0] = a;
															newSlots[1] = b;
															newSlots[2] = c;
															newSlots[3] = d;
															newSlots[z] = y+1;
															newSlots = sortArray(newSlots);
															int[] newSlotTotals = new int[4];
															for(int q = 0; q < 4; q++)
																if(newSlots[q] != 0)
																	newSlotTotals[newSlots[q]-1]++;
															if(newSlotTotals[0] == combo[0] && newSlotTotals[1] == combo[1] &&
																	newSlotTotals[2] == combo[2] && newSlotTotals[3] == combo[3]) {
																expectedTickets += 1.0 * thisProbability / numPossible;
																newSlots = new int[4];
															}else if(newSlotTotals[0]+newPools[0] < combo[0] || newSlotTotals[1]+newPools[1] < combo[1] ||
																	newSlotTotals[2]+newPools[2] < combo[2] || newSlotTotals[3]+newPools[3] < combo[3]) {
																expectedDiamonds += refreshCost * thisProbability / numPossible;
																//expectedTickets +=1;
															}
															
//															System.out.println(numLocks);
//															for(int q = 0; q < 4; q++)
//																System.out.println(newPools[q]);
//															for(int q = 0; q < 4; q++)
//																System.out.println(newSlots[q]);
//															System.out.println(""+a+b+c+d);
//															System.out.println(currentMap.toString());
															double[] currentResults = currentMap.get(new Key(newSlots));
															if(currentResults == null)
																currentResults = defaultResult;
															if(currentResults[6] == 1.0)
																continue;
															expectedDiamonds += currentResults[4] * thisProbability / numPossible;
															expectedTickets += currentResults[5] * thisProbability / numPossible;
															expectedLockCost += currentResults[7] * thisProbability / numPossible;
//															}
															
															
														}
													}
//													for(int q = 0; q < 4; q++)
//														System.out.print(locks[q]+" ");
//													System.out.println(expectedTickets + " " + expectedDiamonds + " | " + (expectedTickets/expectedDiamonds));
													if(expectedTickets/expectedDiamonds > bestTickets/bestDiamonds){
														bestTickets = expectedTickets;
														bestDiamonds = expectedDiamonds;
														bestLockCost = expectedLockCost;
														for(int y = 0; y < 4; y++)
															bestLocks[y] = locks[y];
													}
												}
												double[] oldData = thisMap.get(new Key(thisSlots));
												if(oldData == null)
													System.out.println("check this");
												if(bestDiamonds > oldData[4] && bestTickets <= oldData[5])
													continue myLoop;
												double[] thisResults = new double[8];
												for(int x = 0; x < 4; x++)
													thisResults[x] = (double)bestLocks[x];
												thisResults[4] = bestDiamonds;
												thisResults[5] = bestTickets;
												if(bestDiamonds/bestTickets > newEfficiency + refreshCost)
													thisResults[6] = 1.0;
												thisResults[7] = bestLockCost;
												thisMap.put(new Key(thisSlots),thisResults);
												
												if(thisX > -1) {
													if(System.currentTimeMillis() - timer > 10000) {
														timer = System.currentTimeMillis();
														for(int x = 0; x < 4; x++)
															System.out.print(thisPools[x] + " ");
														System.out.print("| ");
														for(int x = 0; x < 4; x++)
															System.out.print(thisSlots[x] + " ");
														System.out.println();
													}
													/*myNewMap.put(new Key(thisPools),thisMap);
													addStates(myStates,thisPools,thisSlots,myPools,combo);
													break myOtherLoop;*/
												}
//												for(int x = 0; x < 4; x++)
//													System.out.print(thisPools[x] + " ");
//												System.out.print("| ");
//												for(int x = 0; x < 4; x++)
//													System.out.print(thisSlots[x] + " ");
//												System.out.print("| ");
//												for(int x = 0; x < thisResults.length; x++)
//													System.out.print(thisResults[x] + " ");
//												System.out.println();
											}
										}
									}
								}
								myNewMap.put(new Key(thisPools),thisMap);
							}
						}
					}
				}
				
				/*HashMap<Integer,Boolean> checked = new HashMap<Integer,Boolean>();
				while(myStates.size()>0) {
					int[][] thisState = myStates.removeFirst();
					Integer stateHash = new Integer(myHash(thisState[0],thisState[1]));
					if(checked.get(stateHash) != null)
						continue;
					checked.put(stateHash, new Boolean(true));
					int[] thisPools = thisState[0];
					int[] thisSlots = thisState[1];
					HashMap<Key,double[]> thisMap = myNewMap.get(new Key(thisPools));
					double[] oldData = thisMap.get(new Key(thisSlots));
					if(oldData == null)
						continue;
					
					int i = thisPools[0];
					int j = thisPools[1];
					int k = thisPools[2];
					int l = thisPools[3];
					int remaining = i + j + k + l;
					int a = thisSlots[0];
					int b = thisSlots[1];
					int c = thisSlots[2];
					int d = thisSlots[3];
					
//					int sum = 0;
//					System.out.print(myStates.size() + " |");
//					for(int x = 0;x < 4; x++) {
//						System.out.print(" " + thisPools[x]);
//						sum += thisPools[x];}
//					System.out.print(" |");
//					for(int x = 0; x < 4; x++) {
//						System.out.print(" " + thisSlots[x]);
//						if(thisSlots[x]!=0)
//							sum++;}
//					System.out.println(" | " + sum);
					
//					for(int x = 0; x < myStates.size(); x++)
//						System.out.print(stateTotal(myStates.get(x)) + " ");
//					System.out.println();
					
					int[] slotTotals = new int[4];
					if(a != 0)
						slotTotals[a-1]++;
					if(b != 0)
						slotTotals[b-1]++;
					if(c != 0)
						slotTotals[c-1]++;
					if(d != 0)
						slotTotals[d-1]++;
					if(slotTotals[0]+i > myPools[0] || slotTotals[1]+j > myPools[1] ||
							slotTotals[2]+k > myPools[2] || slotTotals[3]+l > myPools[3])
						continue;
					//System.out.println(a + " " + b + " " + c + " " + d + " | " + j);
					if(slotTotals[0]+i < combo[0] || slotTotals[1]+j < combo[1] ||
							slotTotals[2]+k < combo[2] || slotTotals[3]+l < combo[3]) {
						double[] temp2 = {0.0,0.0,0.0,0.0,0.0,0.0,1.0};
						thisMap.put(new Key(thisSlots),temp2);
						
						
						continue;
					}
					if(slotTotals[0] == combo[0] && slotTotals[1] == combo[1] &&
							slotTotals[2] == combo[2] && slotTotals[3] == combo[3])
						continue;
					double bestTickets = 0.0;
					double bestDiamonds = 50.0;
					int[] bestLocks = new int[4];
					for(int x = 0; x < 15; x++) {
						if(a == 0 && x >=1)
							break;
						
						int[]locks = new int[4];
						locks[0] = x/8;
						locks[1] = (x/4)%2;
						locks[2] = (x/2)%2;
						locks[3] = x%2;
						int numLocks = locks[0]+locks[1]+locks[2]+locks[3];
						double expectedTickets = 0.0;
						double expectedDiamonds = 50.0 + 100*numLocks;
						for(int y = 0; y < 4; y++) {
							if(thisPools[y]==0)
								continue;
							double thisProbability = thisPools[y]*1.0/remaining;
							int[] newPools = new int[4];
							newPools[0] = i;
							newPools[1] = j;
							newPools[2] = k;
							newPools[3] = l;
							newPools[y] = newPools[y]-1;
							HashMap<Key,double[]> currentMap = myNewMap.get(new Key(newPools));
							for(int z = 0; z < 4; z++) {
								if(a == 0 && z >=1)
									break;
								if(locks[z]==1)
									continue;
								int numPossible = 4-numLocks;
								if(a == 0)
									numPossible = 1;
								int[] newSlots = new int[4];
								newSlots[0] = a;
								newSlots[1] = b;
								newSlots[2] = c;
								newSlots[3] = d;
								newSlots[z] = y+1;
								newSlots = sortArray(newSlots);
								int[] newSlotTotals = new int[4];
								for(int q = 0; q < 4; q++)
									if(newSlots[q] != 0)
										newSlotTotals[newSlots[q]-1]++;
								if(newSlotTotals[0] == combo[0] && newSlotTotals[1] == combo[1] &&
										newSlotTotals[2] == combo[2] && newSlotTotals[3] == combo[3]) {
									expectedTickets += 1.0 * thisProbability / numPossible;
									newSlots = new int[4];
								}
								if(newSlotTotals[0]+newPools[0] < combo[0] || newSlotTotals[1]+newPools[1] < combo[1] ||
										newSlotTotals[2]+newPools[2] < combo[2] || newSlotTotals[3]+newPools[3] < combo[3]) {
									//expectedDiamonds += avgCost * thisProbability / numPossible;
									//expectedTickets +=1;
								}
								
//								System.out.println(numLocks);
//								for(int q = 0; q < 4; q++)
//									System.out.println(newPools[q]);
//								for(int q = 0; q < 4; q++)
//									System.out.println(newSlots[q]);
//								System.out.println(""+a+b+c+d);
//								System.out.println(currentMap.toString());
								double[] currentResults = currentMap.get(new Key(newSlots));
//								if(currentResults == null && expectedTickets >0){
//									System.out.println("first");
//								} else {
								if(currentResults[6] == 1.0)
									continue;
								expectedDiamonds += currentResults[4] * thisProbability / numPossible;
								expectedTickets += currentResults[5] * thisProbability / numPossible;
//								}
								
								
							}
						}
//						for(int q = 0; q < 4; q++)
//							System.out.print(locks[q]+" ");
//						System.out.println(expectedTickets + " " + expectedDiamonds + " | " + (expectedTickets/expectedDiamonds));
						if(expectedTickets/expectedDiamonds > bestTickets/bestDiamonds){
							bestTickets = expectedTickets;
							bestDiamonds = expectedDiamonds;
							for(int y = 0; y < 4; y++)
								bestLocks[y] = locks[y];
						}
					}
//					if(oldData[5] == 0 && bestTickets == 0)
//						continue;
					double[] thisResults = new double[7];
					for(int x = 0; x < 4; x++)
						thisResults[x] = (double)bestLocks[x];
					thisResults[4] = bestDiamonds;
					thisResults[5] = bestTickets;
					if(bestDiamonds/bestTickets > avgCost + refreshCost)
						thisResults[6] = 1.0;
					
					boolean allSame = true;
					for(int x = 0; x < 7; x++) {
						//System.out.print(thisResults[x] + " " + oldData[x] + " | ");
						if(thisResults[x] != oldData[x])
							allSame = false;
					}
					//System.out.println();
					if(allSame)
						continue;
					thisMap.put(new Key(thisSlots),thisResults);
					myNewMap.put(new Key(thisPools),thisMap);
					addStates(myStates, thisPools, thisSlots, myPools, combo);
				}
				*/
				if(h > highestH) {
					highestH = h;
					timesReached = 0;
				} else if (h == highestH) {
					timesReached++;
				}
				if(timesReached >5)
					break myLoop;
				
				double[] newData = myNewMap.get(new Key(myPools)).get(new Key(new int[4]));
//				for(int e = 0; e < newData.length; e++)
//					System.out.println(newData[e]);
				double thisEfficiency = newData[4]/newData[5];
//				if((thisEfficiency - newEfficiency)< -.001)
//					System.out.println(newEfficiency - thisEfficiency);
				if(thisEfficiency < newEfficiency){
					newEfficiency = thisEfficiency;
					System.out.println(newEfficiency);
					myMap = myNewMap;
				}
			}
			System.out.println("loop done");
			
		} while(confirm && newEfficiency < oldEfficiency);//while(newEfficiency < oldEfficiency);
		printMap(myMap, myPools);

	}
	
	public static void addStates(LinkedList<int[][]> a, int[] pools, int[] slots, int[] myPools, int[] combo) {
		
		int[] theseSlots = new int[4];
		for(int i = 0; i < 4; i++)
			theseSlots[i] = slots[i];
			
		if(slots[0] == 0) {
			if(slots[3] == 0) {
				theseSlots = new int[4];
				for(int i = 0; i < 4; i++) {
					int count = combo[i];
					while(count>0) {
						int j = 0;
						while(theseSlots[j]!=0){
							//System.out.println(j);
							j++;
						}
						theseSlots[j]=i+1;
						count--;
					}
				}
//				for(int i = 0; i < 4; i++)
//					System.out.println(theseSlots[i]);
			} else {
				for(int i = 1; i < 4; i++) {
					if(slots[i]==0)
						continue;
					if(slots[i] == slots[i-1])
						continue;
					int[] newSlots = new int[4];
					for(int j = 0; j < 4; j++)
						newSlots[j] = slots[j];
					newSlots[i] = 0;
					sortArray(newSlots);
					int[] newPools = new int[4];
					for(int j = 0; j < 4; j++)
						newPools[j] = pools[j];
					newPools[slots[i]-1]++;
					int[][] newState = {newPools,newSlots};
					boolean duplicate = false;
					dupCheck:
					for(int x = 0; x < a.size(); x++) {
						int[][] temp = a.get(x);
						for(int y = 0; y < temp.length; y++) {
							for(int z = 0; z < temp[0].length; z++) {
								if(temp[y][z] != newState[y][z]) {
									continue dupCheck;
								}
							}
						}
						duplicate = true;
						break;
					}
					if(!duplicate)
						insert(a,newState);
				}
				
				return;
			}
		}
		for(int i = 0; i < 4; i++) {
			if(i > 0 && theseSlots[i] == theseSlots[i-1])
				continue;
			for(int j = 0; j < 5; j++) {
				int[] newSlots = new int[4];
				for(int k = 0; k < 4; k++)
					newSlots[k] = theseSlots[k];
				newSlots[i] = j;
				
				int[] newPools = new int[4];
				for(int k = 0; k < 4; k++)
					newPools[k] = pools[k];
				int poolToCheck = theseSlots[i]-1;
				//System.out.println(theseSlots[0]+ " " + theseSlots[1]+" "+theseSlots[2]+" " +theseSlots[3]+" "+poolToCheck);
				newPools[poolToCheck]++;
				int quantity = newPools[poolToCheck];
				for(int k = 0; k < 4; k++)
					if(newPools[k] - 1 == poolToCheck)
						quantity++;
				if(quantity > myPools[poolToCheck])
					continue;
				sortArray(newSlots);
				int[][] newState = {newPools,newSlots};
				boolean duplicate = false;
				dupCheck:
				for(int x = 0; x < a.size(); x++) {
					int[][] temp = a.get(x);
					for(int y = 0; y < temp.length; y++) {
						for(int z = 0; z < temp[0].length; z++) {
							if(temp[y][z] != newState[y][z]) {
								continue dupCheck;
							}
						}
					}
					duplicate = true;
					break;
				}
				if(!duplicate) {
					insert(a,newState);
				}
			}
		}
	}
	
	public static void insert(LinkedList<int[][]> a, int[][] state) {
		if(a.size()==0) {
			a.add(state);
			return;
		}
		
		int myTotal = stateTotal(state);
		if(myTotal <= stateTotal(a.getFirst())){
			a.addFirst(state);
		} else if (myTotal >= stateTotal(a.getLast())) {
			a.add(state);
		} else {
			for(int i = 1; i < a.size(); i++) {
				if(myTotal <= stateTotal(a.get(i))) {
					a.add(i,state);
					
					//System.out.println(myTotal + " " + a.indexOf(state));
					return;
				}
			}
			System.out.println("state not added");
		}
	}
	
	public static int stateTotal(int[][] state) {
		int sum = 0;
		for(int i = 0; i < 4; i++)
			sum += state[0][i];
		for(int i = 0; i < 4; i++)
			if(state[1][i] !=0)
				sum++;
		return sum;
	}
	
	public static int myHash(int[] pools, int[] slots) {
		int a = pools[0];
		a = 11*a + pools[1];
		a = 6*a + pools[2];
		a = 2*a + pools[3];
		a = 4*a + slots[0];
		a = 4*a + slots[1];
		a = 4*a + slots[2];
		a = 5*a + slots[3];
		return a;
		
	}
	
	public static void printMap(HashMap<Key,HashMap<Key,double[]>> myNewMap, int[] myPools) {
		for(int i = 0; i <= myPools[0]; i++) {
			for(int j = 0; j <= myPools[1]; j++) {
				for(int k = 0; k <= myPools[2]; k++) {
					for(int l = 0; l <= myPools[3]; l++) {
						int[] thisPools = new int[4];
						thisPools[0] = i;
						thisPools[1] = j;
						thisPools[2] = k;
						thisPools[3] = l;
						HashMap<Key,double[]> thisMap = myNewMap.get(new Key(thisPools));
						if(thisMap == null)
							continue;
						for(int a = 0; a < 5; a++) {
							for(int b = a; b < 5; b++) {
								for(int c = b; c < 5; c++) {
									for(int d = c; d < 5; d++) {
										int[] thisSlots = new int[4];
										thisSlots[0] = a;
										thisSlots[1] = b;
										thisSlots[2] = c;
										thisSlots[3] = d;
										double[] thisResults = thisMap.get(new Key(thisSlots));
										if(thisResults == null)
											continue;
										for(int x = 0; x < 4; x++)
											System.out.print(thisPools[x] + " ");
										System.out.print("| ");
										for(int x = 0; x < 4; x++)
											System.out.print(thisSlots[x] + " ");
										System.out.print("| ");
										if(thisResults[6] == 1.0) {
											System.out.println("RESET");
											continue;
										}
										for(int x = 0; x < 4; x++)
											System.out.print(((int)thisResults[x]) + " ");
										System.out.println("| " +thisResults[4] + " " + thisResults[7]+" " + thisResults[5]);
										//System.out.println();
									}
								}
							}
						}
					}
				}
			}
		}
		double[] a = myNewMap.get(new Key(myPools)).get(new Key(new int[4]));
		System.out.println("avg cost/ticket = "+ (a[4]/a[5]));
		System.out.println("avg lock cost/ticket = " + (a[7]/a[5]));
	}
	
	public static HashMap<Key,HashMap<Key,double[]>> copyMap(HashMap<Key,HashMap<Key,double[]>> oldMap, int[] myPools) {
		HashMap<Key,HashMap<Key,double[]>> myMap = new HashMap<Key, HashMap<Key, double[]>>();
		for(int i = 0; i <= myPools[0]; i++) {
			for(int j = 0; j <= myPools[1]; j++) {
				for(int k = 0; k <= myPools[2]; k++) {
					for(int l = 0; l <= myPools[3]; l++) {
						int[] thisPools = new int[4];
						thisPools[0] = i;
						thisPools[1] = j;
						thisPools[2] = k;
						thisPools[3] = l;
						HashMap<Key,double[]> thisMap = new HashMap<Key,double[]>();
						HashMap<Key,double[]> currentOldMap = oldMap.get(new Key(thisPools));
						for(int a = 0; a < 5; a++) {
							for(int b = a; b < 5; b++) {
								for(int c = b; c < 5; c++) {
									for(int d = c; d < 5; d++) {
										int[] thisSlots = new int[4];
										thisSlots[0] = a;
										thisSlots[1] = b;
										thisSlots[2] = c;
										thisSlots[3] = d;
										double[] myData = currentOldMap.get(new Key(thisSlots));
										if(myData!=null) {
											double[] copyData = new double[myData.length];
											for(int z = 0; z < myData.length; z++)
												copyData[z] = myData[z];
											thisMap.put(new Key(thisSlots), copyData);
										}
									}
								}
							}
						}
						myMap.put(new Key(thisPools), thisMap);
					}
				}
			}
		}
		return myMap;
	}

	public static int[] sortArray(int[] a) {
		int[] b = new int[5];
		for(int i = 0; i < 4; i++)
			b[a[i]]++;
		int[] c = new int[4];
		int x = 0;
		for(int i = 0; i < 5; i++)
			for(int j = 0; j < b[i]; j++) {
				c[x] = i;
				x++;
			}
		return c;
	}
	
	private static class Key {

		private final int w;
	    private final int x;
	    private final int y;
	    private final int z;

	    public Key(int w, int x, int y, int z) {
	    	this.w = w;
	        this.x = x;
	        this.y = y;
	        this.z = z;
	    }
	    
	    public Key(int[] a) {
	    	this.w = a[0];
	    	this.x = a[1];
	    	this.y = a[2];
	    	this.z = a[3];
	    }

	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (!(o instanceof Key)) return false;
	        Key key = (Key) o;
	        return w == key.w && x == key.x && y == key.y && z == key.z;
	    }

	    @Override
	    public int hashCode() {
	        int result = w;
	        result = 41 * result + x;
	        result = 41* result +y;
	        result = 41* result +z;
	        return result;
	    }

	}
}
