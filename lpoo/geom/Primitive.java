package lpoo.geom;

import lpoo.math.*;

/**
 *
 * @author Bianca Tofanelli
 *         Luis Cardoso
 *         Paola Vendruscolo 
 */
public abstract class Primitive extends Shape 
{

    protected float density;

    public Primitive(String name, float density)
    {
        super(name);
        if (density <= 0)
            throw new IllegalArgumentException("The density must be an positive non-zero number");
        this.density = density;
    }

    public float getDensity()
    {
        return density;
    }

    protected void updateMassProperties() //fica no final porque exige o volume que so e definido durante a construcao
    {
        this.mass = density * getVolume();
        this.local_inertia = computeLocalInertia();
    }

    protected abstract Matrix3 computeLocalInertia();
}// Primitives