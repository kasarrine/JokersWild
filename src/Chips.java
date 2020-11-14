/*
Name: Kirk Sarrine
LUID: L20178451
Course: COSC 1174 - Fall 2020
Date: 10/18/2020
Assignment: HW6 - Poker #3
 */

public class Chips {
    private int balance, bet, wins, losses;

    /** Default constructor for Chips. Balance is set to 200 by default */
    public Chips(){
        balance = 200;
    }

    /** Checks for a winning hand. If  */
    public void checkForWin(Hand hand){
        if (!hand.checkForWins().equals("")) {
            wins++;
            balance += (2 * bet);
        }
        else {
            losses++;
        }
        bet = 0;
    }

    /** Getter for balance  */
    public int getBalance() { return balance; }

    /** Decrements the current bet to the account balance  */
    public void applyBetToBalance() { this.balance -= bet; }

    /** Setter for the bet  */
    public void setBet(int bet) { this.bet = bet; }

    /** Getter for the number of wins  */
    public int getWins() {
        return wins;
    }

    /** Getter for the number of losses  */
    public int getLosses() { return losses; }

}
