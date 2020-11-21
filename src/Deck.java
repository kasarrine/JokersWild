/*
Team: Meta Heads
Members: Richard Aviles, Kirk Sarrine, Garrett West
Course: COSC 1174 - Fall 2020
Project: Jokers Wild!
Due: 11/22/2020
 */

import java.util.*;

public class Deck {
    // suits and ranks are used to build out a new Deck
    private static final String[] SUITS =  new String[] {"Hearts", "Diamonds", "Spades", "Clubs"};
    private static final String[] RANKS = new String[] {"Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
            "Ten", "Jack", "Queen", "King", "Ace"};
    // ArrayList will store all of the 54 Cards (2 Jokers are Wild Cards)
    private ArrayList<Card> deck;

    /** Default constructor for Deck. Creates the individual Deck Cards and adds them */
    public Deck() {
        deck = new ArrayList<>();

        for(String suit: SUITS)                   // Nested enhanced for loop to build the deck from the suit and ranks
            for(String rank: RANKS)
                deck.add(new Card(suit, rank));
        deck.add(new Card("RedJoker", "Joker" ));
        deck.add(new Card("BlackJoker", "Joker"));
    }

    /** toString() returns all of the Cards in the Deck */
    public String toString() {
        StringBuilder deckComp = new StringBuilder();
        for(Card oneCard: deck) {
            deckComp.append("\n ").append(oneCard.toString());
        }
        return "The deck has:" + deckComp + "\n";
    }

    /** Deals the top card and removes from the Deck */
    public Card deal() {
        return deck.remove(0);
    }

    /** Removes all Jokers from the Deck */
    public void removeJokers()
    {
        deck.remove(new Card("RedJoker", "Joker" ));
        deck.remove(new Card("BlackJoker", "Joker" ));
    }

    /** Adds cards from a discarded pile to the Deck when Deck has 25% or less cards left */
    public void addCards(ArrayList<Card> discardedCards)
    {
        // If deck size is less than 25%, add all cards from the discarded pile
        if (deck.size()/54.0 <= 25.0/100.0 ) {
            deck.addAll(discardedCards);     // Add all discardedCards
            discardedCards.clear();          // Clear discardedCards
            shuffle();                       // Shuffle the deck
        }
    }

    /** Getter for the deck */
    public ArrayList<Card> getDeck() { return deck; }

    /** Shuffles the Deck 5 times via Collections.shuffle() */
    public void shuffle() {
        for (int i = 0; i < 5; i++)
            Collections.shuffle(deck);
    }
}