package com.fatlinux.utils.styling;

import javafx.scene.text.Text;

/**
 * class that describes coloring methods for text
 */
public class TextColoring {
    /**
     * method for coloring a text in white
     *
     * @param text - said text
     */
    public void setTextWhite(Text text) {
        text.setStyle("-fx-fill: white");
    }
}
