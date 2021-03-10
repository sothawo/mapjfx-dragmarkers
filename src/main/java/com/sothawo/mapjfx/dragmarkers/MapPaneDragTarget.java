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

import java.util.List;

/**
 * @author P.J. Meisch (pj.meisch@sothawo.com)
 */
public class MapPaneDragTarget extends MapPane {
    private static final Logger LOGGER = LoggerFactory.getLogger(MapPaneDragTarget.class);

    private Coordinate pointerPosition;
    private Marker targetMarker;
    private Marker startMarker;
    private Marker draggedMarker;

    @Override
    protected List<Marker> setupMarkers() {
        var markers = super.setupMarkers();
        targetMarker = new Marker(getClass().getResource("/target.png" ), -32, -32)
            .setPosition(new Coordinate(0.0, 0.0)).setVisible(false);
        markers.add(targetMarker);
        return markers;
    }

    protected void setupEventHandlers(MapView mapView) {
        mapView.addEventHandler(MarkerEvent.MARKER_DOUBLECLICKED, event -> {
            event.consume();
            var marker = event.getMarker();
            if (draggedMarker == null) {
                LOGGER.info("doubleclick on marker, start drag: " + marker);
                startMarker = marker;
                draggedMarker = targetMarker;
                draggedMarker.setPosition(pointerPosition);
                targetMarker.setVisible(true);
            }
        });

        mapView.addEventHandler(MapViewEvent.MAP_POINTER_MOVED, event -> {
            event.consume();
            pointerPosition = event.getCoordinate().normalize();
            if (draggedMarker != null) {
                draggedMarker.setPosition(pointerPosition);
            }
        });

        mapView.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                if (draggedMarker != null) {
                    LOGGER.info("escape, aborting drag");
                    targetMarker.setVisible(false);
                    draggedMarker = null;
                }
            }
        });

        mapView.addEventHandler(MarkerEvent.MARKER_CLICKED, event -> {
            event.consume();
            if (draggedMarker != null && event.getMarker() == draggedMarker) {
                LOGGER.info("drag marker click detected, finishing drag");
                targetMarker.setVisible(false);
                draggedMarker = null;
                animateMarker(startMarker, startMarker.getPosition(), event.getMarker().getPosition().normalize());
            }
        });

        mapView.addEventHandler(MapViewEvent.MAP_RIGHTCLICKED, event -> {
            event.consume();
            if (draggedMarker != null) {
                LOGGER.info("right click detected, finishing drag");
                targetMarker.setVisible(false);
                draggedMarker = null;
                animateMarker(startMarker, startMarker.getPosition(), event.getCoordinate().normalize());
            }
        });
    }

}
