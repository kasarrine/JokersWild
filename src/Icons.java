/*
Team: Meta Heads
Members: Richard Aviles, Kirk Sarrine, Garrett West
Course: COSC 1174 - Fall 2020
Project: Jokers Wild!
Due: 11/22/2020
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/** This class helps control the player icon combobox */
public class Icons {

    private final ComboBox<String> iconBox;

    /** This class helps control the player icon combobox */
    public Icons() {
        String icon1 = "file:images/icons/icon1.png";
        String icon2 = "file:images/icons/icon2.png";
        String icon3 = "file:images/icons/icon3.png";
        String icon4 = "file:images/icons/icon4.png";
        String icon5 = "file:images/icons/icon5.png";
        String icon6 = "file:images/icons/icon6.png";
        ObservableList<String> iconList = FXCollections.observableArrayList(icon1,
                icon2, icon3, icon4, icon5, icon6);
        iconBox = new ComboBox<>(iconList);
        iconBox.setCellFactory(param -> new IconListCell());
        iconBox.setButtonCell(new IconListCell());              // combobox image update
    }

    /** Updates combobox cell/list content */
    static class IconListCell extends ListCell<String> {

        /*
            When player selects and avatar icon, the content is taken and moved
            to a specified position, so the list view becomes blank.
            To prevent that, this method updates the combobox to maintain the content inside it.
         */
        
        @Override
        protected void updateItem(String icon, boolean empty) {     // override updateItem for specified customization
            super.updateItem(icon, empty);
            setText(icon);
            if(empty || icon == null) {                             
                setGraphic(null);                                   // clear image
            } else {
                Image iconImage = new Image(icon);                  // create new image of selected item
                ImageView iconImageView = new ImageView(iconImage); 
                iconImageView.setFitHeight(70); 
                iconImageView.setFitWidth(60);
                setGraphic(iconImageView);                          // set the image
            }   
            setText("");
        }
    }

    public ComboBox<String> getIconBox() {
        return iconBox;
    }
}
