package sample;

/**
 * Created by alexeylevenets on 28/11/2016.
 */
class Particle {
    private double x;
    private double y;
    private double z;

    Particle(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    double getDistanceTo(Particle target) {
        return Math.sqrt(Math.pow(this.x - target.x, 2)
                       + Math.pow(this.y - target.y, 2)
                       + Math.pow(this.z - target.z, 2));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
