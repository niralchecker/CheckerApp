package com.checker.sa.android.helper;

import java.util.Random;

public class Randomm {

	Random randomGenerator;
	int[] randomnumbers;
	int counter = 0;

	public Randomm(int seed) {
		counter = 0;
		randomnumbers = new int[1000];
		randomGenerator = new Random(seed);
		for (int i = 0; i < randomnumbers.length; i++) {
			int n = randomGenerator.nextInt(20);
			randomnumbers[i] = n;
		}
	}

	public int nextInt(int size) {
		counter++;
		if (counter > 999)
			counter = 0;
		for (; counter < randomnumbers.length; counter++) {
			if (randomnumbers[counter] < size)
				return randomnumbers[counter];
		}
		return randomnumbers[0];
	}

	public void reresh() {
		counter = 0;

	}

}
