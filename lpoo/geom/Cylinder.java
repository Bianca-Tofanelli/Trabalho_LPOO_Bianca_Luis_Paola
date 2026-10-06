package lpoo.geom;

import lpoo.math.*;

/**
 *
 * @author Bianca Tofanelli
 *         Luis Cardoso
 *         Paola Vendruscolo 
 */
/* Classe pública, "filha" de Primitive, que define um cilindro
 */
public class Cylinder extends Primitive {
    // armazena o raio e a meia altura
    private float radius;
    private float halfHeight; 

    // inicializa o cilindro
    public Cylinder(String name, float density, float radius, float halfHeight) {
        super(name, density);
        if (radius <= 0 || halfHeight <= 0)
            throw new IllegalArgumentException("radius and height must be an positive non-zero number");
        this.radius = radius;
        this.halfHeight = halfHeight;
        updateMassProperties();
    }

    // para pegar o raio 
    public float getRadius() {
        return radius;
    }

    // para pegar a metade da altura
    public float getHalfHeight() {
        return halfHeight;
    }

    // para pegar a altura total
    public float getHeight() {
        return 2f * halfHeight;
    }
    
    // calcula o volume total para que se consiga o pegar
    @Override
    public float getVolume() {
        return (float) Math.PI * radius * radius * getHeight();
    }

     // calcula a área total para que se consiga a pegar
    @Override
    public float getArea() {
        float lateral = 2f * (float) Math.PI * radius * getHeight();
        float caps = 2f * (float) Math.PI * radius * radius;
        return lateral + caps;
    }

     // calcula o tensor de inércia para que se consiga o pegar
    @Override
    protected Matrix3 computeLocalInertia() {
        float m = getMass();
        float s = halfHeight;
        float iyy = 0.5f * m * radius * radius;
        float ixx = (m / 12f) * (3f * radius * radius + 4f * s * s);
        return Matrix3.diagonal(ixx, iyy, ixx);
    }

     // para pegar a caixa limitante
    @Override
    public Bounds3 getBounds() {
        return new Bounds3(new Vector3(-radius, -halfHeight, -radius),new Vector3(radius, halfHeight, radius));
    }
}// Cylinder