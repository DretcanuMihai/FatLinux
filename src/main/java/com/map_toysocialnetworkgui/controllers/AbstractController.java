package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.MainApplication;
import com.map_toysocialnetworkgui.service.SuperService;

/**
 * class that describes an abstract view controller
 */
public class AbstractController {
    /**
     * associated super service
     */
    protected SuperService service;

    /**
     * associated application
     */
    protected MainApplication application;

    /**
     * sets associated super service
     *
     * @param service - said super service
     */
    public void setService(SuperService service) {
        this.service = service;
    }

    /**
     * sets associated application
     *
     * @param application - said application
     */
    public void setApplication(MainApplication application) {
        this.application = application;
    }

    /**
     * resets data on the window to default
     */
    public void reset() {}
}
