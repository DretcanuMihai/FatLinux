package com.map_toysocialnetworkgui.utils.styling;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * class that describes coloring methods for buttons
 */
public class ButtonStyling {
    /**
     * colors a button in orange and adds hover effect
     *
     * @param button - said button
     */
    public void setButtonOrange(Button button) {
        button.setStyle("""
                -fx-focus-traversable: false;
                -fx-background-radius: 10px;
                -fx-background-color: #ff7700;
                """);
        button.setOnMouseEntered(event -> button.setStyle("""
                -fx-focus-traversable: false;
                -fx-background-radius: 10px;
                -fx-background-color: #F04A00;
                """));
        button.setOnMouseExited(event -> button.setStyle("""
                -fx-focus-traversable: false;
                -fx-background-radius: 10px;
                -fx-background-color: #ff7700;
                """));
    }

    /**
     * colors a button in black and adds hover effect
     *
     * @param button - said button
     */
    public void setButtonBlack(Button button) {
        button.setStyle("""
                -fx-focus-traversable: false;
                -fx-background-radius: 10px;
                -fx-background-color: #000000;
                """);
        button.setOnMouseEntered(event -> button.setStyle("""
                -fx-focus-traversable: false;
                -fx-background-radius: 10px;
                -fx-background-color: #424043;
                """));
        button.setOnMouseExited(event -> button.setStyle("""
                -fx-focus-traversable: false;
                -fx-background-radius: 10px;
                -fx-background-color: #000000;
                """));
    }

    /**
     * colors a button in black and adds hover effect with a lighter black
     *
     * @param button - said button
     */
    public void setButtonBlackWithLighterHover(Button button) {
        button.setStyle("""
                -fx-focus-traversable: false;
                -fx-background-radius: 10px;
                -fx-background-color: #000000;
                """);
        button.setOnMouseEntered(event -> button.setStyle("""
                -fx-focus-traversable: false;
                -fx-background-radius: 10px;
                -fx-background-color: #1e1e1e;
                """));
        button.setOnMouseExited(event -> button.setStyle("""
                -fx-focus-traversable: false;
                -fx-background-radius: 10px;
                -fx-background-color: #000000;
                """));
    }

    /**
     * colors a button in orange with square black border and adds hover effect
     *
     * @param button - said button
     */
    public void setButtonOrangeSquareBorder(Button button) {
        button.setStyle("""
                -fx-focus-traversable: false;
                -fx-background-radius: 0px;
                -fx-background-color: #ff7700;
                -fx-border-color: black;
                -fx-background-insets: 0 0 0 0;
                """);
        button.setOnMouseEntered(event -> button.setStyle("""
                -fx-focus-traversable: false;
                -fx-background-radius: 0px;
                -fx-background-color: #F04A00;
                -fx-border-color: black;
                -fx-background-insets: 0 0 0 0;
                """));
        button.setOnMouseExited(event -> button.setStyle("""
                -fx-focus-traversable: false;
                -fx-background-radius: 0px;
                -fx-background-color: #ff7700;
                -fx-border-color: black;
                -fx-background-insets: 0 0 0 0;
                """));
    }

    /**
     * colors search button in black and assigns a corresponding image to it
     *
     * @param button - said button
     */
    public void setButtonForSearchEvent(Button button) {
        button.setStyle("""
                -fx-background-radius: 0 20 20 0;
                -fx-background-color: black;
                -fx-background-image: url("/com/map_toysocialnetworkgui/images/searchIcon.png");
                -fx-background-size: 20;
                -fx-background-repeat: no-repeat;
                -fx-background-position: center;
                """);
        button.setOnMouseEntered(event -> button.setStyle("""
                -fx-background-radius: 0 20 20 0;
                -fx-background-color: #1e1e1e;
                -fx-background-image: url("/com/map_toysocialnetworkgui/images/searchIcon.png");
                -fx-background-size: 20;
                -fx-background-repeat: no-repeat;
                -fx-background-position: center;
                """));
        button.setOnMouseExited(event -> button.setStyle("""
                -fx-background-radius: 0 20 20 0;
                -fx-background-color: black;
                -fx-background-image: url("/com/map_toysocialnetworkgui/images/searchIcon.png");
                -fx-background-size: 20;
                -fx-background-repeat: no-repeat;
                -fx-background-position: center;
                """));
    }

    /**
     * colors cancel search button in black and assigns a corresponding image to it
     *
     * @param button - said button
     */
    public void setButtonForCancelSearchEvent(Button button) {
        button.setStyle("""
                -fx-background-radius: 0 20 20 0;
                -fx-background-color: black;
                -fx-background-image: url("/com/map_toysocialnetworkgui/images/cancelSearchIcon.png");
                -fx-background-size: 20;
                -fx-background-repeat: no-repeat;
                -fx-background-position: center;
                """);
        button.setOnMouseEntered(event -> button.setStyle("""
                -fx-background-radius: 0 20 20 0;
                -fx-background-color: #1e1e1e;
                -fx-background-image: url("/com/map_toysocialnetworkgui/images/cancelSearchIcon.png");
                -fx-background-size: 20;
                -fx-background-repeat: no-repeat;
                -fx-background-position: center;
                """));
        button.setOnMouseExited(event -> button.setStyle("""
                -fx-background-radius: 0 20 20 0;
                -fx-background-color: black;
                -fx-background-image: url("/com/map_toysocialnetworkgui/images/cancelSearchIcon.png");
                -fx-background-size: 20;
                -fx-background-repeat: no-repeat;
                -fx-background-position: center;
                """));
    }

    /**
     * styles a JFX menu button so that it can show a notification bubble with the number of new notifications
     *
     * @param button                - said button
     * @param buttonText            - the text on the button
     * @param buttonImage           - the image on the button
     * @param numberOfNotifications - the number of notifications
     * @param visibleBubble         - if the bubble should be visible
     */
    public void setJFXButtonWithNotificationBubble(JFXButton button, String buttonText, ImageView buttonImage, String numberOfNotifications, boolean visibleBubble) {
        ImageView notificationBubble = new ImageView("com/map_toysocialnetworkgui/images/notificationRedBubbleIcon.png");
        Label label = new Label(buttonText);
        Label nrOfNotifications = new Label(numberOfNotifications);
        HBox buttonElements = new HBox();
        StackPane imageAndNrOfNotifications = new StackPane();

        notificationBubble.setVisible(visibleBubble);
        buttonImage.setFitWidth(30.0);
        buttonImage.setFitHeight(30.0);
        notificationBubble.setFitWidth(20.0);
        notificationBubble.setFitHeight(20.0);
        label.setStyle("-fx-font-weight: bold; -fx-text-fill: #ff7700; -fx-font-size: 15;");
        nrOfNotifications.setStyle("-fx-font-weight: bold; -fx-font-size: 10; -fx-alignment: center;");

        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        imageAndNrOfNotifications.getChildren().addAll(notificationBubble, nrOfNotifications);
        imageAndNrOfNotifications.setAlignment(Pos.CENTER);
        buttonElements.getChildren().addAll(buttonImage, label, imageAndNrOfNotifications);
        buttonElements.setAlignment(Pos.CENTER_LEFT);
        buttonElements.setSpacing(5);
        button.setRipplerFill(Color.valueOf("#ff7700"));
        button.setGraphic(buttonElements);
    }
}
