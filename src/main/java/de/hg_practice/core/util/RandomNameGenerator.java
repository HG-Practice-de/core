package de.hg_practice.core.util;

import java.util.Random;

public class RandomNameGenerator {

    private static final char[] CONSONANTS = "bcdfghjklmnpqrstvwxyz".toCharArray();
    private static final char[] VOCALS = "aeiou".toCharArray();
    private final Random random;

    public RandomNameGenerator(Random random) {
        this.random = random;
    }

    public RandomNameGenerator() {
        this.random = new Random();
    }

    public String getRandomName(int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i % 2 == 0) {
                builder.append(getRandomChar(CONSONANTS));
            } else {
                builder.append(getRandomChar(VOCALS));
            }
        }
        return builder.toString();
    }

    private char getRandomChar(char[] chars) {
        return chars[random.nextInt(chars.length)];
    }


    public static void main(String[] args) {
        RandomNameGenerator generator = new RandomNameGenerator();
        for (int i = 0; i < 100; i++) {
            System.out.println(generator.getRandomName(6));
        }
    }

}
