package ru.nsu.titov.model;

import static ru.nsu.titov.model.Direction.UNDEFINED;
import static ru.nsu.titov.model.ObjectId.DEFAULT;

abstract public class GameObject {
    protected int x;
    protected int y;

    protected int ticksPassed = 0;
    protected int velocity;

    protected boolean doorsPassing = false;
    protected boolean stopFlag = false;
    protected final int uniqueId = UniqueId.getId();
    protected ObjectId ID = DEFAULT;
    protected Direction direction = UNDEFINED;

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isDoorsPassing() {
        return doorsPassing;
    }

    public void setDoorsPassing(boolean doorsPassing) {
        this.doorsPassing = doorsPassing;
    }

    public static boolean checkCollision(GameObject obj1, GameObject obj2) {
        return obj1.x == obj2.x && obj1.y == obj2.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;

    }

    public void setDirection(Direction direction) {
        if (this.direction != direction) {
            this.direction = direction;
            stopFlag = false;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public ObjectId getID() {
        return ID;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getNextX() {
        return switch (direction) {
            case RIGHT -> x + 1;
            case LEFT -> x - 1;
            default -> x;
        };
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public int getNextY() {
        return switch (direction) {
            case UP -> y - 1;
            case DOWN -> y + 1;
            default -> y;
        };
    }

    //TODO сделать интерфейс для модели (возможно)
    public abstract void tick(Model model);

    public abstract void onCollide(GameObject object, Model model);

    public int getVelocity() {
        return velocity;
    }

    public int getTicksPassed() {
        return ticksPassed;
    }
}
