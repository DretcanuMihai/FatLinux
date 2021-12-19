package com.map_toysocialnetworkgui.model.entities;

/**
 * enum describing the status of a user's account
 */
public enum AccountStatus {
    ACTIVE(0),
    DISABLED(1);
    /**
     * a unique code that describes the status
     */
    final int statusCode;

    /**
     * constructs an AccountStatus with a given status code
     *
     * @param statusCode - said status code
     */
    AccountStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * returns the account status represented by a code
     *
     * @param code - said code
     * @return the account status
     */
    public static AccountStatus getByCode(int code) {
        for (AccountStatus status : values()) {
            if (status.statusCode == code) {
                return status;
            }
        }
        return null;
    }
}
