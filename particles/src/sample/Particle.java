package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Pair;

/**
 * Created by alexeylevenets on 28/11/2016.
 */
class Particle {
    private double x; //points
    private double y; // points
    private static final double q = -1.6021766208E-19; //Kl
    private double beforeX; //points
    private double beforeY; // points
    private double real = 1; //points to meters
    private static final double MASS = 9.10938356E-31; //kg
    private static final double fk = 0.3;
    private static double K = 8.9875517873681764E9;
    private double speedX = 0; //points/sec
    private double speedY = 0; //points/sec
    private static double timeStep;
    private int r;

    Particle(double x, double y, double timeStep, int d) {
        this.x = x;
        this.y = y;
        this.timeStep = timeStep;
        this.r = d/2;
    }

    double getDistanceTo(Particle target) {
        return Math.sqrt(Math.pow(this.x - target.x, 2)
                       + Math.pow(this.y - target.y, 2));
    }

    double getDistanceTo(double tx, double ty) {
        return Math.sqrt(Math.pow(this.x - tx, 2)
                + Math.pow(this.y - ty, 2));
    }

    public double delta() {
        return getDistanceTo(beforeX, beforeY);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setNewPosition(double newX, double newY) {
        if ((newX - r)*(newX - r) + (newY - r)*(newY-r) >= r*r) {
            double k = (newY - y) / (newX - x);
            double b = y - k*x;
            double D = (2*k*b - 2*r*k - 2*r) * (2*k*b - 2*r*k - 2*r) - 4 * (r*r + b*b - 2*r*b)*(1 + k*k);
            double x1 = (-(2*k*b - 2*r*k - 2*r) + Math.sqrt(D)) / (2*(1 + k*k));
            double x2 = (-(2*k*b - 2*r*k - 2*r) - Math.sqrt(D)) / (2*(1 + k*k));
            double xp = 0; // point where circle & line intersects
            if (newX > x) {
                xp = (x1 < newX && x1 > x)? x1 : x2;
            }
            else {
                xp = (x1 < x && x1 > newX)? x1 : x2;
            }
            double yp = k*xp + b;

            double xn = ((x*xp+y*yp-x*r-y*r)/(yp-r) - (r*xp-r*yp)/(xp-r))/((yp-r)/(xp-r) - (r-xp)/(yp-r));
            double yn = xn*(yp-r)/(xp-r) + (r*xp-r*yp)/(xp-r);
            newX = x + 2 * (xn - x);
            newY = y + 2 * (yn - y);
        }
        beforeX = this.x;
        beforeY = this.y;
        this.x = newX;
        this.y = newY;
        speedX = (x - beforeX) / timeStep;
        speedY = (y - beforeY) / timeStep;
    }

    public void setNewPositionForces(double cForceX, double cForceY) {
        //friction power
        double forceX = cForceX - fk * (speedX * real / timeStep);
        double forceY = cForceY - fk * (speedY * real / timeStep);

        //x = x0 + v0*t + (F/m)*t^2/2
        double ax = forceX / MASS; // m/s
        double ay = forceY / MASS; // m/s
        double newX = x + speedX * timeStep + (ax * timeStep * timeStep/2) / real;
        double newY = y + speedY * timeStep + (ay * timeStep * timeStep/2) / real;

        setNewPosition(newX, newY);
    }

    public void draw (GraphicsContext context) {
        context.setStroke(Color.BLACK);
        context.setFill(Color.GREEN);
        context.setLineWidth(1);
        context.fillOval(x, y, 2, 2);
        context.strokeOval(x, y, 2, 2);
    }

    public double getCoulombForceX(Particle other) {
        if (other.equals(this)) return 0;
        return K * q * q / (Math.pow((x - other.getX()) * real, 2));
    }
    public double getCoulombForceY(Particle other) {
        if (other.equals(this)) return 0;
        return K * q * q / (Math.pow((y - other.getY()) * real, 2));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Particle other = (Particle) obj;
        return x == other.getX() && y == other.getY();
    }

}
