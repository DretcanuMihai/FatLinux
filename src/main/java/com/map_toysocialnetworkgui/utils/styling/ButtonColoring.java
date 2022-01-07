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
}
