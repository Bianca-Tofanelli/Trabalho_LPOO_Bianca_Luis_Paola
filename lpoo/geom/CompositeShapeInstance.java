package lpoo.geom;
import lpoo.math.*;

public class CompositeShapeInstance extends Shape{
    private final CompositeShape base;  
    public CompositeShapeInstance(String name, CompositeShape base){
        super(name);
        if (base == null) {
            throw new IllegalArgumentException("Error!!");
        }
        this.base = base;
        this.center_mass = base.getCenterOfMass();
        this.mass = base.getMass();
        this.local_inertia = base.getLocalInertia();
    }
    public Bounds3 getBounds(){
        Bounds3 localBounds = base.getBounds();
        if (localBounds == null){
            return null;
        }
        Vector3 min = localBounds.min();
        Vector3 max = localBounds.max();
        Vector3[] corners = {
            new Vector3(min.x, min.y, min.z),
            new Vector3(max.x, min.y, min.z),
            new Vector3(min.x, max.y, min.z),
            new Vector3(max.x, max.y, min.z),
            new Vector3(min.x, min.y, max.z),
            new Vector3(max.x, min.y, max.z),
            new Vector3(min.x, max.y, max.z),
            new Vector3(max.x, max.y, max.z)
    };
    Bounds3 worldBounds = new Bounds3();
        for (Vector3 corner : corners) {
            Vector3 transformedPoint = pose.transformTR(corner); 
            worldBounds.expand(transformedPoint);
        }

        return worldBounds;
    }
    public float getMass(){
        return base.getMass();
    }  
    public Matrix3 getLocalInertia(){
        return base.getLocalInertia();
    }
    public float getVolume(){
        return base.getVolume();
    }
    public float getArea(){
        return base.getArea();
    }

}