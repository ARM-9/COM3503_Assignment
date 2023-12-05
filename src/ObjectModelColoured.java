import gmaths.*;
import com.jogamp.opengl.*;

public class ObjectModelColoured extends ObjectModel {
  
  private Vec3 colour;

  public ObjectModelColoured() {
    super();

    this.colour = null;
  }
  
  public ObjectModelColoured(String name, Mesh mesh, Mat4 modelMatrix, Shader shader, Material material, Camera camera, LightModel firstLight, LightModel secondLight, LightModel spotlight, Vec3 colour) {
    super(name, mesh, modelMatrix, shader, material, camera, firstLight, secondLight, spotlight);

    this.colour = colour;
  }

  public void setColour(Vec3 c) {
    this.colour = c;
  }

  // second version of render is so that modelMatrix can be overriden with a new parameter
  public void render(GL3 gl, Mat4 modelMatrix) {
    if (mesh_null()) {
      System.out.println("Error: null in model render");
      return;
    }

    Mat4 mvpMatrix = Mat4.multiply(camera.getPerspectiveMatrix(), Mat4.multiply(camera.getViewMatrix(), modelMatrix));

    // set shader variables. Be careful that these variables exist in the shader

    shader.use(gl);

    shader.setFloatArray(gl, "model", modelMatrix.toFloatArrayForGLSL());
    shader.setFloatArray(gl, "mvpMatrix", mvpMatrix.toFloatArrayForGLSL());
    
    shader.setVec3(gl, "viewPos", camera.getPosition());

    shader.setVec3(gl, "first_light.position", firstLight.getPosition());
    shader.setVec3(gl, "first_light.ambient", firstLight.getMaterial().getAmbient());
    shader.setVec3(gl, "first_light.diffuse", firstLight.getMaterial().getDiffuse());
    shader.setVec3(gl, "first_light.specular", firstLight.getMaterial().getSpecular());

    shader.setVec3(gl, "second_light.position", secondLight.getPosition());
    shader.setVec3(gl, "second_light.ambient", secondLight.getMaterial().getAmbient());
    shader.setVec3(gl, "second_light.diffuse", secondLight.getMaterial().getDiffuse());
    shader.setVec3(gl, "second_light.specular", secondLight.getMaterial().getSpecular());

    shader.setVec3(gl, "spotlight.light.position", spotlight.getPosition());
    shader.setVec3(gl, "spotlight.light.ambient", spotlight.getMaterial().getAmbient());
    shader.setVec3(gl, "spotlight.light.diffuse", spotlight.getMaterial().getDiffuse());
    shader.setVec3(gl, "spotlight.light.specular", spotlight.getMaterial().getSpecular());
    shader.setVec3(gl, "spotlight.direction", spotlight.getDirectionPoint());
    shader.setFloat(gl, "spotlight.cutoff", spotlight.getCutoff());

    shader.setVec3(gl, "material.ambient", material.getAmbient());
    shader.setVec3(gl, "material.diffuse", material.getDiffuse());
    shader.setVec3(gl, "material.specular", material.getSpecular());
    shader.setFloat(gl, "material.shininess", material.getShininess());

    shader.setVec3(gl, "colour", colour);

    // then render the mesh
    mesh.render(gl);
  }
  
}