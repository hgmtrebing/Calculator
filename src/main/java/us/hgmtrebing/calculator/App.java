package us.hgmtrebing.calculator;

import java.util.Scanner;

public class App {

    public static void main( String[] args ) {

        Calculator c = new Calculator();
        Scanner s = new Scanner(System.in);
        String userInput = "";

        while (true) {
            userInput = s.nextLine();
            try {
                System.out.println (c.evaluate(userInput));
            } catch (Exception e) {
                System.out.println (e.getMessage());
            }
        }
    }
}
