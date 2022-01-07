package com.map_toysocialnetworkgui.utils.structures;

import javafx.scene.control.FocusModel;

/**
 * class that describes a custom, non-focusable model
 *
 * @param <T> - type of object
 */
public class NoFocusModel<T> extends FocusModel<T> {
    @Override
    protected int getItemCount() {
        return 0;
    }

    @Override
    protected T getModelItem(int index) {
        return null;
    }
}
