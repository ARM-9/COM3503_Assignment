import gmaths.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.texture.*;

public class ObjectModel extends Model {
  
  private String name;
  private Mesh mesh;
  private Mat4 modelMatrix;
  private Shader shader;
  private Material material;
  private Camera camera;
  private LightModel firstLight;
  private LightModel secondLight;
  private LightModel spotlight;
  private Texture diffuse;
  private Texture specular;

  public ObjectModel() {
    name = null;
    mesh = null;
    modelMatrix = null;
    material = null;
    camera = null;
    firstLight = null;
    secondLight = null;
    spotlight = null;
    shader = null;
  }
  
  public ObjectModel(String name, Mesh mesh, Mat4 modelMatrix, Shader shader, Material material, LightModel firstLight, LightModel secondLight, LightModel spotlight, Camera camera, Texture diffuse, Texture specular) {
    this.name = name;
    this.mesh = mesh;
    this.modelMatrix = modelMatrix;
    this.shader = shader;
    this.material = material;
    this.firstLight = firstLight;
    this.secondLight = secondLight;
    this.spotlight = spotlight;
    this.camera = camera;
    this.diffuse = diffuse;
    this.specular = specular;
  }
  
  public ObjectModel(String name, Mesh mesh, Mat4 modelMatrix, Shader shader, Material material, LightModel firstLight, LightModel secondLight, LightModel spotlight, Camera camera, Texture diffuse) {
    this(name, mesh, modelMatrix, shader, material, firstLight, secondLight, spotlight, camera, diffuse, null);
  }
  
  public ObjectModel(String name, Mesh mesh, Mat4 modelMatrix, Shader shader, Material material, LightModel firstLight, LightModel secondLight, LightModel spotlight, Camera camera) {
    this(name, mesh, modelMatrix, shader, material, firstLight, secondLight, spotlight, camera, null, null);
  }

  public void setName(String s) {
    this.name = s;
  }

  public void setMesh(Mesh m) {
    this.mesh = m;
  }

  public void setModelMatrix(Mat4 m) {
    modelMatrix = m;
  }
  
  public void setMaterial(Material material) {
    this.material = material;
  }

  public void setShader(Shader shader) {
    this.shader = shader;
  }

  public void setCamera(Camera camera) {
    this.camera = camera;
  }
  
  public void setFirstLight(LightModel light) {
    this.firstLight = light;
  }

  public void setSecondLight(LightModel light) {
    this.secondLight = light;
  }
  
  public void setSpotlight(LightModel light) {
    this.spotlight = light;
  }

  public void setDiffuse(Texture t) {
    this.diffuse = t;
  }

  public void setSpecular(Texture t) {
    this.specular = t;
  }

  public void renderName(GL3 gl) {
    System.out.println("Name = "+name);  
  }

  public void render(GL3 gl) {
    render(gl, modelMatrix);
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

    if (diffuse!=null) {
      shader.setInt(gl, "first_texture", 0);  // be careful to match these with GL_TEXTURE0 and GL_TEXTURE1
      gl.glActiveTexture(GL.GL_TEXTURE0);
      diffuse.bind(gl);
    }
    if (specular!=null) {
      shader.setInt(gl, "second_texture", 1);
      gl.glActiveTexture(GL.GL_TEXTURE1);
      specular.bind(gl);
    }

    // then render the mesh
    mesh.render(gl);
  } 
  
  private boolean mesh_null() {
    return (mesh==null);
  }

  public void dispose(GL3 gl) {
    mesh.dispose(gl);  // only need to dispose of mesh
  }
  
}