/*
Team: Meta Heads
Members: Richard Aviles, Kirk Sarrine, Garrett West
Course: COSC 1174 - Fall 2020
Project: Jokers Wild!
Due: 11/22/2020
 */

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;


public class Poker extends Application {
    Deck deck = new Deck();                                             // New deck of cards
    Hand hand = new Hand();                                             // New hand of cards
    Chips chips = new Chips();                                          // New instance of chips
    ArrayList<Card> discardedCards = new ArrayList<>();                 // Used to store discarded cards
    ArrayList<ToggleButton> btHold = new ArrayList<>();                 // Used to create 5 hold buttons for the cards
    AtomicInteger dealCount = new AtomicInteger(0);           // AtomicInteger to count deal button clicks

    double centerX, centerY = 0.0;                                      // Used to get the center (x, y) on boarderPane
    HBox hBoxBTs = new HBox();                                          // Buttons are placed here
    VBox vBoxBets = new VBox();
    VBox vBoxStats = new VBox();
    GridPane gpCards = new GridPane();                                  // Cards are placed here
    BorderPane borderPane = new BorderPane();                           // Buttons and cards contained by BP
    Node btDraw = new Button("Draw");                                // Create buttons for draw and deal
    Node btDeal = new Button("Deal");

    boolean drawClicked;
    // Create TextFields and set initial values to each field
    TextField tfBalance = new TextField("Balance: $" + String.format("%,d", chips.getBalance()));
    TextField tfWins = new TextField("Wins: " + chips.getWins());
    TextField tfLosses = new TextField("Losses: " + chips.getLosses());
    TextField tfWinType = new TextField("Win Type: ");

    //
    final ToggleGroup betGroup = new ToggleGroup();
    RadioButton btBet1 = new RadioButton("$1");
    RadioButton btBet10 = new RadioButton("$10");
    RadioButton btBet100 = new RadioButton("$100");

