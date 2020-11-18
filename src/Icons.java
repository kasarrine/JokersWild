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

public class Icons {

    private final ComboBox<String> iconBox;

    public Icons() {
        String icon1 = "cards/icon1.png";
        String icon2 = "cards/icon2.png";
        String icon3 = "cards/icon3.png";
        String icon4 = "cards/icon4.png";
        String icon5 = "cards/icon5.png";
        String icon6 = "cards/icon6.png";
        ObservableList<String> iconList = FXCollections.observableArrayList(icon1,
                icon2, icon3, icon4, icon5, icon6);
        iconBox = new ComboBox<>(iconList);
        iconBox.setCellFactory(param -> new IconListCell());
        iconBox.setButtonCell(new IconListCell());
    }

    static class IconListCell extends ListCell<String> {

        @Override
        protected void updateItem(String icon, boolean empty) {
            super.updateItem(icon, empty);
            setText(icon);
            if(empty || icon == null) {
                setGraphic(null);
            } else {
                Image iconImage = new Image(icon);
                ImageView iconImageView = new ImageView(iconImage);
                iconImageView.setFitHeight(70);
                iconImageView.setFitWidth(60);
                setGraphic(iconImageView);
            }
            setText("");
        }
    }

    public ComboBox<String> getIconBox() {
        return iconBox;
    }
}
