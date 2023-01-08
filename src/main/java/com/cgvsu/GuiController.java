package com.cgvsu;

import com.cgvsu.render_engine.RenderEngine;
import com.cgvsu.render_engine.Scene;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.awt.event.MouseEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Vector3f;

import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.render_engine.Camera;

public class GuiController {

    final private float TRANSLATION = 2F;
    private List<Scene> scenes = new ArrayList<>();
    private int currSceneId = -1;

    @FXML
    AnchorPane anchorPane;

    @FXML
    private Canvas canvas;

    private Model mesh = null;

    private Camera camera = new Camera(
            new Vector3f(0, 00, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private Timeline timeline;

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));

            if (scenes.size() != 0) {
                Scene scene = scenes.get(currSceneId);
                scene.camera.setAspectRatio((float) (width / height));
                scene.setHeight((int) height);
                scene.setWight((int) width);
                RenderEngine.render(canvas.getGraphicsContext2D(), scene);
            }

        });
        timeline.getKeyFrames().add(frame);
        timeline.play();
    }



    @FXML
    private void onOpenModelMenuItemClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");

        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());

        try {
            String fileContent = Files.readString(fileName);
            scenes.add(new Scene(currSceneId + 1, ObjReader.read(fileContent), new Camera(
                    new Vector3f(0, 00, 100),
                    new Vector3f(0, 0, 0),
                    1.0F, 1, 0.01F, 100)));
            currSceneId++;
//            mesh = ObjReader.read(fileContent);
        } catch (IOException exception) {

        }
    }

    @FXML
    private void openNextScene(ActionEvent actionEvent) {
        if (currSceneId < scenes.size() - 1) {
            currSceneId++;
        }
    }

    @FXML
    private void openPreviousScene(ActionEvent actionEvent) {
        if (currSceneId > 0) {
            currSceneId--;
        }
    }

    @FXML
    private void closeScene(ActionEvent actionEvent) {
        if (scenes.size() > 0) {
            if (currSceneId + 1 == scenes.size()) {
                scenes.remove(currSceneId);
                currSceneId--;
            } else {
                scenes.remove(currSceneId);
            }
        }
    }

    @FXML
    public void moveCameraForward(ActionEvent actionEvent) {
        scenes.get(currSceneId).camera.movePosition(new Vector3f(0, 0, -TRANSLATION));
    }

    @FXML
    public void moveCameraBackward(ActionEvent actionEvent) {
        scenes.get(currSceneId).camera.movePosition(new Vector3f(0, 0, TRANSLATION));
    }

    @FXML
    public void moveCameraLeft(ActionEvent actionEvent) {
        scenes.get(currSceneId).camera.circleHorMovePosition(TRANSLATION);
    }

    @FXML
    public void moveCameraRight(ActionEvent actionEvent) {
        scenes.get(currSceneId).camera.circleHorMovePosition(-TRANSLATION);
    }

    @FXML
    public void moveCameraUp(ActionEvent actionEvent) {
        scenes.get(currSceneId).camera.circleVerMovePosition(-TRANSLATION);
    }

    @FXML
    public void moveCameraDown(ActionEvent actionEvent) {
        scenes.get(currSceneId).camera.circleVerMovePosition(TRANSLATION);
    }
}