    @Override
    public void start(Stage primaryStage) {
        deck.shuffle();                                                 // Initial deck shuffle
        primaryStage.setResizable(false);                               // Set the window to not be resizable
        primaryStage.setWidth(800);
        primaryStage.setHeight(400);

        gpCards.setAlignment(Pos.CENTER);                               // Set preferences and display size
        gpCards.setHgap(10);
        gpCards.setVgap(10);
        hBoxBTs.setSpacing(36);
        hBoxBTs.setStyle("-fx-border-color: grey");
        hBoxBTs.setAlignment(Pos.TOP_CENTER);
        borderPane.setPrefSize(600, 300);

        setDisableDealDraw();                                           // Initially disable bet and deal buttons

        btDeal.setOnMouseClicked(actionEvent -> {                       // Set Action for deal button
            drawClicked = false;
            chips.applyBetToBalance();
            tfBalance.setText("Balance: $" + chips.getBalance());       // Update to the current balance
            tfWinType.setText("Win Type: ");
            setDisableDealDraw();                                       // Disable bet and deal after click
            if (dealCount.get() < 1) {                                  // Only on first deal
                deselectAllHolds();                                     // Clear card holds if there are any
                dealFirstCards();                                       // Start animations
                dealCount.getAndIncrement();
            }
            else {                                                      // Condition for after first deal
                deselectAllHolds();
                drawCards();
            }});

        btDraw.setOnMouseClicked(actionEvent -> {                       // Set action for draw button
            if (dealCount.get() > 0 && !checkForHold()) {
                drawClicked = true;
                setDisableDealDraw();                                   // Disable bet and deal buttons
                clearBets();                                            // Clear bet RadioButtons
                drawCards();                                            // Draw cards
                checkWins();                                            // Check for winning hand
            }
        });

        hBoxBTs.getChildren().add(btDeal);                              // Deal button added first to hBox

        for (int i = 0; i < 5; ++i) {                                   // For loop used to create 5 hold buttons
            btHold.add(new ToggleButton("Hold"));
            int finalI = i;
            btHold.get(i).setOnMouseClicked(                            // Set action for hold button
                    actionEvent -> {
                        if (!hand.isEmpty()) {                          // Initial check for no empty hand
                            if (btHold.get(finalI).isSelected()) {
                                hand.getCard(finalI).hold();            // Set a hold on card in the deck
                                btDraw.setDisable(false);               // Re-enable draw button
                            } else hand.getCard(finalI).removeHold();   // Else remove the hold
                        }
                    });
            hBoxBTs.getChildren().add(btHold.get(i));                   // Add the hold button to the hBox
        }

        // Set the user data associated with each bet button and add them to the betGroup
        btBet1.setUserData(1);
        btBet10.setUserData(10);
        btBet100.setUserData(100);
        btBet1.setToggleGroup(betGroup);
        btBet10.setToggleGroup(betGroup);
        btBet100.setToggleGroup(betGroup);

        // Get bet from radio button and set the bet in chips
        betGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            if (betGroup.getSelectedToggle() != null) {
                btDeal.setDisable(false);
                int bet = (int) betGroup.getSelectedToggle().getUserData();
                chips.setBet(bet);
            }
        });

        hBoxBTs.getChildren().add(btDraw);                              // Add draw button as the last
        vBoxStats.getChildren().addAll(tfBalance, tfWins, tfWinType, tfLosses);
        vBoxBets.getChildren().addAll(btBet1, btBet10, btBet100);

        // Set Stats and Bets to the same size for spacing purposes
        vBoxStats.setPrefWidth(150);
        vBoxBets.setPrefWidth(150);
        gpCards.setPrefSize(400, 271);

        borderPane.setCenter(gpCards);                                  // Add hBoxCards to center of borderPane
        borderPane.setBottom(hBoxBTs);                                  // Add hBoxBTs to bottom of borderPane
        borderPane.setLeft(vBoxStats);                                  // Add vBoxStats to left of borderPane
        borderPane.setRight(vBoxBets);                                  // Add vBoxBets to right of borderPane

        Scene scene = new Scene(borderPane);                            // Create a scene and place it in the stage
        primaryStage.setTitle("Jokers Wild!");                          // Set the stage title
        primaryStage.setScene(scene);                                   // Place the scene in the stage
        primaryStage.show();                                            // Display the stage
    }

    /** Deals cards for the first initial deal */
    private void dealFirstCards() {
        ArrayList<PathTransition> ptNewBack = new ArrayList<>();        // PathTransitions for back of new cards
        ArrayList<RotateTransition> rtNewBack = new ArrayList<>();      // RotateTransition for back of new cards
        ArrayList<RotateTransition> rtNewFace = new ArrayList<>();      // RotateTransition for the new face cards

        if (dealCount.get() < 1) {                                      // Check the Atomic Counter, for first deal
            deck.shuffle();                                             // Shuffle the deck

            for (int i = 0; i < 5; i++) {
                hand.addCard(deck.deal());                              // Deal a new card, add it to the hand
                ImageView newFace = hand.getCard(i);                    // Get the face of the new card
                ImageView newBack = hand.getCard(i).getBackImage();     // Get the back of the new card

                centerX = newFace.getBoundsInParent().getCenterX();     // Used for centering the nodes
                centerY = newFace.getBoundsInParent().getCenterY();

                newFace.setRotate(90);                                  // Rotate face card 90 degrees

                GridPane.setConstraints(newBack, i, 0);             // Set constraints for new face and back images
                GridPane.setConstraints(newFace, i, 0);

                // Slide down from to animation
                ptNewBack.add(i, new PathTransition(Duration.millis(1000),
                        new Line(centerX, -500, centerX, centerY), newBack));

                // 90 degree slide flip of the new back and new face cards
                rtNewBack.add(i, createRotator(newBack, 0, 90, 500));
                rtNewFace.add(i, createRotator(newFace, 90, 0, 500));
                gpCards.getChildren().add(newBack);

                // Used for ArrayList index inside of even handlers
                int finalI = i;
                /*  Chained set of animations. New back slide down from the top. New back image flips 90 degrees,
                    removes the old new back image and adds the new face image. New face image was previously set at
                    90 degrees, so this flip is from 90 to 0 degrees. Deal and draw buttons are re-enabled once the
                    animations are complete.
                 */
                ptNewBack.get(i).setOnFinished((actionEvent -> rtNewBack.get(finalI).play()));
                rtNewBack.get(i).setOnFinished((actionEvent -> {
                    gpCards.getChildren().remove(newBack);
                    gpCards.getChildren().add(newFace);
                    rtNewFace.get(finalI).play();
                }));
                ptNewBack.get(i).setCycleCount(1);
                ptNewBack.get(i).play();
            }
        } else drawCards();
    }

    /** Draws cards and also used to deal cards after the first initial deal */
    public void drawCards() {
        ArrayList<PathTransition> ptOldBack = new ArrayList<>();        // PathTransitions for back of old cards
        ArrayList<PathTransition> ptNewBack = new ArrayList<>();        // PathTransitions for back of new cards
        ArrayList<RotateTransition> rtOldFace = new ArrayList<>();      // RotateTransition for old face cards
        ArrayList<RotateTransition> rtOldBack = new ArrayList<>();      // RotateTransition for back of old cards
        ArrayList<RotateTransition> rtNewBack = new ArrayList<>();      // RotateTransition for back of new cards
        ArrayList<RotateTransition> rtNewFace = new ArrayList<>();      // RotateTransition for the new face cards

        Hand prevHand = new Hand(hand);                                         // Keep track of the previous hand
        replaceCards();                                                         // Replace cards in current hand

        for (int i = 0; i < 5; i++) {
            int finalI = i;                                                     // Same as previous finalI
            Card oldFace = prevHand.getCard(i);                                 // Old face card
            ImageView oldBack = oldFace.getBackImage();                         // Old back of card
            Card newFace = hand.getCard(i);                                     // New face card
            ImageView newBack = hand.getCard(i).getBackImage();                 // New back of card

            GridPane.setConstraints(oldBack, i, 0);                         // Set all the constraints
            GridPane.setConstraints(newBack, i, 0);
            GridPane.setConstraints(newFace, i, 0);

            // PathTransitions for the old back and the new back cards
            ptOldBack.add(new PathTransition(Duration.millis(1000),
                    new Line(centerX, centerY, centerX, 300), oldBack));
            ptNewBack.add(new PathTransition(Duration.millis(3000),
                    new Line(centerX, -1000, centerX, centerY), newBack));

            // RotatorTransitions for the old face, old back, new back and new face cards
            rtOldFace.add(createRotator(oldFace, 0, 90, 500));
            rtOldBack.add(createRotator(oldBack, 90, 0, 500));
            rtNewBack.add(createRotator(newBack, 0, 90, 600));
            rtNewFace.add(createRotator(newFace, 90, 0, 500));

            if (!prevHand.getCard(i).checkHold()) {                      // Check for individual card hold

                // Since no old, need to rotate the old back and the new face cards for RotatorTransitions
                oldBack.setRotate(90);
                newFace.setRotate(90);

                /*  Two chained set of animations that run concurrently. In the first set, the old face is on the
                    GridPane. That is rotated 90 degrees, then replaced by the back of that card. The back is then
                    rotated 90 degrees. That initiates the sliding of the back card down to the bottom.

                    The second set of animations is the same as the previous set for a blank screen/initial deal.
                    I probably could have created a method for that, but wanted to group these animations together.
                */
                rtOldFace.get(i).setOnFinished(actionEvent -> {
                    gpCards.getChildren().remove(oldFace);
                    gpCards.getChildren().add(oldBack);
                    rtOldBack.get(finalI).play();
                });
                rtOldBack.get(i).setOnFinished((actionEvent -> ptOldBack.get(finalI).play()));
                ptOldBack.get(i).setOnFinished(actionEvent -> {
                    gpCards.getChildren().remove(oldBack);
                    gpCards.getChildren().add(newBack);
                });

                // Second set of animations
                ptNewBack.get(i).setOnFinished(actionEvent -> rtNewBack.get(finalI).play());
                rtNewBack.get(i).setOnFinished(actionEvent -> {
                    gpCards.getChildren().remove(newBack);
                    gpCards.getChildren().add(newFace);
                    rtNewFace.get(finalI).play();
                });
                rtNewFace.get(i).setOnFinished(actionEvent -> {
                    deselectAllHolds();                                     // Clear any holds on screen
                    clearBets();                                            // Clear bet RadioButtons
                    if (drawClicked) {
                        tfBalance.setText("Balance: $" + chips.getBalance());   // Update fields on screen
                        tfWins.setText("Wins: " + chips.getWins());
                        tfLosses.setText("Losses: " + chips.getLosses());
                        String win = hand.checkForWins();
                        if (!win.equals("")) tfWinType.setText("Win Type: " + win);
                    }
                });
                rtOldFace.get(i).play();
                ptNewBack.get(finalI).play();
            }
        }
    }

    /** Custom RotateTransition for flipping cards 90 degrees */
    private RotateTransition createRotator(Node card, int fromAngle, int toAngle, int duration) {
        RotateTransition rotator = new RotateTransition(Duration.millis(duration), card);
        rotator.setFromAngle(fromAngle);
        rotator.setToAngle(toAngle);
        rotator.setCycleCount(1);
        rotator.setAxis(Rotate.Y_AXIS);
        rotator.setInterpolator(Interpolator.LINEAR);
        return rotator;
    }

    /** Replaces cards in the hand that do not have a hold with new cards from the deck */
    private void replaceCards() {
        for (int i = 0; i < hand.size(); ++i) {
            Card card = hand.getCard(i);
            if (!card.checkHold()) {                                    // Check for a current card hold
                discardedCards.add(card);                               // Add the card to discarded pile
                hand.removeCard(i);                                     // Remove it from the hand
                deck.addCards(discardedCards);                          // Check if deck has less than 5 cards
                hand.addCard(i, deck.deal());                           // Deal a new card and replace it in the hand
            }
        }
    }

    /** Checks for winning hand. Terminal output is for debugging purposes. */
    private void checkWins() {
        String win = hand.checkForWins();
        boolean didWin = !win.equals("");
        System.out.println("Winning hand? " + didWin);
        if (didWin) System.out.println("Hand type: " + hand.checkForWins());
        chips.checkForWin(hand);
        System.out.println("Balance: $" + chips.getBalance());
        System.out.println("Wins: " + chips.getWins() + ", Losses: " + chips.getLosses());
        System.out.println();
    }

    /** Clears the hold buttons */
    private void deselectAllHolds() {
        if (!hand.isEmpty()) hand.removeHolds();
        for (ToggleButton button: btHold) button.setSelected(false);
    }

    /** Checks the hold buttons to determine if any have been pressed */
    private boolean checkForHold() {
        for (ToggleButton button: btHold)
            if (!button.isSelected()) { return false; }
        return true;
    }

    /** Disables the deal and draw buttons */
    private void setDisableDealDraw() {
        btDraw.setDisable(true);
        btDeal.setDisable(true);
    }

    /** Clears the selection from the bet button group */
    private void clearBets() {
        betGroup.getToggles().forEach(toggle -> toggle.setSelected(false));
    }
}
