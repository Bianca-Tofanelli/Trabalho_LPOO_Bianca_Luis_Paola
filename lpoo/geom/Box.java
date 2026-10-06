package lpoo.geom;
 
import lpoo.math.*;
 
/**
 *
 * @author Bianca Tofanelli
 *         Luis Cardoso
 *         Paola Vendruscolo 
 */
/* A classe pública Box, representa uma forma gemétrica, "filha" da classe Primitive. 
 */
public class Box extends Primitive {
    private float sx, sy, sz; // meias dimensoes dos lados
 
    // construtor da Box
    public Box(String name, float density, float sx, float sy, float sz) {
        super(name, density);

        // impede a criação de caixas com dimensões com valor nulo ou abaixo de 0
        if (sx <= 0 || sy <= 0 || sz <= 0) {
            throw new IllegalArgumentException("As dimensões devem ter valores maiores que zero.");
        }
        
        this.sx = sx;
        this.sy = sy;
        this.sz = sz;
        updateMassProperties();
    }
 
    // para pegar o valor das dimensões 
    public float getHalfWidth() {
        return sx;
    }

    public float getHalfHeight() {
        return sy;
    }

    public float getHalfDepth() {
        return sz;
    }
 
    // sobrescreve o método de volume,calculando-o
    @Override
    public float getVolume() {
        return 8f * ( sx * sy * sz );
    }
 
    // sobrescreve o método da área,calculando-a
    @Override
    public float getArea() {
        return 8f * (sx * sy + sy * sz + sz * sx);
    }
 
    // sobrescreve o método do tensor de inércia,calculando-o
    @Override
    protected Matrix3 computeLocalInertia() {
        float m = getMass(); // pega a massa
        float ix = (m / 3f) * (sy * sy + sz * sz);
        float iy = (m / 3f) * (sx * sx + sz * sz);
        float iz = (m / 3f) * (sx * sx + sy * sy);
        return Matrix3.diagonal(ix, iy, iz);
    }
 
    // sobrescreve o método da caixa limitante,calculando-a
    @Override
    public Bounds3 getBounds() {
        return new Bounds3(new Vector3(-sx, -sy, -sz), new Vector3(sx, sy, sz));
    }
}// Box