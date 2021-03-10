/*
 * (c) Copyright 2021 sothawo
 */
package com.sothawo.mapjfx.dragmarkers;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.MapView;
import com.sothawo.mapjfx.Marker;
import com.sothawo.mapjfx.event.MapViewEvent;
import com.sothawo.mapjfx.event.MarkerEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author P.J. Meisch (pj.meisch@sothawo.com)
 */
public class MapPaneDragOriginal extends MapPane {
    private static final Logger LOGGER = LoggerFactory.getLogger(MapPaneDragOriginal.class);

    private Coordinate dragStart;
    private Marker draggedMarker;

    protected void setupEventHandlers(MapView mapView) {
        mapView.addEventHandler(MarkerEvent.MARKER_DOUBLECLICKED, event -> {
            event.consume();
            var marker = event.getMarker();
            if (draggedMarker == null) {
                LOGGER.info("doubleclick on marker, start drag: " + marker);
                draggedMarker = marker;
                dragStart = marker.getPosition();
            }
        });

        mapView.addEventHandler(MapViewEvent.MAP_POINTER_MOVED, event -> {
            event.consume();
            if (draggedMarker != null) {
                draggedMarker.setPosition(event.getCoordinate().normalize());
            }
        });

        mapView.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            event.consume();
            if (event.getCode() == KeyCode.ESCAPE) {
                if (draggedMarker != null) {
                    LOGGER.info("escape, aborting drag");
                    animateMarker(draggedMarker, draggedMarker.getPosition(), dragStart);
                    draggedMarker = null;
                }
            }
        });

        mapView.addEventHandler(MapViewEvent.MAP_RIGHTCLICKED, event -> {
            event.consume();
            if (draggedMarker != null) {
                LOGGER.info("right click detected, finishing drag");
                draggedMarker = null;
            }
        });
    }

}
