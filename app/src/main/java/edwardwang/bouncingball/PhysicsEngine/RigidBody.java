package edwardwang.bouncingball.PhysicsEngine;

import edwardwang.bouncingball.PhysicsEngine.Vector.Vector3DDouble;
import edwardwang.bouncingball.PhysicsEngine.Vector.Vector3DInt;
import edwardwang.bouncingball.PhysicsEngine.Vector.Vector3DDirection;

/**
 * Created by edwardwang on 8/2/16.
 */
public class RigidBody {
    private double mass;    // kg
    private float orientation;  // radians
    private float angularVelocity; //radians
    private float torque;
    private double runSpeedMultiplier;

    //http://hyperphysics.phy-astr.gsu.edu/hbase/cm.html
    private Vector3DInt centerOfMass;

    //Used for calculations in physicsEngine
    private Vector3DDouble netAccel;
    private Vector3DDouble velocity;   //velocity
    private Vector3DDouble movementSpeed;
    private Vector3DInt deltaDistance;
    private Vector3DDirection direction;

    public RigidBody() {
        mass = 10;
        orientation = 0;
        angularVelocity = 0;
        torque = 0;
        runSpeedMultiplier = 1.5;
        netAccel = new Vector3DDouble();
        velocity = new Vector3DDouble();
        movementSpeed = new Vector3DDouble();
        deltaDistance = new Vector3DInt();
        direction = new Vector3DDirection();
    }

    /**
     * Movement speed is necessary because when resetting velocity,
     * app needs memory of original speed.
     * @param movementSpeedX
     * @param movementSpeedY
     */
    public void setMovementSpeed(double movementSpeedX, double movementSpeedY){
        movementSpeed.setX(movementSpeedX);
        movementSpeed.setY(movementSpeedY);
        resetVelocityX();
        resetVelocityY();
    }

    public void resetVelocityX(){
        velocity.setX(movementSpeed.getX());
    }

    public void resetVelocityY(){
        velocity.setY(movementSpeed.getY());
    }


    ////////////////////////////////////////////////////////////////////////
    //Getter
    public double getGravity(){
        return mass * PhysicsEngine.GRAVITY;
    }

    public double getNormal(){
        return mass * PhysicsEngine.NORMAL;
    }

    public Vector3DDouble getNetAccel(){
        return netAccel;
    }

    public double getMass() {
        return mass;
    }

    public float getOrientation() {
        return orientation;
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public float getTorque() {
        return torque;
    }

    public double getRunSpeedMultiplier() {
        return runSpeedMultiplier;
    }

    public Vector3DDouble getVelocity() {
        return velocity;
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    // Movement Speed
    public double getJumpSpeed(){
        return velocity.getY();
    }

    public double getWalkSpeed(){
        return velocity.getX();
    }

    public double getRunSpeed(){
        return velocity.getX() * runSpeedMultiplier;
    }

    public Vector3DInt getDeltaDistance() {
        return deltaDistance;
    }

    public Vector3DDouble getMovementSpeed() {
        return movementSpeed;
    }

    public Vector3DDirection getDirection() {
        return direction;
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //Setter
    private void setupNetForceX(){
        //nothing for now
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public void setOrientation(float orientation) {
        this.orientation = orientation;
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public void setTorque(float torque) {
        this.torque = torque;
    }

    public void setRunSpeedMultiplier(double runSpeedMultiplier) {
        this.runSpeedMultiplier = runSpeedMultiplier;
    }

    public void setDirectionX(Direction direction) {
        this.direction.setX(direction);
    }

    public void setDirectionY(Direction direction) {
        this.direction.setY(direction);
    }
}
