import com.jogamp.opengl.GL3;

import gmaths.Mat4;

public abstract class Model {
    private String name;
    private Mesh mesh;
    private Mat4 modelMatrix;
    private Shader shader;
    private Material material;
    private Camera camera;

    public abstract void render(GL3 gl);
    
    public abstract void render(GL3 gl, Mat4 modelMatrix);

    public abstract void dispose(GL3 gl);
}
