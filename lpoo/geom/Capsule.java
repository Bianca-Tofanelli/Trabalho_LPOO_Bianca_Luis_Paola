package lpoo.geom;

import lpoo.math.*;

public class Capsule extends Primitive
{
    private float radius;
    private float halfHeight; // s: meia-altura da parte cilindrica

    public Capsule(String name, float density, float radius, float halfHeight)
    {
        super(name, density);
        if (radius <= 0 || halfHeight <= 0)
        {
            throw new IllegalArgumentException("radius and height must be an positive non-zero number");
        }
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

    private float getCylinderVolume()
    {
        return (float) Math.PI * radius * radius * (2f * halfHeight);
    }

    private float getSphereVolume()
    {
        return (4f / 3f) * (float) Math.PI * radius * radius * radius;
    }

    @Override
    public float getVolume()
    {
        return getCylinderVolume() + getSphereVolume();
    }

    @Override
    public float getArea()
    {
        float lateral = 2f * (float) Math.PI * radius * (2f * halfHeight);
        float spherical = 4f * (float) Math.PI * radius * radius;
        return lateral + spherical;
    }

    @Override
    protected Matrix3 computeLocalInertia()
    {
        float mc = density * getCylinderVolume();
        float ms = density * getSphereVolume();
        float r = radius;
        float s = halfHeight;
        float h = 2f * s;

        float iyy = 0.5f * mc * r * r + 0.4f * ms * r * r;

        float ixxCyl = (mc / 12f) * (3f * r * r + h * h);
        float ixxSph = ms * ((2f / 5f) * r * r + s * s + 0.75f * s * r);
        float ixx = ixxCyl + ixxSph;
        
        return Matrix3.diagonal(ixx, iyy, ixx);
    }

    @Override
    public Bounds3 getBounds()
    {
        float top = halfHeight + radius;
        return new Bounds3(new Vector3(-radius, -top, -radius),new Vector3(radius, top, radius));
    }
}// Capsule