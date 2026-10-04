package lpoo.geom;

import lpoo.math.*;

/**
 *
 * @author Bianca Tofanelli
 *         Luis Cardoso
 *         Paola Vendruscolo 
 */
/* A classe pública Capsule, representa uma forma gemétrica, união de duas semiesferas e um cilindro, "filha" da classe Primitive. 
 */
public class Capsule extends Primitive
{
    private float radius;
    private float halfHeight; // meia-altura da parte cilindrica

    // construtor da Capsule
    public Capsule(String name, float density, float radius, float halfHeight)
    {
        super(name, density);
        // impede que o raio e a altura sejam nulos ou negativos
        if (radius <= 0 || halfHeight <= 0)
        {
            throw new IllegalArgumentException("radius and height must be an positive non-zero number");
        }
        this.radius = radius;
        this.halfHeight = halfHeight;
        updateMassProperties();
    }

    // pega o valor de raio
    public float getRadius()
    {
        return radius;
    }

    // pega o valor da meia-alutura
    public float getHalfHeight()
    {
        return halfHeight;
    }

    // faz a conta do volume do cilindro
    private float getCylinderVolume()
    {
        return (float) Math.PI * radius * radius * (2f * halfHeight);
    }

    // faz a conta do volume da esfera
    private float getSphereVolume()
    {
        return (4f / 3f) * (float) Math.PI * radius * radius * radius;
    }

    // sobrescreve o método de volume,calculando-o
    @Override
    public float getVolume()
    {
        return getCylinderVolume() + getSphereVolume();
    }

    // sobrescreve o método da área,calculando-a
    @Override
    public float getArea()
    {
        float lateral = 2f * (float) Math.PI * radius * (2f * halfHeight); // área da lateral do cilindro
        float spherical = 4f * (float) Math.PI * radius * radius; // área da parte esférica
        return lateral + spherical;
    }

    // sobrescreve o método do tensor de inércia,calculando-o
    @Override
    protected Matrix3 computeLocalInertia()
    {
        float partialCylinderMass = density * getCylinderVolume();
        float partialSphereMass = density * getSphereVolume();

        float iyy = 0.5f * partialCylinderMass * radius * radius + 0.4f * partialSphereMass * radius * radius; //inércia em Y do cilindro somada com a inércia em Y da esfera
        float ixxCyl = (partialCylinderMass / 12f) * (3f * radius * radius + 2f * halfHeight * 2f * halfHeight); // inércia base do cilindro, eixos X e Z
        float ixxSph = partialSphereMass * ((2f / 5f) * radius * radius + halfHeight * halfHeight + 0.75f * halfHeight * radius); // inércia das semiesferas deslocadas
        float ixx = ixxCyl + ixxSph;
        
        return Matrix3.diagonal(ixx, iyy, ixx);
    }

    // sobrescreve o método da caixa limitante, calculando-a
    @Override
    public Bounds3 getBounds()
    {
        return new Bounds3(new Vector3(-radius, -halfHeight - radius, -radius),new Vector3(radius, halfHeight + radius, radius));
    }
}// Capsule