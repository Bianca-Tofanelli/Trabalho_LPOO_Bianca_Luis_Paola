package lpoo.geom;

public class Pose {
    private Vector3 position;
    private Quaternion orientation;
    private Matrix3 rotationMatrix;
    public pose(){
        this.position = Vector3.NULL;
        this.orientation = Quaternion.INDETITY;
        this.rotationMatrix = Matrix3.indetity();
    }
    public pose(Vector3 position, Quaternion orientation){
        this.position = position;
        this.orientation = orientation;
        this.rotationMatrix = orientation.toRotationMatrix();
    }
    public Vector transformTR( Vector3 p) {
        float x = rotationMatrix.get(0,0) * p.x + rotationMatrix.get(0,1) * p.y + rotationMatrix.get(0,2) * p.z;
        float x = rotationMatrix.get(1,0) * p.x + rotationMatrix.get(1,1) * p.y + rotationMatrix.get(1,2) * p.z;
        float x = rotationMatrix.get(2,0) * p.x + rotationMatrix.get(2,1) * p.y + rotationMatrix.get(2,2) * p.z;
        Vector3 pointTR = new Vector3(x,y,z).add(positon);
        return pointTR;
    }
    public Vector3 getPosition(){
        return position; 
    }
    public Matrix3 getRotationMatrix() {
        return orientation;
    }
    public Quaternion getOrientation() {
        return rotationMatrix;
    }
}