package lpoo.geom;

import lpoo.math.*;

public class Cylinder extends Primitive
{
    private float radius;
    private float halfHeight; // (meia altura)

    public Cylinder(String name, float density, float radius, float halfHeight)
    {
        super(name, density);
        if (radius <= 0 || halfHeight <= 0)
            throw new IllegalArgumentException("radius and height must be an positive non-zero number");
        this.radius = radius;
        this.halfHeight = halfHeight;
        updateMassProperties();
    }

    public float getRadius()
    {
        return radius;
    }

    public float getHalfHeight()
    {
        return halfHeight;
    }

    public float getHeight()
    {
        return 2f * halfHeight;
    }

    @Override
    public float getVolume()
    {
        return (float) Math.PI * radius * radius * getHeight();
    }

    @Override
    public float getArea()
    {
        float lateral = 2f * (float) Math.PI * radius * getHeight();
        float caps = 2f * (float) Math.PI * radius * radius;
        return lateral + caps;
    }

    @Override
    protected Matrix3 computeLocalInertia()
    {
        float m = getMass();
        float s = halfHeight;
        float iyy = 0.5f * m * radius * radius;
        float ixx = (m / 12f) * (3f * radius * radius + 4f * s * s);
        return diagonal(ixx,iyy,ixx);
    }

    @Override
    public Bounds3 getBounds()
    {
        return new Bounds3(new Vector3(-radius, -halfHeight, -radius),new Vector3(radius, halfHeight, radius));
    }
}// Cylinder