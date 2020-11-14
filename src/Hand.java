/*
Name: Kirk Sarrine
LUID: L20178451
Course: COSC 1174 - Fall 2020
Date: 10/18/2020
Assignment: HW6 - Poker #3
 */

import java.util.*;

public class Hand {
    private ArrayList<Card> cards;                      // Stores the actual cards
    private HashMap<String, ArrayList<String>> suites;  // Used for hand checks, suite(keys): ranks(values)
    // Used for hand checks (excludes suites), ranks(keys): occurrences(value)
    private HashMap<String, Integer> ranks;

    /**
     * Default Constructor for Hand. Creates a new Hand of Cards
     */
    public Hand() {
        cards = new ArrayList<>();
        suites = new HashMap<>();
        ranks = new HashMap<>();
    }

    /**
     * Constructor for Hand. Creates a new Hand of Cards from another Hand
     */
    public Hand(Hand prevHand) {
        cards = new ArrayList<>();
        suites = prevHand.getSuites();
        ranks = prevHand.getRanks();
        for (int i = 0; i < prevHand.size(); i++)
            cards.add(i, prevHand.getCard(i));
    }

    /** Returns the number of Jokers in the Hand */
    public int countJokers(){
        int count = 0;
        for(Card card: cards)
            if (card.isJoker())
                count++;

        return count;
    }

    /**
     * Sorts the hand by card rank from least to greatest
     */
    public void sort() {
        cards.sort(Comparator.comparingInt(Card::getOrder));
    }

    /**
     * Adds the card to the Suite HashMap
     */
    private void addToSuiteHashMap(Card card) {
        if (suites.containsKey(card.getSuit()))
            suites.get(card.getSuit()).add(card.getRank());
        else
            suites.put(card.getSuit(), new ArrayList<>(Collections.singleton(card.getRank())));
    }

    /**
     * Adds the card to the Rank HashMap
     */
    private void addToRankHashMap(Card card) {
        if (ranks.containsKey(card.getRank())) {
            int currentRank = ranks.get(card.getRank());
            ranks.put(card.getRank(), currentRank + 1);
        } else ranks.put(card.getRank(), 1);
    }

    /**
     * Removes the card from Suite HashMap
     */
    private void removeFromSuiteHashMap(Card card) {
        if (suites.containsKey(card.getSuit()))
            suites.get(card.getSuit()).remove(card.getRank());
    }

    /**
     * Removes the card from Rank HashMap
     */
    private void removeFromRankHashMap(Card card) {
        String rank = card.getRank();
        if (ranks.containsKey(rank))
            ranks.put(rank, ranks.get(rank) - 1);
        if (ranks.get(rank) == 0)
            ranks.remove(rank);
    }

    /**
     * Adds the card to the Rank and Suite HashMaps
     */
    private void addToHashMaps(Card card) {
        addToSuiteHashMap(card);
        addToRankHashMap(card);
    }

    /**
     * Removes the card from the Rank and Suite HashMaps
     */
    private void removeFromHashMaps(Card card) {
        removeFromSuiteHashMap(card);
        removeFromRankHashMap(card);
    }

    /**
     * toString() returns all of the Cards in the Hand
     */
    public String toString() {
        StringBuilder deckComp = new StringBuilder();
        for (Card card : cards)
            deckComp.append("\n ").append(card.toString());
        return "The hand has:" + deckComp + "\n";
    }

    /**
     * Adds a Card to the Hand and updates the total value of the hand
     */
    public void addCard(Card card) {
        cards.add(card);
        addToHashMaps(card);
    }

    /**
     * Adds a Card to the Hand and updates the total value of the hand
     */
    public void addCard(int index, Card card) {
        cards.add(index, card);
        addToHashMaps(card);
    }


    /**
     * Removes a Card from the Hand and updates the total value of the hand
     */
    public void removeCard(int index) {
        removeFromHashMaps(cards.get(index));
        cards.remove(index);
    }

    /**
     * Removes a Card from the Hand and updates the total value of the hand
     */
    public void removeCard(Card card) {
        removeFromHashMaps(card);
        cards.remove(card);
    }

    /**
     * Get the card at the specified index
     */
    public Card getCard(int index) {
        return cards.get(index);
    }


    /**
     * Get the size of the hand
     */
    public int size() {
        return cards.size();
    }

    /**
     * Clear all holds in the hand
     */
    public void removeHolds() {
        for (Card card : cards)
            card.removeHold();
    }

    /**
     * Checks if the Hand is empty and returns a boolean
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Removes Jokers from hand
     */
    private void removeJokers() {
        cards.removeIf(Card::isJoker);
        removeFromHashMaps(new Card("RedJoker", "Joker" ));
        removeFromHashMaps(new Card("BlackJoker", "Joker" ));
    }

    /**
     * Checks Hand to see if all cards contain the same suit
     */
    private boolean checkOrder(){
        sort();
        int firstCardOrder = getCard(0).getOrder();
        for(Card card: cards)
        {
            if (card.getOrder() != firstCardOrder)
                return false;
            firstCardOrder++;
        }
        return true;
    }

    /**
     * Checks Hand to see if all cards contain the same suit
     */
    private boolean checkSuites(){
        String suit = getCard(0).getSuit();
        for (Card card: cards)
            if (!card.getSuit().equals(suit))
                return false;
        return true;
    }

    /**
     * Master Method for checking pairs in a Hand
     */
    private boolean checkForPairs(int count, int frequency) {
        return Collections.frequency(new ArrayList<>(ranks.values()), count) == frequency;
    }

    /** Checks Hand for a Full house */
    private boolean checkForFullHouse(){
        return checkForPairs(3, 1) && checkForPairs(2, 1);
    }


