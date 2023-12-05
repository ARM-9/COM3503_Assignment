import gmaths.*;
import com.jogamp.opengl.*;
  
public class LightModel extends Model {

  private Vec3 position;
  private Vec3 directionPoint;
  private float direction; // Angle from centre that the spotlight is pointing in
  private float cutoff;
  private boolean on = true;
    
  public LightModel(String name, Mesh mesh, Mat4 modelMatrix, Shader shader, Material material, Camera camera) {
    this(name, mesh, modelMatrix, shader, material, camera, 0, 0);
  }

  public LightModel(String name, Mesh mesh, Mat4 modelMatrix, Shader shader, Material material, Camera camera, float direction, float cutoff) {
    super(name, mesh, modelMatrix, shader, material, camera);
    position = new Vec3();
    toggleOnOff(); // Turn lights off and set A/D/S values
    this.cutoff = cutoff;
    this.direction = direction;
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

  public void setDirectionPoint(Vec3 dirPoint) {
    this.directionPoint = dirPoint;
  }

  public Vec3 getDirectionPoint() {
    return directionPoint;
  }

  public void setDirection(float dir) {
    direction = dir;
  }

  public float getDirection() {
    return direction;
  }

  public void setCutoff(float cutoff) {
    this.cutoff = cutoff;
  }
  
  public float getCutoff() {
    return cutoff;
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

  public void render(GL3 gl, Mat4 modelMatrix) {
    if (mesh_null()) {
      System.out.println("Error: null in model render");
      return;
    }

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
  
}