package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by alexeylevenets on 28/11/2016.
 */
class Particle {
    private double x;
    private double y;
    private double beforeX;
    private double beforeY;

    Particle(double x, double y) {
        this.x = x;
        this.y = y;
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

    public void setNewPosition(double x, double y) {
        beforeX = this.x;
        beforeY = this.y;
        this.x = x;
        this.y = y;
    }

    public void draw (GraphicsContext context) {
        context.setStroke(Color.BLACK);
        context.setFill(Color.GREEN);
        context.setLineWidth(1);
        context.fillOval(x, y, 2, 2);
        context.strokeOval(x, y, 2, 2);
    }
}
