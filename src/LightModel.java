import gmaths.*;
import com.jogamp.opengl.*;
  
public class LightModel extends Model {

  private String name;
  private Mesh mesh;
  private Mat4 modelMatrix;
  private Shader shader;
  private Material material;
  private Camera camera;
  private Vec3 position;
  private Vec3 direction;
  private float cutoff;
  private boolean on = true;
    
  public LightModel(String name, Mesh mesh, Mat4 modelMatrix, Shader shader, Material material, Camera camera) {
    this(name, mesh, modelMatrix, shader, material, camera, new Vec3(0, 0, 0), 0);
  }

  public LightModel(String name, Mesh mesh, Mat4 modelMatrix, Shader shader, Material material, Camera camera, Vec3 direction, float cutoff) {
    this.name = name;
    this.mesh = mesh;
    this.modelMatrix = modelMatrix;
    this.shader = shader;
    this.material = material;
    this.camera = camera;
    toggleOnOff(); // Turn lights off and set A/D/S values
    position = new Vec3(3f,2f,1f); // Calculate positions
    this.direction = direction; // Calculate direction
    this.cutoff = cutoff;
  }
  
  public void setPosition(Vec3 v) {
    position.x = v.x;
    position.y = v.y;
    position.z = v.z;
  }
  
  public void setPosition(float x, float y, float z) {
    position.x = x;
    position.y = y;
    position.z = z;
  }
  
  public Vec3 getPosition() {
    return position;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
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
  public void setShader(Shader shader) {
    this.shader = shader;
  }

  public Camera getCamera() {
    return this.camera;
  }

  public void setDirection(Vec3 dir) {
    direction = dir;
  }

  public Vec3 getDirection() {
    return direction;
  }

  public void setCutoff(float cutoff) {
    this.cutoff = cutoff;
  }
  
  public float getCutoff() {
    return cutoff;
  }

  public Mesh getMesh() {
    return mesh;
  }

  public Shader getShader() {
    return shader;
  }
  
  public void setMaterial(Material m) {
    material = m;
  }
  
  public Material getMaterial() {
    return material;
  }
  
  public void setCamera(Camera camera) {
    this.camera = camera;
  }

  public void toggleOnOff() {
    if ( on ) {
      on = false;
      material.setAmbient(0.0f, 0.0f, 0.0f);
      material.setDiffuse(0.0f, 0.0f, 0.0f);
      material.setSpecular(0.0f, 0.0f, 0.0f);
    } else {
      on = true;
      material.setAmbient(0.3f, 0.3f, 0.3f);
      material.setDiffuse(0.6f, 0.6f, 0.6f);
      material.setSpecular(0.6f, 0.6f, 0.6f);
    }
  }
  
  public void render(GL3 gl) {
    render(gl, modelMatrix);
  }

  public void render(GL3 gl, Mat4 modelMatrix) {
    Mat4 mvpMatrix = Mat4.multiply(camera.getPerspectiveMatrix(), Mat4.multiply(camera.getViewMatrix(), modelMatrix));
    
    shader.use(gl);
    shader.setFloatArray(gl, "mvpMatrix", mvpMatrix.toFloatArrayForGLSL());
    if ( on ) {
      shader.setVec3(gl, "colour", new Vec3(1.0f, 1.0f, 1.0f));
    } else {
      shader.setVec3(gl, "colour", new Vec3(1.0f, 1.0f, 0.0f));
    }
  
    mesh.render(gl);
  }

  public void dispose(GL3 gl) {
    mesh.dispose(gl);
  }
  
}