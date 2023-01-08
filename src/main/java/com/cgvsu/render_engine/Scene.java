package com.cgvsu.render_engine;

import com.cgvsu.model.Model;
import javafx.scene.canvas.GraphicsContext;

public class Scene {
    private int id;
    public Model mesh;
    public Camera camera;
    private int wight;
    private int height;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Model getMesh() {
        return mesh;
    }

    public void setMesh(Model mesh) {
        this.mesh = mesh;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public int getWight() {
        return wight;
    }

    public void setWight(int wight) {
        this.wight = wight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Scene(int id, Model mesh, Camera camera, int wight, int height) {
        this.id = id;
        this.mesh = mesh;
        this.camera = camera;
        this.wight = wight;
        this.height = height;
    }

    public Scene(int id, Model mesh, Camera camera) {
        this.id = id;
        this.mesh = mesh;
        this.camera = camera;
        this.wight = 0;
        this.height = 0;
    }
}
