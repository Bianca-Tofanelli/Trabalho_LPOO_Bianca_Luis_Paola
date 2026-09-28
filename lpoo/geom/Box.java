package lpoo.geom;
 
import lpoo.math.*;
 
public class Box extends Primitive
{
    private float sx, sy, sz; // meias dimensoes dos lados
 
    public Box(String name, float density, float sx, float sy, float sz)
    {
        super(name, density);
        if (sx <= 0 || sy <= 0 || sz <= 0)
            throw new IllegalArgumentException("The dimensions must be an positive non-zero number");
        this.sx = sx;
        this.sy = sy;
        this.sz = sz;
        updateMassProperties();
    }
 
    public float getHalfWidth()
    {
        return sx;
    }

    public float getHalfHeight()
    {
        return sy;
    }

    public float getHalfDepth()
    {
        return sz;
    }
 
    @Override
    public float getVolume()
    {
        return 8f * ( sx * sy * sz );
    }
 
    @Override
    public float getArea()
    {
        return 8f * (sx * sy + sy * sz + sz * sx);
    }
 
    @Override
    protected Matrix3 computeLocalInertia()
    {
        float m = getMass();
        ix = (m / 3f) * (sy * sy + sz * sz);
        iy = (m / 3f) * (sx * sx + sz * sz);
        iz = (m / 3f) * (sx * sx + sy * sy);
        return diagonal(ix, iy, iz);
    }
 
    @Override
    public Bounds3 getBounds()
    {
        return new Bounds3(new Vector3(-sx, -sy, -sz), new Vector3(sx, sy, sz));
    }
}// Box