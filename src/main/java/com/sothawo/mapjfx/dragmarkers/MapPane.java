/*
 * (c) Copyright 2021 sothawo
 */
package com.sothawo.mapjfx.dragmarkers;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.MapLabel;
import com.sothawo.mapjfx.MapType;
import com.sothawo.mapjfx.MapView;
import com.sothawo.mapjfx.Marker;
import javafx.animation.Transition;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author P.J. Meisch (pj.meisch@sothawo.com)
 */
public abstract class MapPane extends BorderPane {

    private final Coordinate centre = new Coordinate(48.87095771574132, 2.331742908177377);
    private final Coordinate tourEiffel = new Coordinate(48.85834016654485, 2.2947383673494053);
    private final Coordinate notreDame = new Coordinate(48.8525733574212, 2.350482007989976);
    private final Coordinate montMartre = new Coordinate(48.88690042485116, 2.3428150160356993);

    private final List<Marker> markers = new ArrayList<>();


    public MapPane() {
        var mapView = setupMapView();
        setCenter(mapView);
        markers.addAll(setupMarkers());
    }

    private MapView setupMapView() {
        final MapView mapView;
        mapView = new MapView();
        mapView.setAnimationDuration(500);
        mapView.setMapType(MapType.OSM);

        setupEventHandlers(mapView);

        mapView.initializedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                markers.forEach(mapView::addMarker);
                mapView.setCenter(centre);
                mapView.setZoom(13.0);
            }
        });
        mapView.initialize();
        return mapView;
    }

    abstract protected void setupEventHandlers(MapView mapView);

    protected List<Marker> setupMarkers() {
        List<Marker> markers = new ArrayList<>();
        var markerTourEiffel = Marker.createProvided(Marker.Provided.GREEN)
            .setPosition(tourEiffel).setVisible(true)
            .attachLabel(new MapLabel("visit day 1").setPosition(tourEiffel).setVisible(true));
        markers.add(markerTourEiffel);
        var markerNotreDame = Marker.createProvided(Marker.Provided.GREEN)
            .setPosition(notreDame).setVisible(true)
            .attachLabel(new MapLabel("visit day 2").setPosition(notreDame).setVisible(true));
        markers.add(markerNotreDame);
        var markerMontMartre = Marker.createProvided(Marker.Provided.GREEN)
            .setPosition(montMartre).setVisible(true)
            .attachLabel(new MapLabel("visit day 3").setPosition(montMartre).setVisible(true));
        markers.add(markerMontMartre);
        return markers;
    }

    protected void animateMarker(Marker marker, Coordinate oldPosition, Coordinate newPosition) {
        // animate the marker to the new position
        final Transition transition = new Transition() {
            private final Double oldPositionLongitude = oldPosition.getLongitude();
            private final Double oldPositionLatitude = oldPosition.getLatitude();
            private final double deltaLatitude = newPosition.getLatitude() - oldPositionLatitude;
            private final double deltaLongitude = newPosition.getLongitude() - oldPositionLongitude;

            {
                setCycleDuration(Duration.millis(500.0));
                setOnFinished(evt -> marker.setPosition(newPosition));
            }

            @Override
            protected void interpolate(double v) {
                final double latitude = oldPosition.getLatitude() + v * deltaLatitude;
                final double longitude = oldPosition.getLongitude() + v * deltaLongitude;
                marker.setPosition(new Coordinate(latitude, longitude));
            }
        };
        transition.play();
    }


}
