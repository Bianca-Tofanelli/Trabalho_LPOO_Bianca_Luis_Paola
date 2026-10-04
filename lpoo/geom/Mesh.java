package lpoo.geom;

import lpoo.math.*;

public class Mesh extends Shape
{
    protected float density;
    private Index3[] triangles;
    private Vector3[] vertex;

    public Mesh(String name, float density, Index3[] triangles, Vector3[] vertex )
    {
        super(name);

        if (density <= 0)
            throw new IllegalArgumentException("The density must be a positive non-zero number");
        
        if (triangles.length <= 0)
            throw new IllegalArgumentException("The triangle count must be a positive non-zero number.");
        
        if (vertex.length == 0 )
            throw new IllegalArgumentException("The mesh must have at least one vertex.");

        this.triangles = triangles;
        this.vertex = vertex;
        this.density = density;

    float totalSignedVolume = 0;
    
    float sumCenterX = 0;
    float sumCenterY = 0;
    float sumCenterZ = 0;

    for (int t = 0; t < triangles.length; t++) {
        Index3 x = triangles[t];
        Vector3 a = vertex[x.i];
        Vector3 b = vertex[x.j];
        Vector3 c = vertex[x.k];

       
        float v_tetra = tetrahedronVolume(a, b, c);
        
        float cx = (a.x + b.x + c.x) / 4.0f;
        float cy = (a.y + b.y + c.y) / 4.0f;
        float cz = (a.z + b.z + c.z) / 4.0f;

        sumCenterX += cx * v_tetra;
        sumCenterY += cy * v_tetra;
        sumCenterZ += cz * v_tetra;
        
        totalSignedVolume += v_tetra;
    }

    float finalVolume = Math.abs(totalSignedVolume);
    this.mass = this.density * finalVolume;

    if (finalVolume > 0) {
        this.center_mass = new Vector3(
            sumCenterX / totalSignedVolume, 
            sumCenterY / totalSignedVolume, 
            sumCenterZ / totalSignedVolume
        );
    } else {
        this.center_mass = new Vector3(0, 0, 0);
    }
    
    this.local_inertia = computeLocalInertia();
    }

    public float triangleArea(Vector3 a, Vector3 b, Vector3 c)
    {
        Vector3 u = b.sub(a);
        Vector3 v = c.sub(a);
        float uv = u.dot(v);
        float w = (float)u.normSquared() * v.normSquared() - uv * uv;

        return 0.5f * (float)Math.sqrt(Math.max(0.0f, w));
    }

    @Override
    public float getArea()
    {
        float sum = 0;

        for (int t = 0; t < triangles.length; t++)
        {
            Index3 x = triangles[t];
            Vector3 a = vertex[x.i];
            Vector3 b = vertex[x.j];
            Vector3 c = vertex[x.k];

            sum += triangleArea(a, b, c);
        }
        return sum;
    }

    public float tetrahedronVolume(Vector3 a, Vector3 b, Vector3 c)
    {
        float det = a.x * (b.y * c.z - b.z * c.y) - a.y * (b.x * c.z - b.z * c.x) + a.z * (b.x * c.y - b.y * c.x);
        return (det/6);
    }

    @Override
    public float getVolume()
    {
      
        if (this.density > 0) {
            return this.mass / this.density;
        }
        return 0;
    }

    public Vector3 boundsMin()
    {
        Vector3 p = vertex[0];
        for (int i = 1; i < vertex.length; i++) {
            p = Vector3.min(p, vertex[i]);
        }
        return p;
    }

    public Vector3 boundsMax()
    {
        Vector3 p = vertex[0];
        for (int i = 1; i < vertex.length; i++){
            p = Vector3.max(p, vertex[i]);
        }
        return p;
    }

    @Override
    public Bounds3 getBounds()
    {
        return new Bounds3(boundsMin(), boundsMax());
    }

    protected Matrix3 computeLocalInertia() {
        
        return Matrix3.zero(); 
    }
}