package lpoo.geom;

import lpoo.math.*;

/**
 *
 * @author Bianca Tofanelli
 *         Luis Cardoso
 *         Paola Vendruscolo 
 */
// A classe publica "Mesh" representa a malha de triangulos , "filha" da classe Shape
public class Mesh extends Shape {
    // declara os valores pertinentes a composicao da malha
    protected float density;
    private Index3[] triangles;
    private Vector3[] vertex;

    // inicia a malha , recebendo seus componentes fundamentais junto com seu nome
    public Mesh(String name, float density, Index3[] triangles, Vector3[] vertex) {
        // recebe o nome herdado de shape
        super(name);

        // verifica se a densidade e um numero natural, imprimindo mensagem de erro em caso falho
        if (density < 0) {
            throw new IllegalArgumentException("The density must be a positive number");
        }

        // verifica se existem triangulos no programa , imprimindo mensagem de erro em caso falho
        if (triangles == null || triangles.length == 0) {
            throw new IllegalArgumentException("The triangle count must be a positive non-zero number");
        }

        // verifica se existem vertices no programa , imprimindo mensagem de erro no em caso falho
        if (vertex == null || vertex.length == 0) {
            throw new IllegalArgumentException("The mesh must have at least one vertex");
        }

        // atribui os valores recebidos pela declaracao aos componentes da malha
        this.triangles = triangles;
        this.vertex = vertex;
        this.density = density;

        // declara os valores representantes do volume e coordenadas do centro da malha
        float MeshVolume = 0;
        float sumCenterX = 0;
        float sumCenterY = 0;
        float sumCenterZ = 0;

        // calcula o volume e centro de massa da malha por meio da soma de decomposicao em tetraedros 
        for (int t = 0; t < triangles.length; t++) {
            // determina os valores do triangulo a ser trabalhado
            Index3 x = triangles[t];
            Vector3 a = vertex[x.i];
            Vector3 b = vertex[x.j];
            Vector3 c = vertex[x.k];

            // realiza o calculo de volume do tetraedro
            float v_tetra = tetrahedronVolume(a, b, c); 
            
            // determina as coordenadas do centro de massa do tetraedro
            float cx = (a.x + b.x + c.x) / 4.0f;
            float cy = (a.y + b.y + c.y) / 4.0f;
            float cz = (a.z + b.z + c.z) / 4.0f;

            // determina as componentes dos eixos da malha pela soma dos componentes dos tetraedros
            sumCenterX += cx * v_tetra;
            sumCenterY += cy * v_tetra;
            sumCenterZ += cz * v_tetra;
            
            // determina o volume da malha por meio da soma do volume dos tetraedros
            MeshVolume += v_tetra;
        }

        // determina o volume absoluto do volume
        float AbsoluteMeshVolume = Math.abs(MeshVolume);

        // calcula a massa da malha
        this.mass = this.density * AbsoluteMeshVolume;

        // caso o volume nao seja nulo , realiza o calculo das coordenadas de seu centro de massa
        if (AbsoluteMeshVolume > 0) {
            this.center_mass = new Vector3(
                sumCenterX / MeshVolume, 
                sumCenterY / MeshVolume, 
                sumCenterZ / MeshVolume
            ); 
        }
        // caso o volume seja nulo, o centro de massa tambem sera
        else {
            this.center_mass = new Vector3(0, 0, 0);
        }
       
        // realiza o calculo do tensor de inercia da malha
        this.local_inertia = computeLocalInertia();
    }

    // realiza o calculo da area de um triangulo por meio de suas coordenadas
    public float triangleArea(Vector3 a, Vector3 b, Vector3 c) {
        Vector3 u = b.sub(a);
        Vector3 v = c.sub(a);
        float uv = u.dot(v);
        float w = (float)u.normSquared() * v.normSquared() - uv * uv;

        return 0.5f * (float)Math.sqrt(Math.max(0.0f, w));
    }

    // sobrescreve o metodo de calculo de area global com o local
    @Override
    public float getArea() {
        float sum = 0;

        for (int t = 0; t < triangles.length; t++) {
            Index3 x = triangles[t];
            sum += triangleArea(vertex[x.i], vertex[x.j], vertex[x.k]);
        }

        return sum;
    }

    // realiza o calculo do volume de um tetraedro por meio de tres de suas coordenadas , sendo assumido a quarta em (0,0,0)
    public float tetrahedronVolume(Vector3 a, Vector3 b, Vector3 c) {
        float det = a.x * (b.y * c.z - b.z * c.y) - a.y * (b.x * c.z - b.z * c.x) + a.z * (b.x * c.y - b.y * c.x);
        return (det / 6.0f);
    }

