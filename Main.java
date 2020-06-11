package readability;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        //Initializing variables
        File file = new File(args[0]);
        int words = 0;
        int sent = 0;
        int ch = 0;
        int sylb = 0;
        int psylab = 0;

        Pattern w = Pattern.compile("\\w+(,\\d+)*");
        Pattern se = Pattern.compile("[.?!]|\\w+$");
        Pattern c = Pattern.compile("\\S+");

        //Read File
        try (Scanner scanner = new Scanner(file)) {
            System.out.println("The text is:");
            while (scanner.hasNext()) {
                String s = scanner.nextLine();
                Matcher wo = w.matcher(s);
                Matcher sen = se.matcher(s);
                Matcher cha = c.matcher(s);
                while (cha.find()) {
                    ch += cha.group().length();
                }
                while (wo.find()) {
                    int x = sylab(wo.group());
                    sylb += x;
                    if (x > 2) {
                        psylab++;
                    }

                    words++;
                }
                while (sen.find()) {
                    sent++;
                }
                System.out.println(s);
            }
            printData(words, sent, ch, sylb, psylab);
            System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
            Scanner in = new Scanner(System.in);
            System.out.println();
            switch (in.next()) {
                case "ARI":
                    ari(ch, words, sent);
                    break;
                case "FK":
                    fk(words, sent, sylb);
                    break;
                case "SMOG":
                    smog(psylab, sent);
                    break;
                case "CL":
                    cl(ch, words, sent);
                    break;
                case "all":
                    int a = ari(ch, words, sent);
                    int fk = fk(words, sent, sylb);
                    int smog = smog(psylab, sent);
                    int cl = cl(ch, words, sent);
                    System.out.println();
                    System.out.printf("This text should be understood in average by %.02f year olds.\n", (double) (a + fk + smog + cl) / 4);
                    break;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File Do not Exists");
        }
    }

    static void printData(int w, int s, int c, int sy, int ps) {
        System.out.println();
        System.out.println("Words: " + w);
        System.out.println("Sentences: " + s);
        System.out.println("Characters: " + c);
        System.out.println("Syllables:" + sy);
        System.out.println("Polysyllables:" + ps);

    }

    static int sylab(String s) {
        int x = 0;
        char[] c = s.toLowerCase().toCharArray();
        if (checkchars(c[0])) {
            x++;
        }
        if(c.length>2) {
            for (int i = 1; i < c.length - 1; i++) {
                if (checkchars(c[i]) && !(checkchars(c[i - 1]))) {
                    x++;
                }
            }
            if (!(c[c.length - 1] == 'e') && checkchars(c[c.length - 1]) && !checkchars(c[c.length - 2])) {
                x++;
            }
        }
        return x == 0 ? 1 : x;
    }

    static boolean checkchars(char c) {
        return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' || c == 'y';
    }

    static int scoreSys(int x) {
        switch (x) {
            case 1:
                System.out.println("(about 6 year olds).");
                return 6;
            case 2:
                System.out.println("(about 7 year olds).");
                return 7;
            case 3:
                System.out.println("(about 9 year olds).");
                return 9;
            case 4:
                System.out.println("(about 10 year olds).");
                return 10;
            case 5:
                System.out.println("(about 11 year olds).");
                return 11;
            case 6:
                System.out.println("(about 12 year olds).");
                return 12;
            case 7:
                System.out.println("(about 13 year olds).");
                return 13;
            case 8:
                System.out.println("(about 14 year olds).");
                return 14;
            case 9:
                System.out.println("(about 15 year olds).");
                return 15;
            case 10:
                System.out.println("(about 16 year olds).");
                return 16;
            case 11:
                System.out.println("(about 17 year olds).");
                return 17;
            case 12:
                System.out.println("(about 18 year olds).");
                return 18;
            case 13:
                System.out.println("(about 24 year olds).");
                return 24;
            default:
                System.out.println("(about 24+ year olds).");
                return 24;
        }

    }

    static int ari(double c, double w, double s) {
        double score = 4.71 * (c / w) + 0.5 * (w / s) - 21.43;
        System.out.printf("Automated Readability Index: %.02f ", score);
        return scoreSys((int) Math.floor(score));
    }

    static int fk(double w, double s, double sy) {
        double score = 0.39 * (w / s) + 11.8 * (sy / w) - 15.59;
        System.out.printf("Flesch–Kincaid readability tests: %.02f ", score);
        return scoreSys((int) Math.floor(score));
    }

    static int smog(double p, double s) {
        double score = 1.043 * Math.sqrt(p * 30 / s) + 3.1291;
        System.out.printf("Simple Measure of Gobbledygook: %.02f ", score);
        return scoreSys((int) Math.floor(score));
    }

    static int cl(double c, double w, double s) {
        double L = c / w * 100;
        double S = s / w * 100;
        double score = 0.0588 * L - 0.296 * S - 15.8;

        System.out.printf("Coleman–Liau index: %.02f ", score);
        return scoreSys((int) Math.ceil(score));
    }

}