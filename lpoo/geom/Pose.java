package lpoo.geom;
import lpoo.math.*;

/**
 *
 * @author Bianca Tofanelli
 *         Luis Cardoso
 *         Paola Vendruscolo 
 */
public class Pose {
    private Vector3 position;
    private Quaternion orientation;
    private Matrix3 rotationMatrix;
    public Pose(){
        this.position = Vector3.NULL;
        this.orientation = Quaternion.IDENTITY;
        this.rotationMatrix = Matrix3.identity();
    }
    public Pose(Vector3 position, Quaternion orientation){
        this.position = position;
        this.orientation = orientation;
        this.rotationMatrix = orientation.toRotationMatrix();
    }    
    public Vector3 transformTR(Vector3 p) {
        float x = rotationMatrix.get(0,0) * p.x + rotationMatrix.get(0,1) * p.y + rotationMatrix.get(0,2) * p.z;
        float y = rotationMatrix.get(1,0) * p.x + rotationMatrix.get(1,1) * p.y + rotationMatrix.get(1,2) * p.z;
        float z = rotationMatrix.get(2,0) * p.x + rotationMatrix.get(2,1) * p.y + rotationMatrix.get(2,2) * p.z;
        Vector3 pointTR = new Vector3(x, y, z).add(this.position);
        return pointTR;
    }
    public Vector3 getPosition(){
        return position; 
    }
    public Matrix3 getRotationMatrix() {
        return rotationMatrix;
    }
    public Quaternion getOrientation() {
        return orientation;
    }
}