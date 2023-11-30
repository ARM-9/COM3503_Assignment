import gmaths.*;
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
  
public class Light {
  
  private Material material;
  private Vec3 position;
  private Vec3 direction;
  private float cutoff;
  private Mesh mesh;
  private Shader shader;
  private Camera camera;
  private boolean on = true;
    
  public Light(GL3 gl) {
    this(gl, new Vec3(0, 0, 0), 0);
  }

  public Light(GL3 gl, Vec3 direction, float cutoff) {
    material = new Material();
    toggleOnOff(); // Turn lights off and set A/D/S values
    position = new Vec3(3f,2f,1f);
    this.direction = direction;
    this.cutoff = cutoff;
    mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
    shader = new Shader(gl, "shaders/vs_light_01.txt", "shaders/fs_light_01.txt");
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
    Mat4 model = new Mat4(1);
    model = Mat4.multiply(Mat4Transform.scale(0.3f,0.3f,0.3f), model);
    model = Mat4.multiply(Mat4Transform.translate(position), model);
    
    Mat4 mvpMatrix = Mat4.multiply(camera.getPerspectiveMatrix(), Mat4.multiply(camera.getViewMatrix(), model));
    
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