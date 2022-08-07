package com.onlyabhinav.cowinslots.utils;

import java.io.IOException;

public class WindowsNotification {


    public static void notifyWithMessage(String message){
        try {
            Runtime.getRuntime().exec(String.format("terminal-notifier -message Vaccine_Available"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
