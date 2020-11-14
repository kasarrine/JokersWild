/*
Team: Meta Heads
Members: Richard Aviles, Kirk Sarrine, Garrett West
Course: COSC 1174 - Fall 2020
Project: Jokers Wild!
Due: 11/22/2020
 */

import javafx.application.Application;
import javafx.stage.Stage;


public class HandTest extends Application {
    public static void main(String[] args)
    {
        Card redJoker = new Card("RedJoker", "Joker");
        Card blackJoker = new Card("BlackJoker", "Joker");
        // Flush
        Hand hand = new Hand();
        twoJokerTests();
    }

    public static void oneJokerTests() {
        Hand hand = new Hand();
        Card redJoker = new Card("RedJoker", "Joker");
        Card blackJoker = new Card("BlackJoker", "Joker");
        System.out.println("+++ONE JOKER TESTS+++");

        // Royal Flush
        hand = new Hand();
        System.out.println();
        System.out.println("Royal Flush Test.");
        hand.addCard(new Card("Diamonds", "Ace"));
        hand.addCard(new Card("Diamonds", "Ten"));
        hand.addCard(new Card("Diamonds", "Queen"));
        hand.addCard(new Card("Diamonds", "Jack"));
        hand.addCard(redJoker);
        System.out.println(hand);
        System.out.println(hand.checkForWins());


        // Straight flush
        hand = new Hand();
        System.out.println();
        System.out.println("Straight Flush Test.");
        hand.addCard(new Card("Spades", "Three"));
        hand.addCard(new Card("Spades", "Seven"));
        hand.addCard(new Card("Spades", "Five"));
        hand.addCard(new Card("Spades", "Four"));
        hand.addCard(redJoker);
        System.out.println(hand);
        System.out.println(hand.checkForWins());

        // Four of a Kind
        hand = new Hand();
        System.out.println();
        System.out.println("Four of a Kind Test.");
        hand.addCard(new Card("Hearts", "Jack"));
        hand.addCard(new Card("Diamonds", "Jack"));
        hand.addCard(new Card("Diamonds", "Seven"));
        hand.addCard(new Card("Clubs", "Jack"));
        hand.addCard(redJoker);
        System.out.println(hand);
        System.out.println(hand.checkForWins());

        // Full House
        hand = new Hand();
        System.out.println();
        System.out.println("Full House Test.");
        hand.addCard(new Card("Hearts", "Ten"));
        hand.addCard(new Card("Diamonds", "Nine"));
        hand.addCard(new Card("Spades", "Ten"));
        hand.addCard(new Card("Clubs", "Nine"));
        hand.addCard(redJoker);
        System.out.println(hand);
        System.out.println(hand.checkForWins());


        // Flush
        hand = new Hand();
        System.out.println();
        System.out.println("Flush Test.");
        hand.addCard(new Card("Spades", "Four"));
        hand.addCard(new Card("Spades", "Jack"));
        hand.addCard(new Card("Spades", "Eight"));
        hand.addCard(new Card("Spades", "Two"));
        hand.addCard(redJoker);
        System.out.println(hand);
        System.out.println(hand.checkForWins());

        // Straight
        hand = new Hand();
        System.out.println();
        System.out.println("Straight Test.");
        hand.addCard(new Card("Heart", "Five"));
        hand.addCard(new Card("Clubs", "Nine"));
        hand.addCard(new Card("Spades", "Seven"));
        hand.addCard(new Card("Diamonds", "Six"));
        hand.addCard(redJoker);
        System.out.println(hand);
        System.out.println(hand.checkForWins());

        // Three of a Kind
        hand = new Hand();
        System.out.println();
        System.out.println("Three of a Kind Test.");
        hand.addCard(new Card("Clubs", "Seven"));
        hand.addCard(new Card("Diamonds", "Seven"));
        hand.addCard(new Card("Clubs", "King"));
        hand.addCard(new Card("Diamonds", "Three"));
        hand.addCard(redJoker);
        System.out.println(hand);
        System.out.println(hand.checkForWins());
        System.out.println();
    }

    public static void twoJokerTests() {
        Hand hand = new Hand();
        Card redJoker = new Card("RedJoker", "Joker");
        Card blackJoker = new Card("BlackJoker", "Joker");
        System.out.println("+++TWO JOKER TESTS+++");

        // Royal Flush
        System.out.println();
        System.out.println("Royal Flush Test.");
        hand.addCard(new Card("Diamonds", "Ace"));
        hand.addCard(new Card("Diamonds", "Queen"));
        hand.addCard(new Card("Diamonds", "Jack"));
        hand.addCard(redJoker);
        hand.addCard(blackJoker);
        System.out.println(hand);
        System.out.println(hand.checkForWins());

        // Straight flush
        hand = new Hand();
        System.out.println();
        System.out.println("Straight Flush Test.");
        hand.addCard(new Card("Spades", "Three"));
        hand.addCard(new Card("Spades", "Seven"));
        hand.addCard(new Card("Spades", "Four"));
        hand.addCard(redJoker);
        hand.addCard(blackJoker);
        System.out.println(hand);
        System.out.println(hand.checkForWins());

        // Four of a Kind
        hand = new Hand();
        System.out.println();
        System.out.println("Four of a Kind Test.");
        hand.addCard(new Card("Diamonds", "Jack"));
        hand.addCard(new Card("Diamonds", "Seven"));
        hand.addCard(new Card("Clubs", "Jack"));
        hand.addCard(redJoker);
        hand.addCard(blackJoker);
        System.out.println(hand);
        System.out.println(hand.checkForWins());

        // Full House
        hand = new Hand();
        System.out.println();
        System.out.println("Full House Test.");
        hand.addCard(new Card("Hearts", "Ten"));
        hand.addCard(new Card("Diamonds", "Nine"));
        hand.addCard(new Card("Spades", "Ten"));
        hand.addCard(redJoker);
        hand.addCard(blackJoker);
        System.out.println(hand);
        System.out.println(hand.checkForWins());


        // Flush
        hand = new Hand();
        System.out.println();
        System.out.println("Flush Test.");
        hand.addCard(new Card("Spades", "Four"));
        hand.addCard(new Card("Spades", "Jack"));
        hand.addCard(new Card("Spades", "Eight"));
        hand.addCard(redJoker);
        hand.addCard(blackJoker);
        System.out.println(hand);
        System.out.println(hand.checkForWins());

        // Straight
        hand = new Hand();
        System.out.println();
        System.out.println("Straight Test.");
        hand.addCard(new Card("Hearts", "Five"));
        hand.addCard(new Card("Clubs", "Nine"));
        hand.addCard(new Card("Spades", "Seven"));
        hand.addCard(redJoker);
        hand.addCard(blackJoker);
        System.out.println(hand);
        System.out.println(hand.checkForWins());

        // Three of a Kind
        hand = new Hand();
        System.out.println();
        System.out.println("Three of a Kind Test.");
        hand.addCard(new Card("Clubs", "Seven"));
        hand.addCard(new Card("Clubs", "King"));
        hand.addCard(new Card("Diamonds", "Three"));
        hand.addCard(redJoker);
        hand.addCard(blackJoker);
        System.out.println(hand);
        System.out.println(hand.checkForWins());
    }
    @Override
    public void start(Stage stage) {

    }
}
