import gmaths.*;

import com.jogamp.opengl.*;

 /**
 * This class stores the Robot
 *
 * @author    Dr Steve Maddock
 * @version   1.0 (31/08/2022)
 */

public class GlobalLight {

  private Camera camera;

  private LightModel sphere;
  private Mat4 lightPositionTranslate; // Possibly make an attr of lightmodel

  private SGNode lightRoot;
   
  public GlobalLight(GL3 gl, Camera camera, Vec3 pos) {
    this.camera = camera;

    sphere = makeSphere(gl);
    
    Mat4 m;

    // Root
    lightRoot = new NameNode("root");
    lightPositionTranslate = Mat4Transform.translate(pos);
    m = Mat4.multiply(lightPositionTranslate, Mat4Transform.scale(0.3f, 0.3f, 0.3f));
    m = Mat4.multiply(m, Mat4Transform.translate(0.0f, 0.5f, 0.0f));
    TransformNode lightTransform = new TransformNode("position light", m);
    ModelNode lightShape = new ModelNode("Sphere(light)", sphere);

    // Scene graph hierarchy
    lightRoot.addChild(lightTransform);
    lightTransform.addChild(lightShape);

    sphere.setPosition(new Vec3(Mat4.multiply(lightPositionTranslate, new Vec4())));
    
    lightRoot.update();  // IMPORTANT - don't forget this

  }

  private LightModel makeSphere(GL3 gl) {
    String name = "sphere";
    Mesh mesh = new Mesh(gl, Sphere.vertices, Sphere.indices);
    Shader shader = new Shader(gl, "shaders/vs_light_01.txt", "shaders/fs_light_01.txt");
    Material material = new Material();
    Mat4 modelMatrix = new Mat4(1.0f);
    LightModel sphere = new LightModel(name, mesh, modelMatrix, shader, material, camera);
    return sphere;
  }

  public LightModel getLightModel() {
    return sphere;
  }

  public void render(GL3 gl) {
    lightRoot.draw(gl);
  }

  public void dispose(GL3 gl) {
    sphere.dispose(gl);
  }
}
