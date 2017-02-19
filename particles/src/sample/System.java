package sample;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.*;
import java.util.concurrent.TimeUnit;


class System {

    private static final double DELTA = 0.5;
    private static final double GRAVITY = 6674286700000000D;
    private static final double KULON = 8.85E-12;
    private static final double timeStep = 0.05; //seconds
    private ArrayList<Particle> currentState;
    private int PARTICLES_AMOUNT;
    private boolean working = false;
    private int d;
    private Canvas canvas;
    private Random random;

    System() {
        random = new Random();
    }

    public void initializeCurrentState(Canvas canvas) {
        this.canvas = canvas;
        d = (int) Math.min(canvas.getHeight(), canvas.getWidth());

        currentState = new ArrayList<>();
        for (int i = 0; i < PARTICLES_AMOUNT; i++) {
            int x = random.nextInt(d - 2) + 1;
            int y = d / 2 + ((x < d / 2) ? (random.nextInt(2 * x) - x) : (random.nextInt(2 * (d - x)) - d + x));
            currentState.add(new Particle(x, y, timeStep, d));
        }
    }

    public void start(Canvas canvas, int num) {
        this.canvas = canvas;
        PARTICLES_AMOUNT = num;
        initializeCurrentState(canvas);

        working = true;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new java.util.TimerTask() {
            @Override
            public void run() {
                if (!working) this.cancel();
                Platform.runLater(System.this::getNextCondition);
            }
        }, 0, (long) (timeStep * 1000));
    }

    public void getNextCondition() {
        currentState.forEach(this::countParticle);
        setIsWorkingCondition();
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


    private void countParticle(Particle particle) {
        double cForceX = 0;
        double cForceY = 0;
        for (Particle p : currentState) {
            cForceX += particle.getCoulombForceX(p);
            cForceY += particle.getCoulombForceY(p);
        }
        particle.setNewPositionForces(cForceX, cForceY);

        //particle.setNewPosition(particle.getX() + random.nextInt(10) - 5, particle.getY() + random.nextInt(10) - 5);

        /*double sumX = 0;
        double sumY = 0;
        for (Particle p1 : currentState) {
            double cos = 0;
            double sin = 0;
            if (p1 != null && particle != null) {
                double x1 = p1.getX();
                double y1 = p1.getY();
                double x = particle.getX();
                double y = particle.getY();
                if (x1 != x || y1 != y) {
                    double F = getGravityF(x, y, x1, y1);
                    F -= getKylonF(x, y, x1, y1) * 3 * 10E21;
                    cos = (x1 - x) / getDistance(x1 - x, 0, 0, y1 - y);
                    sin = (y1 - y) / getDistance(x1 - x, 0, 0, y1 - y);
                    sumX += F * cos;
                    sumY += F * sin;
                }
            }
        }
        if (particle != null) {
            particle.setNewPosition(particle.getX() + 1000000 * sumX * time * time / (2 * MASS) + particle.getSpeedX() * time,
                    1000000 * sumY * time * time / (2 * MASS) + particle.getSpeedY() * time + particle.getY());
            particle.setSpeedX(time * 1000000 * sumX / MASS + particle.getSpeedX());
            particle.setSpeedX(time * 1000000 * sumY / MASS + particle.getSpeedY());
            if (getDistance(d / 2, d / 2, particle.getX(), particle.getY()) >= d / 2 - 5) {
                particle.setNewPosition((d / 2 - particle.getX()) / 50 + particle.getX(), (d / 2 - particle.getY()) / 50 + particle.getY());
                double cosa;
                double R = modul((particle.getX() - d / 2), (particle.getY() - d / 2));
                double V = modul(particle.getSpeedX(), particle.getSpeedY());
                if (R == 0 || V == 0) {
                    cosa = 1;
                } else {
                    cosa = ((particle.getX() - d / 2) * particle.getSpeedX() + (particle.getY() - d / 2) * particle.getSpeedY()) /
                            (R * V);
                }
                if (Double.isNaN(cosa)) return;
                double k = particle.getSpeedX() / particle.getSpeedY();
                double b = particle.getSpeedY() - k * particle.getSpeedX();
                double corner = Math.acos(cosa);
                double x = particle.getSpeedX();
                double y = particle.getSpeedY();
                if (d/2 > k * d/2 + b){
                    corner =  (Math.PI  - corner);
                } else {
                    corner = 2 * corner;
                }

                double cos = Math.cos(corner);
                double sin = Math.sin(corner);
                particle.setSpeedX(x * cos + y * sin);
                particle.setSpeedY(- x * sin + y * cos);
                particle.getSpeedX();
            }
        }*/
    }

    private double sinForCos(double cos) {
        return Math.sqrt(1 - cos * cos);
    }

    private double cosDoubleAngleFromCos(double cos) {
        return Math.cos(Math.acos(cos) * 2);//sqrt(1 - cos*cos);
    }

    private double modul(double a, double b) {
        return Math.sqrt(a * a + b * b);
    }

    private double getDistance(double x, double y, double x1, double y1) {
        return Math.sqrt((x1 - x) * (x1 - x) + (y1 - y) * (y1 - y));
    }

    private void setIsWorkingCondition() {
        working = false;
        for (Particle p : currentState) {
            if (p.delta() > DELTA) {
                working = true;
                return;
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
