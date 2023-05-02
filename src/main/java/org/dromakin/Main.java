package org.dromakin;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Main {

    private static final AtomicInteger beautyThree = new AtomicInteger(0);
    private static final AtomicInteger beautyFour = new AtomicInteger(0);
    private static final AtomicInteger beautyFive = new AtomicInteger(0);

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    private static boolean isAlphabaticOrder(String s) {
        int n = s.length();
        char[] copy = new char [n];

        IntStream.range(0, n).forEachOrdered(i -> copy[i] = s.charAt(i));
        Arrays.sort(copy);

        for (int i = 0; i < n; i++)
            if (copy[i] != s.charAt(i))
                return false;

        return true;
    }
    public static void main(String[] args) throws InterruptedException {

        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread thread3 = new Thread(() -> {
            for (String text : texts) {
                if (text.length() == 3) {
                    counterCriteria(text, beautyThree);
                }
            }
        });

        thread3.start();

        Thread thread4 = new Thread(() -> {
            for (String text : texts) {
                if (text.length() == 4) {
                    counterCriteria(text, beautyFour);
                }
            }
        });

        thread4.start();

        Thread thread5 = new Thread(() -> {
            for (String text : texts) {
                if (text.length() == 5) {
                    counterCriteria(text, beautyFive);
                }
            }
        });

        thread5.start();

        thread3.join();
        thread4.join();
        thread5.join();

        String base = "Красивых слов с длиной ";
        String stringBuilder =
                base + 3 + ": " + beautyThree.get() + " шт" + "\n" +
                base + 4 + ": " + beautyFour.get() + " шт" + "\n" +
                base + 5 + ": " + beautyFive.get() + " шт" + "\n";
        System.out.println(stringBuilder);
    }

    private static void counterCriteria(String text, AtomicInteger beautyCount) {
        if (new StringBuilder(text).reverse().toString().equals(text)) {
            beautyCount.incrementAndGet();
        }

        int count = (int) IntStream.range(0, text.length()).filter(j -> text.charAt(j) == text.charAt(0)).count();
        if (text.length() == count) {
            beautyCount.incrementAndGet();
        }

        if (isAlphabaticOrder(text)) {
            beautyCount.incrementAndGet();
        }
    }
}