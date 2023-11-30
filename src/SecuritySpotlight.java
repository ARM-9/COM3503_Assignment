import gmaths.*;

import com.jogamp.opengl.*;
import com.jogamp.opengl.util.texture.*;

 /**
 * This class stores the Robot
 *
 * @author    Dr Steve Maddock
 * @version   1.0 (31/08/2022)
 */

public class SecuritySpotlight {

  private Camera camera;
  private Light light1;
  private Light light2;
  private Light spotlight;

  private Model sphere;

  private SGNode spotlightRoot;
  private TransformNode headSpin;
   
  public SecuritySpotlight(GL3 gl, Camera camera, Light light1, Light light2, Light spotlight, Texture t1, Texture t2, Vec3 pos) {
    this.camera = camera;
    this.light1 = light1;
    this.light2 = light2;
    this.spotlight = spotlight;

    sphere = makeSphere(gl, t1,t2);
    
    float poleLength = 8f;
    Vec3 poleScale = new Vec3(0.5f, poleLength, 0.5f);
    float headLength = 2f;
    float headWidth = 0.5f;
    Vec3 headScale = new Vec3(headWidth, headWidth, headLength);
    float headTiltAngle = 30f;
    Vec3 lightScale = new Vec3(headWidth/2, headWidth/2, headWidth/2);
    
    Mat4 m;

    // Root
    spotlightRoot = new NameNode("root");
    m = Mat4Transform.translate(pos);
    TransformNode spotlightTranslate = new TransformNode("position spotlight", m);

    // Pole

    NameNode pole = new NameNode("root");
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(poleScale));
    m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode poleTransform = new TransformNode("position pole", m);
    ModelNode poleShape = new ModelNode("Sphere(pole)", sphere);

    // Head

    TransformNode wholeHeadTranslate = new TransformNode("position whole head", Mat4Transform.translate(0, poleLength, 0));
    headSpin = new TransformNode("spin head", Mat4Transform.rotateAroundY(0));
    TransformNode tiltHead = new TransformNode("tilt head", Mat4Transform.rotateAroundX(headTiltAngle));

    NameNode head = new NameNode("head");
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(headScale));
    m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
    TransformNode headTransform = new TransformNode("head transform", m);
    ModelNode headShape = new ModelNode("Sphere(head)", sphere);

    m = Mat4Transform.translate(0, 0, headLength/2);
    TransformNode lightTranslate = new TransformNode("position light", m);
    
    NameNode light = new NameNode("light");
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.translate(0, headWidth/2, 0));
    m = Mat4.multiply(m, Mat4Transform.scale(lightScale));
    //m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
    TransformNode lightTransform = new TransformNode("light transform", m);
    ModelNode lightShape = new ModelNode("Sphere(light)", sphere);

    // Scene graph hierarchy
    spotlightRoot.addChild(spotlightTranslate);
    spotlightTranslate.addChild(pole);
    pole.addChild(poleTransform);
      poleTransform.addChild(poleShape);
    pole.addChild(wholeHeadTranslate);
      wholeHeadTranslate.addChild(headSpin);
      headSpin.addChild(tiltHead);
      tiltHead.addChild(head);
      head.addChild(headTransform);
        headTransform.addChild(headShape);
      head.addChild(lightTranslate);
        lightTranslate.addChild(light);
        light.addChild(lightTransform);
        lightTransform.addChild(lightShape);
    
    spotlightRoot.update();  // IMPORTANT - don't forget this

  }

  private Model makeSphere(GL3 gl, Texture t1, Texture t2) {
    String name= "sphere";
    Mesh mesh = new Mesh(gl, Sphere.vertices, Sphere.indices);
    Shader shader = new Shader(gl, "shaders/vs_standard.txt", "shaders/fs_standard_2t.txt");
    Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    Mat4 modelMatrix = new Mat4(1);
    Model sphere = new Model(name, mesh, modelMatrix, shader, material, light1, light2, spotlight, camera, t1, t2);
    return sphere;
  } 

  public void render(GL3 gl) {
    spotlightRoot.draw(gl);
  }

  public void spinAnimation(double elapsedTime) {
    float rotateAngle = 180 * (float)Math.sin(elapsedTime);
    headSpin.setTransform(Mat4Transform.rotateAroundY(rotateAngle));
    headSpin.update();
  }

  public void dispose(GL3 gl) {
    sphere.dispose(gl);
  }
}
