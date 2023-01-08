package com.cgvsu;

import com.cgvsu.render_engine.RenderEngine;
import com.cgvsu.render_engine.Scene;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.awt.*;
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
    public Text camPosXTx;
    public Text camPosYTx;
    public Text camPosZTx;
    private List<Scene> scenes = new ArrayList<>();
    private int currSceneId = -1;

    @FXML
    AnchorPane anchorPane;

    @FXML
    private Canvas canvas;
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
                    new Vector3f(0, 0, 100),
                    new Vector3f(0, 0, 0),
                    1.0F, 1, 0.01F, 100)));
            currSceneId++;
        } catch (IOException exception) {

        }
    }

    @FXML
    public void openNextScene(ActionEvent actionEvent) {
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
        if (scenes.size() > 0) {
            scenes.get(currSceneId).camera.movePosition(new Vector3f(0, 0, -TRANSLATION));
            printCamPos();
        }
    }

    @FXML
    public void moveCameraBackward(ActionEvent actionEvent) {
        if (scenes.size() > 0) {
            scenes.get(currSceneId).camera.movePosition(new Vector3f(0, 0, TRANSLATION));
            printCamPos();
        }
    }

    @FXML
    public void moveCameraLeft(ActionEvent actionEvent) {
        if (scenes.size() > 0) {
            scenes.get(currSceneId).camera.circleHorMovePosition(TRANSLATION);
            printCamPos();
        }
    }

    @FXML
    public void moveCameraRight(ActionEvent actionEvent) {
        if (scenes.size() > 0) {
            scenes.get(currSceneId).camera.circleHorMovePosition(-TRANSLATION);
            printCamPos();
        }
    }

    @FXML
    public void moveCameraUp(ActionEvent actionEvent) {
        if (scenes.size() > 0) {
            scenes.get(currSceneId).camera.circleVerMovePosition(-TRANSLATION);
            printCamPos();
        }
    }

    @FXML
    public void moveCameraDown(ActionEvent actionEvent) {
        if (scenes.size() > 0) {
            scenes.get(currSceneId).camera.circleVerMovePosition(TRANSLATION);
            printCamPos();
        }
    }

    private void printCamPos() {
        camPosXTx.setText("x:" + scenes.get(currSceneId).camera.getPosition().x);
        camPosYTx.setText("y:" + scenes.get(currSceneId).camera.getPosition().y);
        camPosZTx.setText("z:" + scenes.get(currSceneId).camera.getPosition().z);
    }
}