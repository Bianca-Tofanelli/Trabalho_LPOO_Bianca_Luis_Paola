package lpoo.geom;
 
import lpoo.math.*;
 
/**
 *
 * @author Bianca Tofanelli
 *         Luis Cardoso
 *         Paola Vendruscolo 
 */
public class Sphere extends Primitive
{ 
    private float radius;
 
    public Sphere(String name, float density, float radius)
    {
        super(name, density);
        if (radius <= 0)
            throw new IllegalArgumentException("radius must be an positive non-zero number");
        this.radius = radius;
        updateMassProperties();
    }
 
    public float getRadius()
    {
        return radius;
    }
 
    @Override
    public float getVolume()
    {
        return (4f / 3f) * (float) Math.PI * radius * radius * radius;
    }
 
    @Override
    public float getArea()
    {
        return 4f * (float) Math.PI * radius * radius;
    }
 
    @Override
    protected Matrix3 computeLocalInertia()
    {
        float m = getMass();
        float i = (2f / 5f) * m * radius * radius;
        return Matrix3.diagonal(i);
    }
 
    @Override
    public Bounds3 getBounds()
    {
        return new Bounds3(new Vector3(-radius, -radius, -radius),new Vector3(radius, radius, radius));
    }
} // Sphere
 