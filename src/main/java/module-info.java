module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires javafx.media;
    requires tika.core;
    requires tika.parsers;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    opens vn.tqt.player.music to javafx.fxml,com.fasterxml.jackson.core,com.fasterxml.jackson.databind,com.fasterxml.jackson.annotation;
    exports vn.tqt.player.music;
    exports vn.tqt.player.music.repository;
    opens vn.tqt.player.music.repository to com.fasterxml.jackson.annotation, com.fasterxml.jackson.core, com.fasterxml.jackson.databind, javafx.fxml;
    exports vn.tqt.player.music.services;
    opens vn.tqt.player.music.services to com.fasterxml.jackson.annotation, com.fasterxml.jackson.core, com.fasterxml.jackson.databind, javafx.fxml;
}