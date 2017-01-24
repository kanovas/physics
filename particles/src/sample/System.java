package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;


class System {

    private static final double DELTA = 0.5;
    private static final Long TICK_TIME = 100L;
    private ArrayList<Particle> currentState;
    private ArrayList<Particle> subState;
    private int PARTICLES_AMOUNT;
    private boolean working = false;
    private int d;
    private Canvas canvas;
    private Random random;

    System() {
        random = new Random();
    }

    private void initializeCurrentState() {
        for (int i = 0; i < PARTICLES_AMOUNT; i++) {
            int x = random.nextInt(d - 2) + 1;
            int y = d/2 + ((x < d/2)? (random.nextInt(2 * x) - x) : (random.nextInt(2 * (d - x)) - d + x));
            currentState.add(new Particle(x, y));
        }
    }
    
    public void start (int part_amount, Canvas canvas) {
        this.canvas = canvas;
        d = (int)Math.min(canvas.getHeight(), canvas.getWidth());

        PARTICLES_AMOUNT = part_amount;
        currentState = new ArrayList<>();
        initializeCurrentState();

        working = true;
        while (working) {
            getNextCondition();
        }
    }

    private void getNextCondition() {
        Long startTime = getCurrentTime();
        subState = currentState;
        currentState = new ArrayList<>();
        for (Particle p : subState)
            currentState.add(countParticle(p));
        setIsWorkingCondition();
        correctTickTime(startTime);
        draw();
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);
        gc.fillOval(0, 0, d, d);
        gc.strokeOval(0, 0, d, d);
        for (Particle p : currentState) {
            p.draw(gc);
        }
    }

    private Particle countParticle(Particle particle) {
        //TODO : count Particle position according to subState and currentState
        return particle;
    }

    private void setIsWorkingCondition() {
        working = false;
        for (int i = 0; i < PARTICLES_AMOUNT; i++) {
            if (subState.get(i).getDistanceTo(currentState.get(i)) > DELTA) {
                working = true;
                return;
            }
        }
    }

    private void correctTickTime(Long startTime) {
        Long workingTime = getCurrentTime() - startTime;
        if (workingTime < TICK_TIME) {
            try {
                TimeUnit.SECONDS.sleep((TICK_TIME - workingTime) / 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Long getCurrentTime() {
        Date date = new Date();
        return date.getTime();
    }

    boolean isWorking() {
        return working;
    }
}
