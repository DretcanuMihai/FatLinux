package com.map_toysocialnetworkgui.utils.styling;

import javafx.scene.control.Button;

/**
 * class that describes coloring methods for buttons
 */
public class ButtonColoring {
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
}
