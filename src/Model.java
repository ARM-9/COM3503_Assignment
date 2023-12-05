import com.jogamp.opengl.GL3;

import gmaths.Mat4;

public abstract class Model {
    protected String name;
    protected Mesh mesh;
    protected Mat4 modelMatrix;
    protected Shader shader;
    protected Material material;
    protected Camera camera;

    public Model() {
        this.name = null;
        this.mesh = null;
        this.modelMatrix = null;
        this.shader = null;
        this.material = null;
        this.camera = null;
    }

    public Model(String name, Mesh mesh, Mat4 modelMatrix, Shader shader, Material material, Camera camera) {
        this.name = name;
        this.mesh = mesh;
        this.modelMatrix = modelMatrix;
        this.shader = shader;
        this.material = material;
        this.camera = camera;
    }
    
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Mesh getMesh() {
        return this.mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public Mat4 getModelMatrix() {
        return this.modelMatrix;
    }

    public void setModelMatrix(Mat4 modelMatrix) {
        this.modelMatrix = modelMatrix;
    }

    public Shader getShader() {
        return this.shader;
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }

    public Material getMaterial() {
        return this.material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Camera getCamera() {
        return this.camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void renderName(GL3 gl) {
        System.out.println("Name = "+name);  
    }

    public void render(GL3 gl) {
        render(gl, modelMatrix);
    }
    
    public abstract void render(GL3 gl, Mat4 modelMatrix);

    public void dispose(GL3 gl) {
        mesh.dispose(gl);  // only need to dispose of mesh
    }
    
    protected boolean mesh_null() {
        return (mesh==null);
    }
}
