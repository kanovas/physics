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
    private static final Double GRAVITY = 6674286700000000D;
    private static final Double MASS = 0.00000000000000000000091093835611D;
    private static final Double Z = 1.6E-19;
    private static final Double KULON = 8.85E-12;
    private ArrayList<Particle> currentState;
    private int PARTICLES_AMOUNT;
    Long time;
    private boolean working = false;
    private int d;
    private Canvas canvas;
    private Random random;
    private Integer num;
    Long currentTime = -1L;

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
            currentState.add(new Particle(x, y));
        }
    }

    public void start(Canvas canvas) {
        //this.canvas = canvas;


        working = true;
        //while (working) {
        getNextCondition();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //}
    }

    public void getNextCondition() {
        Long startTime = getCurrentTime();
        time = startTime - currentTime;
        if (currentTime == -1) time = 1L;
        currentTime = startTime;
        currentState.forEach(this::countParticle);
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


    private void countParticle(Particle particle) {
        double sumX = 0;
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
        }
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

    private double getKylonF(double x, double y, double x1, double y1) {
        double dist = getDistance(x, y, x1, y1);
        return KULON * Z * Z / (dist * dist);
    }

    private double getGravityF(double x, double y, double x1, double y1) {
        double dist = getDistance(x, y, x1, y1);
        return GRAVITY * MASS * MASS / (dist * dist);
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

    public void setNum(Integer num) {
        this.PARTICLES_AMOUNT = num;
    }
}