    // sobrescreve o metodo de calculo de volume global com o local
    @Override
    public float getVolume() {

        if (this.density > 0) {
            return this.mass / this.density;
        }

        return 0;
    }

    // retorna o ponto minimo da caixa limitante
    public Vector3 boundsMin() {
        Vector3 p = vertex[0];

        for (int i = 1; i < vertex.length; i++) {
            p = Vector3.min(p, vertex[i]);
        }

        return p;
    }

    // retorna o ponto maximo da caixa limitante
    public Vector3 boundsMax() {
        Vector3 p = vertex[0];

        for (int i = 1; i < vertex.length; i++) {
            p = Vector3.max(p, vertex[i]);
        }

        return p;
    }

    // determina a caixa limitante por meio de seus pontos maximo e minimos
    @Override
    public Bounds3 getBounds() {
        return new Bounds3(boundsMin(), boundsMax());
    }

   // realiza o calculo do tensor de inercia da malha
    protected Matrix3 computeLocalInertia() {
        float Ixx = 0, Iyy = 0, Izz = 0;
        float Ixy = 0, Ixz = 0, Iyz = 0;

        for (int t = 0; t < triangles.length; t++) {
            Index3 idx = triangles[t];
            Vector3 a = vertex[idx.i];
            Vector3 b = vertex[idx.j];
            Vector3 c = vertex[idx.k];

            float v = tetrahedronVolume(a, b, c);

            float exx = a.x*a.x + b.x*b.x + c.x*c.x + a.x*b.x + a.x*c.x + b.x*c.x;
            float eyy = a.y*a.y + b.y*b.y + c.y*c.y + a.y*b.y + a.y*c.y + b.y*c.y;
            float ezz = a.z*a.z + b.z*b.z + c.z*c.z + a.z*b.z + a.z*c.z + b.z*c.z;

            float exy = a.x*a.y + b.x*b.y + c.x*c.y + 0.5f*(a.x*b.y + b.x*a.y + a.x*c.y + c.x*a.y + b.x*c.y + c.x*b.y);
            float exz = a.x*a.z + b.x*b.z + c.x*c.z + 0.5f*(a.x*b.z + b.x*a.z + a.x*c.z + c.x*a.z + b.x*c.z + c.x*b.z);
            float eyz = a.y*a.z + b.y*b.z + c.y*c.z + 0.5f*(a.y*b.z + b.y*a.z + a.y*c.z + c.y*a.z + b.y*c.z + c.y*b.z);

            Ixx += v * (eyy + ezz) / 10.0f;
            Iyy += v * (exx + ezz) / 10.0f;
            Izz += v * (exx + eyy) / 10.0f;
            Ixy -= v * exy / 10.0f;
            Ixz -= v * exz / 10.0f;
            Iyz -= v * eyz / 10.0f;
        }

        Ixx *= this.density;
        Iyy *= this.density;
        Izz *= this.density;
        Ixy *= this.density;
        Ixz *= this.density;
        Iyz *= this.density;

        Vector3 cm = this.center_mass;
        float m = this.mass;

        Ixx -= m * (cm.y * cm.y + cm.z * cm.z);
        Iyy -= m * (cm.x * cm.x + cm.z * cm.z);
        Izz -= m * (cm.x * cm.x + cm.y * cm.y);
        Ixy += m * (cm.x * cm.y);
        Ixz += m * (cm.x * cm.z);
        Iyz += m * (cm.y * cm.z);

        Matrix3 diag = Matrix3.diagonal(Ixx, Iyy, Izz);

        Matrix3 mXy = Matrix3.outer(new Vector3(1, 1, 0), Ixy).add(Matrix3.diagonal(-Ixy, -Ixy, 0));
        Matrix3 mXz = Matrix3.outer(new Vector3(1, 0, 1), Ixz).add(Matrix3.diagonal(-Ixz, 0, -Ixz));
        Matrix3 mYz = Matrix3.outer(new Vector3(0, 1, 1), Iyz).add(Matrix3.diagonal(0, -Iyz, -Iyz));

        return diag.add(mXy).add(mXz).add(mYz);
    }

    // retorna a quantidade de vertices na malha
    public int getVertexCount() {
        return vertex.length;
    }

    // retorna a quantidade de triangulos na malha
    public int getTriangleCount() {
        return triangles.length;
    }
} // Mesh