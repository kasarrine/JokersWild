/*
Team: Meta Heads
Members: Richard Aviles, Kirk Sarrine, Garrett West
Course: COSC 1174 - Fall 2020
Project: Jokers Wild!
Due: 11/22/2020
 */

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import java.util.Objects;

public class Card extends ImageView implements Comparable<Card> {
    private final String suit;
    private final String rank;
    private int order = 0;
    private boolean hold = false;

    /** Default constructor for Card. Creates an new Card with the suit, rank and value */
    public Card(String suit, String rank) {
        super(new Image("file:cards/" + rank + "Of" + suit + ".png"));
        switch (rank) {
            case "One" -> order = 1;
            case "Two" -> order = 2;
            case "Three" -> order = 3;
            case "Four" -> order = 4;
            case "Five" -> order = 5;
            case "Six" -> order = 6;
            case "Seven" -> order = 7;
            case "Eight" -> order = 8;
            case "Nine" -> order = 9;
            case "Ten" -> order = 10;
            case "Jack" -> order = 11;
            case "Queen" -> order = 12;
            case "King" -> order = 13;
            case "Ace" -> order = 14;
            case "Joker" -> order = 15;
        }

        this.suit = suit;
        this.rank = rank;
    }

    /** toString() returns a string representation of the Card with the rank, suit and filePath of the Card image */
    public ImageView getBackImage() {
        if (suit.equals("Hearts") || suit.equals("Diamonds") || suit.equals("RedJoker"))
            return new ImageView("file:cards/" + "RedBack.png");
        else return new ImageView("file:cards/" + "BlackBack.png");
    }

    /** toString() returns a string representation of the Card with the rank, suit and filePath of the Card image */
    public String toString(){ return rank + " of " + suit; }

    /** Getter for Card suit */
    public String getSuit() {
        return suit;
    }

    /** Getter for Card Rank */
    public String getRank() {
        return rank;
    }

    /** Getter for Card Order */
    public int getOrder() {
        return order;
    }

    /** Setter to hold a Card */
    public void hold() {
        this.hold = true;
    }

    /** Setter to remove a hold */
    public void removeHold() {
        hold = false;
    }

    /** Getter for Card hold */
    public boolean checkHold() {
        return hold;
    }

    /** Checks if a Card is a Joker */
    public boolean isJoker() {
        return rank.equals("Joker");
    }

    /** Equals method for Card */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Card card = (Card) o;
        return suit.equals(card.suit) && rank.equals(card.rank);
    }

    /** HashCode method for Card */
    @Override
    public int hashCode() {
        return Objects.hash(suit, rank);
    }

    /** CompareTo method for Card */
    @Override
    public int compareTo(Card o) {
        return Integer.compare(this.order, getOrder());
    }
}