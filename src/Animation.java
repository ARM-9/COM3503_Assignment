import java.util.function.Consumer;

public class Animation {
    private Consumer<Double> movement;
    private double start;
    private double stop;
    private boolean moving;

    public Animation(Consumer<Double> movement) {
        this.movement = movement;
    }

    public Consumer<Double> getMovement() {
        return this.movement;
    }

    public void setMovement(Consumer<Double> movement) {
        this.movement = movement;
    }

    public double getStart() {
        return this.start;
    }
    
    public double getStop() {
        return this.stop;
    }

    public void setStop(double stop) {
        this.stop = stop;
    }

    public void setStart(double start) {
        this.start = start;
    }

    public boolean isMoving() {
        return this.moving;
    }

    public boolean getMoving() {
        return this.moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void toggleMoving() {
        if ( moving ) {
            moving = false;
        } else {
            moving = true;
        }
    }
}