    /** Checks Hand for Four of a Kind */
    private boolean checkForFourOfAKind() {
        return (checkForPairs(4, 1)); }

    /** Checks Hand for Three of a Kind */
    private boolean checkForThreeOfAKind() {
        return checkForPairs(3, 1); }

    /** Checks Hand for Two of a Kind */
    private boolean checkForTwoPairs () {
        return checkForPairs(2, 2); }

    /**
     * Checks Hand for a Royal Flush
     */
    private boolean checkForRoyalFlush() {
        Set<String> keys = new HashSet<>(Arrays.asList("Ace", "King", "Queen", "Jack", "Ten"));
        for (Map.Entry<String, ArrayList<String>> entry : suites.entrySet()) {
            Set<String> values = new HashSet<>(entry.getValue());
            if (keys.containsAll(values) && values.containsAll(keys)) return true;
        }
        return false;
    }

    /**
     * Checks Hand for a Flush
     */
    private boolean checkForFlush() {
       return !checkOrder() && checkSuites();
    }

    /**
     * Checks Hand for a Straight Flush
     */
    private boolean checkForStraightFlush() {
        return  checkOrder() && checkSuites();
    }

    /**
     * Checks Hand for a Straight
     */
    private boolean checkForStraight() {
        return checkOrder() && !checkSuites();
    }

    /**
     * Getter for Suites HashMap
     */
    public HashMap<String, ArrayList<String>> getSuites() {
        return suites;
    }

    /**
     * Getter for Ranks HashMap
     */
    public HashMap<String, Integer> getRanks() {
        return ranks;
    }

    /**
     * Master method to check for a winning hand
     */
    private boolean checkForWin() {
        sort();
        return checkForRoyalFlush() || checkForStraightFlush() || checkForFourOfAKind()
                || checkForFullHouse() || checkForFlush() || checkForStraight()
                || checkForThreeOfAKind() || checkForTwoPairs();
    }

    /**
     * Test method for debugging winning hand types
     */
    private String handWinType() {
        sort();
        if (checkForRoyalFlush())
            return "Royal Flush.";
        else if (checkForStraightFlush())
            return "Straight Flush.";
        else if (checkForFourOfAKind())
            return "Four of a kind.";
        else if (checkForFullHouse())
            return "Full House";
        else if (checkForFlush())
            return "Flush.";
        else if (checkForStraight())
            return "Straight.";
        else if (checkForThreeOfAKind())
            return "Three of a kind.";
        else if (checkForTwoPairs())
            return "Two pairs.";
        else
            return "";
    }

    /**
     * Test method for debugging winning hand types
     */
    private Integer checkJokerWin() {
        sort();
        Set<Integer> wins = new HashSet<>();
        if (checkForRoyalFlush()){
            wins.add(9);
        }
        else if (checkForStraightFlush()) {
            wins.add(8);
        }
        else if (checkForFourOfAKind()) {
            wins.add(7);
        }
        else if (checkForFullHouse()) {
            wins.add(6);
        }
        else if (checkForFlush()) {
            wins.add(5);
        }
        else if (checkForStraight()) {
            wins.add(4);
        }
        else if (checkForThreeOfAKind()) {
            wins.add(3);
        }
        else if (checkForTwoPairs()) {
            wins.add(2);
        }
        else {
            wins.add(0);
        }

        return Collections.max(wins);
    }

    /**
     *  Checks
     */
    private String getJokerWinType(int num) {
        return switch (num) {
            case 10 -> "Five of a kind.";
            case 9 -> "Royal Flush.";
            case 8 -> "Straight Flush.";
            case 7 -> "Four of a kind.";
            case 6 -> "Full House.";
            case 5 -> "Flush.";
            case 4 -> "Straight.";
            case 3 -> "Three of a kind.";
            case 2 -> "Two pairs.";
            default -> "";
        };
    }

    /**
     * Test method for debugging winning hand types with Joker
     */
    public String testOneJoker() {
        Deck deck = new Deck();
        deck.removeJokers();
        int win = 0; // set to no win
        for (Card card : cards) {
            if (card.isJoker()) {
                for (Card deckCard : deck.getDeck()) {
                    ArrayList<Card> tempCards = new ArrayList<>(cards);
                    tempCards.remove(card);
                    tempCards.add(deckCard);
                    Hand tempHand = new Hand();
                    for (Card card1 : tempCards)
                        tempHand.addCard(card1);

                    if (tempHand.checkForWin()) {
                        if (tempHand.checkJokerWin() > win)
                            win = tempHand.checkJokerWin();
                    }
                }
            }
        }
        return getJokerWinType(win);
    }

    public String testTwoJokers() {
        Deck deck = new Deck();
        deck.removeJokers();
        int win = 0; // set to no win
        ArrayList<Card> tempCards = new ArrayList<>();
        for (Card card: cards)
            if (!card.isJoker()) tempCards.add(card);

        for (Card deckCard1: deck.getDeck()){
            for (Card deckCard2: deck.getDeck())
            {
                if (deckCard1 != deckCard2 && !cards.contains(deckCard1) && !cards.contains(deckCard2)) {
                    Hand tempHand = new Hand();
                    for (Card card1 : tempCards)
                        tempHand.addCard(card1);
                    tempHand.addCard(deckCard1);
                    tempHand.addCard(deckCard2);

                    if (tempHand.checkForWin()) {
                        if (tempHand.checkJokerWin() > win)
                            win = tempHand.checkJokerWin();
                    }
                }
            }
        }
        return getJokerWinType(win);
    }

    public String checkForWins(){
        Hand testHand = new Hand(this);
        int jokerCount = testHand.countJokers();
        if (jokerCount == 2)
            return testHand.testTwoJokers();
        else if (jokerCount == 1)
            return testHand.testOneJoker();
        else
            return testHand.handWinType();
    }
}