package org.proyectococinav2.ui.controller;

import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class Controller {
    private Scene menuScene;

    public void setMenuScene(Scene menuScene) {
        this.menuScene = menuScene;
    }

    public void volverAlMenuPrincipal(Scene currentScene) {
        Stage stage = (Stage) currentScene.getWindow();
        stage.setScene(menuScene);
        stage.setTitle("Menú Principal");
        stage.show();
    }
}
