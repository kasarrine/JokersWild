/*
Team: Meta Heads
Members: Richard Aviles, Kirk Sarrine, Garrett West
Course: COSC 1174 - Fall 2020
Project: Jokers Wild!
Due: 11/22/2020
 */

public class Chips {
    private int balance, bet, wins, losses;

    /** Default constructor for Chips. Balance is set to 200 by default */
    public Chips(){
        balance = 200;
    }

    /** Checks for a winning hand. */
    public void checkForWin(Hand hand) {
        switch (hand.checkForWins()) {
            case "Royal Flush." -> setPayout(10);
            case "Straight Flush." -> setPayout(9);
            case "Four of a kind." -> setPayout(8);
            case "Full House." -> setPayout(7);
            case "Flush." -> setPayout(6);
            case "Straight." -> setPayout(5);
            case "Three of a kind." -> setPayout(4);
            case "Two pairs." -> setPayout(3);
            default -> losses++;
        }
        clearBet();
    }

    /** Sets the payout based on the type of a winning hand */
    private void setPayout(int num)
    {
        balance += (num * bet);
        wins++;
    }

    /** Clears the current bet */
    private void clearBet()
    {
        bet = 0;
    }

    /** Getter for balance  */
    public int getBalance() {
        return balance;
    }

    /** Decrements the current bet to the account balance  */
    public void applyBetToBalance() {
        this.balance -= bet;
    }

    /** Setter for the bet  */
    public void setBet(int bet) {
        this.bet = bet;
    }

    /** Getter for the number of wins  */
    public int getWins() {
        return wins;
    }

    /** Getter for the number of losses  */
    public int getLosses() {
        return losses;
    }

}
