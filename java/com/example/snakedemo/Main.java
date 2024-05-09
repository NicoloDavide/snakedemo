package com.example.snakedemo;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private static final int WIDTH = 800;
    private static final int HEIGHT = WIDTH;
    private static final int ROWS = 20;
    private static final int SQUARE_SIZE = WIDTH / ROWS;

    private GraphicsContext gc;
    private List<Point> snakeBody = new ArrayList<>();
    private Point snakeHead;
    private boolean gameOver;
    private int currentDirection;
    private int score = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Snake");
        Group root = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        gc = canvas.getGraphicsContext2D();

        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            handleKeyPress(code);
        });

        initializeGame();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(120), e -> run()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void initializeGame() {
        snakeBody.clear();
        for (int i = 0; i < 6; i++) {
            snakeBody.add(new Point(5, ROWS / 2));
        }
        snakeHead = snakeBody.get(0);
        currentDirection = 0;
        gameOver = false;
    }

    private void handleKeyPress(KeyCode code) {
        if (!gameOver) {
            if (code == KeyCode.RIGHT || code == KeyCode.D) {
                if (currentDirection != 1) {
                    currentDirection = 0;
                }
            } else if (code == KeyCode.LEFT || code == KeyCode.A) {
                if (currentDirection != 0) {
                    currentDirection = 1;
                }
            } else if (code == KeyCode.UP || code == KeyCode.W) {
                if (currentDirection != 3) {
                    currentDirection = 2;
                }
            } else if (code == KeyCode.DOWN || code == KeyCode.S) {
                if (currentDirection != 2) {
                    currentDirection = 3;
                }
            }
        }
    }

    private void run() {
        if (gameOver) {
            drawGameOver();
            return;
        }

        drawBackground();
        drawSnake();
        drawScore();

        moveSnake();
        checkCollision();
    }

    private void drawBackground() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < ROWS; j++) {
                if ((i + j) % 2 == 0) {
                    gc.setFill(Color.web("AAD751"));
                } else {
                    gc.setFill(Color.web("A2D149"));
                }
                gc.fillRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }

    private void drawSnake() {
        gc.setFill(Color.web("#fba71b"));
        gc.fillRoundRect(snakeHead.getX() * SQUARE_SIZE, snakeHead.getY() * SQUARE_SIZE,
                SQUARE_SIZE - 1, SQUARE_SIZE - 1, 35, 35);

        for (int i = 1; i < snakeBody.size(); i++) {
            gc.fillRoundRect(snakeBody.get(i).getX() * SQUARE_SIZE, snakeBody.get(i).getY() * SQUARE_SIZE,
                    SQUARE_SIZE - 1, SQUARE_SIZE - 1, 20, 20);
        }
    }

    private void moveSnake() {
        for (int i = snakeBody.size() - 1; i >= 1; i--) {
            snakeBody.get(i).x = snakeBody.get(i - 1).x;
            snakeBody.get(i).y = snakeBody.get(i - 1).y;
        }

        switch (currentDirection) {
            case 0:
                snakeHead.x++;
                break;
            case 1:
                snakeHead.x--;
                break;
            case 2:
                snakeHead.y--;
                break;
            case 3:
                snakeHead.y++;
                break;
        }
    }

    private void checkCollision() {
        if (snakeHead.x < 0 || snakeHead.y < 0 || snakeHead.x >= ROWS || snakeHead.y >= ROWS) {
            gameOver = true;
        }

        for (int i = 1; i < snakeBody.size(); i++) {
            if (snakeHead.x == snakeBody.get(i).getX() && snakeHead.y == snakeBody.get(i).getY()) {
                gameOver = true;
                break;
            }
        }
    }

    private void drawScore() {
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Digital-7", 35));
        gc.fillText("Score: " + score, 10, 35);
    }

    private void drawGameOver() {
        gc.setFill(Color.RED);
        gc.setFont(new Font("Digital-7", 70));
        gc.fillText("Game Over", WIDTH / 3.5, HEIGHT / 2);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
