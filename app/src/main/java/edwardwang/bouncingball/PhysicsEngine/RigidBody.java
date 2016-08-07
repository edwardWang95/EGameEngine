package edwardwang.bouncingball.PhysicsEngine;

import edwardwang.bouncingball.PhysicsEngine.Vector2D.Direction;
import edwardwang.bouncingball.PhysicsEngine.Vector2D.Vector2DDouble;
import edwardwang.bouncingball.PhysicsEngine.Vector2D.Vector2DInt;
import edwardwang.bouncingball.PhysicsEngine.Vector2D.Vector2DDirection;

/**
 * Created by edwardwang on 8/2/16.
 */
public class RigidBody {
    private double mass;    // kg
    private float orientation;  // radians
    private float angularVelocity; //radians
    private float torque;
    private double runSpeedMultiplier;

    //Used for calculations in physicsEngine
    //TODO: may not need movement speed or initial/final, just one velocity object

    private Vector2DDouble velocityInitial;
    private Vector2DDouble velocityFinal;

    private Vector2DDouble netForce;

    //TODO:Keep
    private Vector2DDouble netAccel;
    private Vector2DDouble velocity;   //velocity
    private Vector2DDouble movementSpeed;
    private Vector2DInt deltaDistance;
    private Vector2DDirection direction;

    public RigidBody() {
        mass = 10;
        orientation = 0;
        angularVelocity = 0;
        torque = 0;
        runSpeedMultiplier = 1.5;
        velocityInitial = new Vector2DDouble();
        velocityFinal = new Vector2DDouble();
        netForce = new Vector2DDouble();
        netAccel = new Vector2DDouble();
        velocity = new Vector2DDouble();
        movementSpeed = new Vector2DDouble();
        deltaDistance = new Vector2DInt();
        direction = new Vector2DDirection();
    }

    public void setMovementSpeed(double movementSpeedX, double movementSpeedY){
        movementSpeed.setX(movementSpeedX);
        movementSpeed.setY(movementSpeedY);
        resetVelocity();
    }

    public void resetVelocity(){
        velocity.setX(movementSpeed.getX());
        velocity.setY(movementSpeed.getY());
    }


    ////////////////////////////////////////////////////////////////////////
    //TODO: REMOVE

    public void addAccelX(double appliedForce){
        netForce.setX(netForce.getX() + (mass * appliedForce));
    }

    public void addAccelY(double appliedForce){
        netForce.setY(netForce.getY() + (mass * appliedForce));
    }

    public void resetNetForce(){
        netForce.setX(0);
        netForce.setY(0);
        netAccel.setX(0);
        netAccel.setY(0);
        setupNetForceX();
        setupNetForceY();
    }

    /**
     * Formula used is:
     * vf = sqrt( vi^2 - 2ad  )
     * @param distanceX
     * @param distanceY
     */
    public void updateVelocity(double distanceX, double distanceY){
        double finalX = Math.sqrt((velocityInitial.getX() *
                        velocityInitial.getX()) + 2 *
                        (netAccel.getX() * distanceX));
        velocityFinal.setX(finalX);

        double finalY = Math.sqrt((velocityInitial.getY() *
                        velocityInitial.getY()) + 2 *
                        (netAccel.getY() * distanceY));
        velocityFinal.setY(finalY);
    }


    ////////////////////////////////////////////////////////////////////////
    //Getter
    private double getGravity(){
        return mass * PhysicsEngine.GRAVITY;
    }

    private double getNormal(){
        return mass * PhysicsEngine.NORMAL;
    }

    public Vector2DDouble getVelocityInitial() {
        return velocityInitial;
    }

    public Vector2DDouble getVelocityFinal() {
        return velocityFinal;
    }

    public Vector2DDouble getNetForce() {
        return netForce;
    }

    public Vector2DDouble getNetAccel(){
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

    public Vector2DDouble getVelocity() {
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

    public Vector2DInt getDeltaDistance() {
        return deltaDistance;
    }

    public Vector2DDouble getMovementSpeed() {
        return movementSpeed;
    }

    public Vector2DDirection getDirection() {
        return direction;
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //Setter
    private void setupNetForceX(){
        //nothing for now
    }

    private void setupNetForceY(){
        netForce.setY(getGravity() + getNormal());
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
