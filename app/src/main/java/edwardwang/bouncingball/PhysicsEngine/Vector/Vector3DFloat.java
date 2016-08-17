package edwardwang.bouncingball.PhysicsEngine.Vector;

/**
 * Created by edwardwang on 8/5/16.
 */
public class Vector3DFloat {
    float x, y, z;

    public Vector3DFloat() {
        x = 0f;
        y = 0f;
        z = 0f;
    }

    public Vector3DFloat(float x, float y) {
        this.x = x;
        this.y = y;
        z = 0;
    }

    public Vector3DFloat(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /*
    public float[] matrixMultiplcation(Vector3DFloat vector3DFloat){
        float[] result = new float[9];
        result[0] = ;
        result[1]
        result[2]
        result[3]
        result[4]
        result[5]
        result[6]
        result[7]
        result[8]
        result[9]

        return result;
    }
    */
    /*
    private float[] matrixMultiplication(float[] a, float[] b)
    {
        float[] result = new float[9];

        result[0] = a[0] * b[0] + a[1] * b[3] + a[2] * b[6];
        result[1] = a[0] * b[1] + a[1] * b[4] + a[2] * b[7];
        result[2] = a[0] * b[2] + a[1] * b[5] + a[2] * b[8];

        result[3] = a[3] * b[0] + a[4] * b[3] + a[5] * b[6];
        result[4] = a[3] * b[1] + a[4] * b[4] + a[5] * b[7];
        result[5] = a[3] * b[2] + a[4] * b[5] + a[5] * b[8];

        result[6] = a[6] * b[0] + a[7] * b[3] + a[8] * b[6];
        result[7] = a[6] * b[1] + a[7] * b[4] + a[8] * b[7];
        result[8] = a[6] * b[2] + a[7] * b[5] + a[8] * b[8];

        return result;
    }

     */

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }
}
