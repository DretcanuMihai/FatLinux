package com.map_toysocialnetworkgui.controllers;

import com.map_toysocialnetworkgui.MainApplication;
import com.map_toysocialnetworkgui.service.SuperService;

public class AbstractController {
    protected SuperService service;
    protected MainApplication application;

    public void setService(SuperService service) {
        this.service = service;
    }

    public void setApplication(MainApplication application) {
        this.application = application;
    }
}
