/*
Name: Kirk Sarrine
LUID: L20178451
Course: COSC 1174 - Fall 2020
Date: 10/18/2020
Assignment: HW6 - Poker #3
 */


import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;


public class DisplayTest extends Application {

    Deck deck = new Deck();
    Hand hand = new Hand();
    ArrayList<Card> discardedCards = new ArrayList<>();
    double centerX, centerY = 0.0;
    AtomicInteger dealCount = new AtomicInteger(0);

    HBox hBoxBTs = new HBox();

    Node btDraw = new Button("Draw");                                // Create buttons for draw and deal
    Node btDeal = new Button("Deal");

    GridPane gpCards = new GridPane();
    BorderPane borderPane = new BorderPane();
    ParallelTransition ptNewSlideDown = new ParallelTransition();
    ParallelTransition ptNewFlipBack = new ParallelTransition();
    ParallelTransition ptNewFlipFace = new ParallelTransition();

    ParallelTransition ptOldFlipFace = new ParallelTransition();
    ParallelTransition ptOldFlipBack = new ParallelTransition();
    ParallelTransition ptOldSlideDown = new ParallelTransition();

    ArrayList<Node> oldFaces = new ArrayList<>();
    ArrayList<Node> oldBacks = new ArrayList<>();
    ArrayList<Node> newBacks = new ArrayList<>();
    ArrayList<Node> newFaces = new ArrayList<>();


    @Override
    public void start(Stage primaryStage) {
        deck.shuffle();                                                 // Initial shuffle of the new deck
        primaryStage.setResizable(false);                               // Set the window to not be resizable
        primaryStage.setWidth(800);
        primaryStage.setHeight(400);                                       // Set the window to not be resizable
        gpCards.setAlignment(Pos.CENTER);                               // Set preferences and display size
        gpCards.setHgap(10);
        gpCards.setVgap(10);

        btDeal.setOnMouseClicked(actionEvent -> {
            if (dealCount.get() < 1) {
                dealCount.getAndIncrement();
                deal(false);
            }
            else draw(false); });

        btDraw.setOnMouseClicked(actionEvent -> draw(true));

        hBoxBTs.setSpacing(36);
        hBoxBTs.setStyle("-fx-border-color: grey");
        hBoxBTs.setAlignment(Pos.TOP_CENTER);
        hBoxBTs.getChildren().addAll(btDeal, btDraw);

        borderPane.setCenter(gpCards);
        borderPane.setBottom(hBoxBTs);

        Scene scene = new Scene(borderPane);                            // Create a scene and place it in the stage
        primaryStage.setTitle("DisplayTest");                              // Set the stage title
        primaryStage.setScene(scene);                                   // Place the scene in the stage

        primaryStage.show();                                            // Display the stage
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

    private void deal(Boolean startDraw){
        for (int i = 0; i < 5; i++) {
            Card card = deck.deal();
            hand.addCard(card);
            // card.setVisible(false);
            Node backCard = card.getBackImage();
            centerX = backCard.getBoundsInParent().getCenterX();     // Used for centering the nodes
            centerY = backCard.getBoundsInParent().getCenterY();
            GridPane.setConstraints(backCard, i, 0);
            GridPane.setConstraints(card, i, 0);
            ptNewSlideDown.getChildren().addAll(new PathTransition(Duration.millis(1500),
                    new Line(centerX, -500, centerX, centerY) ,backCard));
            ptNewFlipBack.getChildren().addAll(createRotator(backCard, 0, 90, 500));
            ptNewFlipFace.getChildren().addAll(createRotator(card, 90, 0, 500));
            newFaces.add(card);
            gpCards.getChildren().addAll(backCard);
        }
        ptNewSlideDown.setOnFinished(actionEvent -> {
            ptNewFlipBack.play();
        });
        ptNewFlipBack.setOnFinished(actionEvent -> {
            addFaceCards();
            ptNewFlipFace.play();
        });
        ptNewFlipFace.setOnFinished(actionEvent -> {
            removeBackCards();
            if (startDraw) {
                draw(false);
            }
            clearALNodes();
            System.out.println(gpCards.getChildren());
        });
        ptNewSlideDown.play();
    }

    private void draw(boolean startDeal) {
        clearParallelTransitions();

        Hand prevHand = new Hand(hand);
        replaceCards();

        for (int i = 0; i < 5; i++) {
            Card oldFace = prevHand.getCard(i);                                 // Old face card
            Node oldBack = oldFace.getBackImage();                              // Old back of card
            Card newFace = hand.getCard(i);                                     // New face card
            Node newBack = hand.getCard(i).getBackImage();                      // New back of card

            oldFaces.add(oldFace);
            oldBacks.add(oldBack);
            newBacks.add(newBack);
            newFaces.add(newFace);

            GridPane.setConstraints(oldBack, i, 0);                         // Set all the constraints
            GridPane.setConstraints(newBack, i, 0);
            GridPane.setConstraints(newFace, i, 0);

            if (!prevHand.getCard(i).checkHold()) {
                ptNewSlideDown.getChildren().add(new PathTransition(Duration.millis(3000),
                        new Line(centerX, -1000, centerX, centerY), newBack));
                ptOldSlideDown.getChildren().add(new PathTransition(Duration.millis(1000),
                        new Line(centerX, centerY, centerX, 300), oldBack));

                ptOldFlipFace.getChildren().add(createRotator(oldFace, 0, 90, 500));
                ptOldFlipBack.getChildren().add(createRotator(oldBack, 90, 0, 500));
                ptNewFlipBack.getChildren().add(createRotator(newBack, 0, 90, 600));
                ptNewFlipFace.getChildren().add(createRotator(newFace, 90, 0, 500));
            }

            ptOldFlipFace.setOnFinished(actionEvent -> {
                removeFaceCards();
                gpCards.getChildren().addAll(oldBacks);
                ptOldFlipBack.play();
            });

            ptOldFlipBack.setOnFinished((actionEvent -> ptOldSlideDown.play()));
            ptOldSlideDown.setOnFinished(actionEvent -> {
                removeBackCards();
                System.out.println(gpCards.getChildren());
                clearALNodes();
                if (startDeal) draw(false);
            });
            ptOldFlipFace.play();
        }
    }

    private void removeBackCards(){ gpCards.getChildren().removeIf(node -> !(node instanceof Card)); }
    private void removeFaceCards(){ gpCards.getChildren().removeIf(node -> node instanceof Card); }

    private void addFaceCards(){ gpCards.getChildren().addAll(newFaces); }

    private void clearALNodes() {
        oldBacks.clear();
        oldFaces.clear();
        newBacks.clear();
        newFaces.clear();
    }
    private void setVisible(){
        for (Node node: gpCards.getChildren())
            node.setVisible(true);
    }

    private void clearParallelTransitions(){
        ptNewSlideDown.getChildren().clear();
        ptNewFlipBack.getChildren().clear();
        ptNewFlipFace.getChildren().clear();
        ptOldFlipFace.getChildren().clear();
        ptOldFlipBack.getChildren().clear();
        ptOldSlideDown.getChildren().clear();
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
}

