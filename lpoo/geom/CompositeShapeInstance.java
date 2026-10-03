package lpoo.geom;

import lpoo.math.*;

public class CompositeShapeInstance extends Shape {
    private final CompositeShape base;

    public CompositeShapeInstance(String name, CompositeShape base) {
        super(name);
        if (base == null) {
            throw new IllegalArgumentException("A forma base não pode ser nula.");
        }
        this.base = base;
        this.mass = base.getMass();
        
    }

    
    @Override
    public Vector3 getCenterOfMass() {
        return this.pose.transformTR(base.getCenterOfMass());
    }

    
    @Override
    public Matrix3 getLocalInertia() {
        Matrix3 i = base.getLocalInertia();
        Matrix3 r = this.pose.getRotationMatrix();
        Matrix3 rT = r.transpose();
        return r.mul(i).mul(rT);
    }

    public Bounds3 getBounds() {
        Bounds3 localBounds = base.getBounds();
        if (localBounds == null) {
            return null;
        }
        Vector3 min = localBounds.min();
        Vector3 max = localBounds.max();
        Vector3[] vertices = {
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
        for (int i = 0; i < vertices.length; i++) {
            Vector3 transformedPoint = this.pose.transformTR(vertices[i]);
            worldBounds.expand(transformedPoint);
        }
        return worldBounds;
    }

    public float getMass() {
        return base.getMass();
    }

    public float getVolume() {
        return base.getVolume();
    }

    public float getArea() {
        return base.getArea();
    }
}