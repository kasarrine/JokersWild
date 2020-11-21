Team: Meta Heads
Members: Richard Aviles, Kirk Sarrine, Garrett West
Course: COSC 1174 - Fall 2020
Project: Jokers Wild!
Due: 11/22/2020

Jokers Wild!

This program simulates one user playing a 5 card hand of poker. The deck used has 54 cards: 52 standard cards and 2 jokers. The jokers are wildcards.

The objective of the game is for the user to deal a hand, select which cards they would like to hold by clicking on them, then clicking on draw to replace the cards that are not being held. The user is required to discard at least one card.

The user starts out with $200 in their account. Before dealing, the user is required to bet $1, $10 or $100. Once the deal button is clicked, the bet amount is debited from the user's account.

At the draw point is when the round ends. If the user has a winning hand, the payout amount is credited to the user's account and the type of winning hand is displayed in the game.

Here is a list of the types of winning hands and the tiered payout amounts, ranked from greatest to least:

Royal Flush			-> 10x
Straight Flush		-> 9x
Four of a Kind		-> 8x
Full House			-> 7x
Flush				-> 6x
Straight			-> 5x
Three of a Kind		-> 4x
Two pairs			-> 3x

Note that a single pair is not considered a winning hand.
The following is a list of 4 required features that were implemented in the game for our group of 3:

1.	An internal trigger that reshuffles the decks when less than 25% of the cards are left
2.	Remove the hold buttons, and move the function to clicking on the cards themselves
3.	When a card is selected for discard, it is flipped face down
4.	Options for at least 6 different avatars in the top left corner that scroll through the options when clicked

There are 6 class files: Card, Chips, Deck, Hand, Icons and Poker.
The image files for the cards and avatars are stored within the cards and icons subfolders of the images folder.

The development of this program was written in IntelliJ on macOS, Microsoft and Linux. Subsequently, the QA Testing of this program was run on all platforms.

For requirements, in IntelliJ on all platforms the JavaFX Library is required to be loaded into the Project Structure. Additonally, VM arguments may be required in the run configuration. On macOS, the correct arguments are the following:

--module-path **JAVA FX LIBRARY PATH*** --add-modules javafx.controls,javafx.fxml