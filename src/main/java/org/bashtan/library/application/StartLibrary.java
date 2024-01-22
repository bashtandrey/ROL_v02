package org.bashtan.library.application;


import org.bashtan.library.controller.StartApplication;

public class StartLibrary {
    public static RunLibrary runLibrary ;

    public static void main(String[] args) {
        runLibrary = new RunLibrary();
        StartApplication.main(args);
    }
}
