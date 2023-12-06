import gmaths.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.texture.*;

public class ObjectModelOneTex extends ObjectModel {
  
  private Texture firstTexture;

  public ObjectModelOneTex() {
    super();

    this.firstTexture = null;
  }
  
  public ObjectModelOneTex(String name, Mesh mesh, Mat4 modelMatrix, Shader shader, Material material, Camera camera, LightModel firstLight, LightModel secondLight, LightModel spotlight, Texture texture) {
    super(name, mesh, modelMatrix, shader, material, camera, firstLight, secondLight, spotlight);

    this.firstTexture = texture;
  }

  public void setFirstTexture(Texture t) {
    this.firstTexture = t;
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

    // If there is a mismatch between the number of textures the shader expects and the number we try to set here, then there will be problems.
    // Assumption is the user supplied the right shader and the right number of textures for the model

    if (firstTexture!=null) {
      shader.setInt(gl, "first_texture", 0);
      gl.glActiveTexture(GL.GL_TEXTURE);
      firstTexture.bind(gl);
    }

    // then render the mesh
    mesh.render(gl);
  }
  
}