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
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;


public class Poker extends Application {
    Deck deck = new Deck();                                             // New deck of cards
    Hand hand = new Hand();                                             // New hand of cards
    Chips chips = new Chips();                                          // New instance of chips
    ArrayList<Card> discardedCards = new ArrayList<>();                 // Used to store discarded cards
    AtomicInteger dealCount = new AtomicInteger(0);            // AtomicInteger to count deal button clicks
    AtomicInteger holdCount = new AtomicInteger(0);            // AtomicInteger to count total cards held

    double centerX, centerY = 0.0;                                      // Used to get the center (x, y) on boarderPane
    HBox hBoxBTs = new HBox();                                          // Buttons are placed here
    VBox vBoxBets = new VBox();
    VBox vBoxStats = new VBox();
    GridPane gpCards = new GridPane();                                  // Cards are placed here
    BorderPane borderPane = new BorderPane();                           // Buttons and cards contained by BP
    Node btDraw = new Button("Draw");                              // Create buttons for draw and deal
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

        primaryStage.setMinWidth(800);                                                  // Minimum window width
        primaryStage.setMinHeight(400);                                                 // Minimum window height
        // Maximum size set to users monitor specifications
        primaryStage.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth());     // Maximum window width
        primaryStage.setMaxHeight(Screen.getPrimary().getVisualBounds().getHeight());   // Maximum window height

        borderPane.setStyle("-fx-background-color: green; -fx-border-color: black");    // Color?

        gpCards.setAlignment(Pos.CENTER);                               // Set preferences and display size
        gpCards.setHgap(10);
        gpCards.setVgap(10);
        gpCards.setTranslateY(15);

        hBoxBTs.setSpacing(36);
        hBoxBTs.setAlignment(Pos.TOP_CENTER);
        hBoxBTs.setTranslateY(-40);                                     // Relocate deal & draw button

        setFieldsNotInteractive(tfBalance);                             // Make trackers non-clickable
        setFieldsNotInteractive(tfWins);                                // ^^
        setFieldsNotInteractive(tfLosses);                              // ^^
        setFieldsNotInteractive(tfWinType);                             // ^^

        setDisableDealDraw();                                           // Initially disable bet and deal buttons

        btDeal.setOnMouseClicked(actionEvent -> {                       // Set Action for deal button
            drawClicked = false;
            chips.applyBetToBalance();
            tfBalance.setText("Balance: $" + String.format("%,d", chips.getBalance())); // Update to the current balance
            tfWinType.setText("Win Type: ");
            setDisableDealDraw();                                       // Disable bet and deal after click
            if (dealCount.get() < 1) {                                  // Only on first deal
                deselectAllHolds();                                     // Clear card holds if there are any
                dealFirstCards();                                       // Start animations
                dealCount.getAndIncrement();
            }
            else {                                                      // Condition for after first deal
                drawCards();
            }
            // -Aviles-
            clickableCardHold(hand);                                    // Allow user to hold desired cards
        });

        btDraw.setOnMouseClicked(actionEvent -> {                       // Set action for draw button

            // !checkForHold(hand) == cards not clickable after the player draws new cards
            if (dealCount.get() > 0 && !checkForHold(hand)) {
                btDrawListener();                                       // Draw button actions to perform
                drawCards();                                            // Draw cards if user discards
                checkWins();                                            // Check for winning hand
            } else if(holdCount.get() == 5) {
                btDrawListener();                                       // Draw button actions to perform
                checkWins();                                            // Check for winning hand
                deselectAllHolds();                                     // Allows new hand to be dealt
                updateFields();                                         // Update fields
            }
        });

        hBoxBTs.getChildren().add(btDeal);                              // Deal button added first to hBox

        // Set the user data associated with each bet button and add them to the betGroup
        btBet1.setUserData(1);
        btBet10.setUserData(10);
        btBet100.setUserData(100);
        btBet1.setToggleGroup(betGroup);
        btBet10.setToggleGroup(betGroup);
        btBet100.setToggleGroup(betGroup);

        betButtonStyle(btBet1);
        betButtonStyle(btBet10);
        betButtonStyle(btBet100);

        // Get bet from radio button and set the bet in chips
        betGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            if (betGroup.getSelectedToggle() != null) {
                btDeal.setDisable(false);
                int bet = (int) betGroup.getSelectedToggle().getUserData();
                chips.setBet(bet);
            }
        });

        Icons playerIcons = new Icons();
        ComboBox<String> icons = playerIcons.getIconBox();
        icons.setVisibleRowCount(1);
        icons.setMaxWidth(50);
        icons.setStyle("-fx-background-color: transparent");
        icons.getSelectionModel().selectFirst();

        hBoxBTs.getChildren().add(btDraw);                              // Add draw button as the last
        vBoxStats.getChildren().addAll(tfBalance, tfWins, tfWinType, tfLosses);
        vBoxBets.getChildren().addAll(icons, btBet1, btBet10, btBet100);
        vBoxBets.getChildren().get(0).setTranslateX(vBoxBets.getWidth());
        vBoxBets.setSpacing(5);
        vBoxBets.setTranslateY(55); vBoxBets.setTranslateX(50);

        icons.setTranslateY(-55);

        // Set Stats and Bets to the same size for spacing purposes
        vBoxStats.setPrefWidth(150);
        vBoxBets.setPrefWidth(150);
        gpCards.setPrefSize(400, 271);

        borderPane.setCenter(gpCards);                                  // Add hBoxCards to center of borderPane
        borderPane.setBottom(hBoxBTs);                                  // Add hBoxBTs to bottom of borderPane
        borderPane.setLeft(vBoxStats);                                  // Add vBoxStats to left of borderPane
        borderPane.setRight(vBoxBets);                                  // Add vBoxBets to right of borderPane


         /*
            GROUP & SCENE CREATION, SETTING THE STAGE & WINDOW RESIZING
         */
        Group group = new Group(borderPane);                            // Grouping all children of borderPane
        group.setAutoSizeChildren(true);                                // Setting nodes to auto resize

        double initWidth = 750;                                         // Initial width for borderPane
        double initHeight = 350;                                        // Initial height for borderPane

        // Make scene borderPane & children scalable
        if(borderPane.getPrefWidth() == Region.USE_COMPUTED_SIZE) {
            borderPane.setPrefWidth(initWidth);
        } else {
            initWidth = borderPane.getPrefWidth();
        }
        if(borderPane.getPrefHeight() == Region.USE_COMPUTED_SIZE) {
            borderPane.setPrefHeight(initHeight);
        } else {
            initHeight = borderPane.getPrefHeight();
        }

        Scene scene = new Scene(group, initWidth, initHeight);          // Create a scene and place it in the stage
        primaryStage.setTitle("Jokers Wild!");                          // Set the stage title
        primaryStage.setScene(scene);                                   // Place the scene in the stage
        primaryStage.show();                                            // Display the stage

        // When user changes window size, borderPane & its children will scale to players window size
        Scale scale = new Scale();
        scale.xProperty().bind(scene.widthProperty().divide(initWidth));        // scales borderPane width
        scale.yProperty().bind(scene.heightProperty().divide(initHeight));      // scales borderPane height
        borderPane.getTransforms().addAll(scale);                               // add the scaling to borderPane

    }

    /** Prevents fields from being interacted with. Rename? Also sets style of the fields */
    public void setFieldsNotInteractive(TextField tracker) {
        tracker.setFocusTraversable(false);
        tracker.setEditable(false);
        tracker.setMouseTransparent(true);
        tracker.setStyle("-fx-font-size: 12; -fx-font-weight:  bold");
    }

    /** Style for bet buttons */
    public void betButtonStyle(RadioButton betButton) {
        betButton.setFocusTraversable(false);                                  // Removes blue-highlight (delete?)
        betButton.setStyle("-fx-font-size: 15; -fx-font-weight: bold");
    }

    /** Takes current hand, targets index of card clicked, and lowers card position to show that it's held */
    public void clickableCardHold(Hand hand) {
        if(hand.size() == 5) {                                                 // Only works if player has a hand
            for(int i = 0; i < hand.size(); i++) {                             // Loop all cards
                int finalI = i;                                                // Track card index
                hand.getCard(finalI).setOnMouseClicked(cardHoldAction -> {     // Hold action event for each card
                    if(!hand.getCard(finalI).checkHold() && !drawClicked) {    // Condition for being clickable
                        hand.getCard(finalI).hold();                           // Hold the card
                        hand.getCard(finalI).setTranslateY(15);                // Shift card down as visual cue of hold
                        btDraw.setDisable(false);                              // Holding cards allows you to draw new
                        holdCount.getAndIncrement();
                    } else {
                        hand.getCard(finalI).removeHold();                     // Allow player to remove the hold
                        hand.getCard(finalI).setTranslateY(0);                 // Shift the card back up
                        holdCount.getAndDecrement();
                    }
                });
            }
        }
    }

    /** Returns held cards to normal position once draw button is clicked */
    public void returnHeldCards() {
        for(int i= 0; i < 5; i++) {
            if(hand.getCard(i).checkHold()) {
                hand.getCard(i).setTranslateY(0);
            }
        }
        btDraw.setDisable(true);
    }

    /** Draw button function placed in method to address user holding no cards, or all cards */
    public void btDrawListener() {
        drawClicked = true;
        returnHeldCards();                                              // Place cards held in correct position
        setDisableDealDraw();                                           // Disable bet and deal buttons
        clearBets();                                                    // Clear bet RadioButtons
        holdCount.set(0);                                               // Reset hold count
    }

    /** Called in drawCards() method, and in draw button action if user holds all cards */
    public void updateFields() {
        tfBalance.setText("Balance: $" + String.format("%,d", chips.getBalance()));        // Update fields on screen
        tfWins.setText("Wins: " + chips.getWins());
        tfLosses.setText("Losses: " + chips.getLosses());
        tfWinType.setText("Win Type: " + hand.checkForWins());
        btDraw.setDisable(true);
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

                GridPane.setConstraints(newBack, i, 0);         // Set constraints for new face and back images
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
        } else {
            drawCards();
        }
        btDraw.setDisable(true);
    }

    /** Draws cards and also used to deal cards after the first initial deal */
    public void drawCards() {
        ArrayList<PathTransition> ptOldBack = new ArrayList<>();        // PathTransitions for back of old cards
        ArrayList<PathTransition> ptNewBack = new ArrayList<>();        // PathTransitions for back of new cards
        ArrayList<RotateTransition> rtOldFace = new ArrayList<>();      // RotateTransition for old face cards
        ArrayList<RotateTransition> rtOldBack = new ArrayList<>();      // RotateTransition for back of old cards
        ArrayList<RotateTransition> rtNewBack = new ArrayList<>();      // RotateTransition for back of new cards
        ArrayList<RotateTransition> rtNewFace = new ArrayList<>();      // RotateTransition for the new face cards

        Hand prevHand = new Hand(hand);                                 // Keep track of the previous hand
        replaceCards();                                                 // Replace cards in current hand

        for (int i = 0; i < 5; i++) {
            int finalI = i;                                             // Same as previous finalI
            Card oldFace = prevHand.getCard(i);                         // Old face card
            ImageView oldBack = oldFace.getBackImage();                 // Old back of card
            Card newFace = hand.getCard(i);                             // New face card
            ImageView newBack = hand.getCard(i).getBackImage();         // New back of card

            GridPane.setConstraints(oldBack, i, 0);             // Set all the constraints
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

                /*  Two chained set of animations that run concurrently. In the first set, the old face is on thew
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
                    deselectAllHolds();                                    // Clear any holds on screen
                    clearBets();                                           // Clear bet RadioButtons
                    if(drawClicked) {
                        updateFields();
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

    /** Clears the hold on cards held in hand */
    private void deselectAllHolds() {
        if (!hand.isEmpty()) hand.removeHolds();
    }

    /** Checks the hold buttons to determine if any have been pressed */
    private boolean checkForHold(Hand hand) {

        for (Card card : hand.getCards()) {
            if (!card.checkHold()) {
                return false;
            }
        }
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