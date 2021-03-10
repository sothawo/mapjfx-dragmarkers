/*
 * (c) Copyright 2021 sothawo
 */
package com.sothawo.mapjfx.dragmarkers;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author P.J. Meisch (pj.meisch@sothawo.com)
 */
public class TestApp extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestApp.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        LOGGER.info("starting devtest program...");

        var splitPane = new SplitPane();
        var leftPane = new MapPaneDragOriginal();
        var rightPane = new MapPaneDragTarget();

        splitPane.getItems().add(leftPane);
        splitPane.getItems().add(rightPane);

        var scene = new Scene(splitPane, 1400, 800);
        primaryStage.setTitle("mapjfx marker drag demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
