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
  private LightModel light1;
  private LightModel light2;
  private LightModel spotlight;

  private ObjectModel sphere;

  private SGNode spotlightRoot;
  private TransformNode wholeHeadTranslate;
  private TransformNode headSpin;
  private Mat4 lightPositionTranslate;
   
  public SecuritySpotlight(GL3 gl, Camera camera, LightModel light1, LightModel light2, Texture t1, Texture t2, Vec3 pos) {
    this.camera = camera;
    this.light1 = light1;
    this.light2 = light2;
    this.spotlight = makeLightbulb(gl);

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

    
    wholeHeadTranslate = new TransformNode("position whole head", Mat4Transform.translate(0, poleLength, 0));
    headSpin = new TransformNode("spin head", Mat4Transform.rotateAroundY(0));
    m = Mat4Transform.rotateAroundX(headTiltAngle);
    TransformNode tiltHead = new TransformNode("tilt head", m);
    lightPositionTranslate = m;

    NameNode head = new NameNode("head");
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(headScale));
    m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
    TransformNode headTransform = new TransformNode("head transform", m);
    ModelNode headShape = new ModelNode("Sphere(head)", sphere);

    m = Mat4Transform.translate(0, 0, headLength/2);
    TransformNode lightTranslate = new TransformNode("position light", m);
    lightPositionTranslate = Mat4.multiply(lightPositionTranslate, m);
    
    NameNode light = new NameNode("light");
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.translate(0, headWidth/2, 0));
    lightPositionTranslate = Mat4.multiply(lightPositionTranslate, m);
    m = Mat4.multiply(m, Mat4Transform.scale(lightScale));
    TransformNode lightTransform = new TransformNode("light transform", m);
    ModelNode lightShape = new ModelNode("Sphere(light)", spotlight);

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
    
    spotlight.setPosition(new Vec3(Mat4.multiply(lightPositionTranslate, new Vec4())));
    Mat4 positionDir = Mat4Transform.translate(0, -1, (float)Math.tan(Math.toRadians(spotlight.getDirection())));
    spotlight.setDirectionPoint(new Vec3(Mat4.multiply(Mat4.multiply(positionDir, lightPositionTranslate), new Vec4())));
    
    spotlightRoot.update();  // IMPORTANT - don't forget this

  }

  public LightModel getSpotlight() {
    return spotlight;
  }

  private ObjectModel makeSphere(GL3 gl, Texture t1, Texture t2) {
    String name= "sphere";
    Mesh mesh = new Mesh(gl, Sphere.vertices, Sphere.indices);
    Shader shader = new Shader(gl, "shaders/vs_standard.txt", "shaders/fs_standard_2t.txt");
    Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    Mat4 modelMatrix = new Mat4(1);
    ObjectModel sphere = new ObjectModel(name, mesh, modelMatrix, shader, material, light1, light2, spotlight, camera, t1, t2);
    return sphere;
  }

  private LightModel makeLightbulb(GL3 gl) {
    String name = "sphere";
    Mesh mesh = new Mesh(gl, Sphere.vertices, Sphere.indices);
    Shader shader = new Shader(gl, "shaders/vs_light_01.txt", "shaders/fs_light_01.txt");
    Material material = new Material();
    Mat4 modelMatrix = new Mat4(1.0f);
    LightModel sphere = new LightModel(name, mesh, modelMatrix, shader, material, camera, 15, (float)Math.cos(Math.toRadians(30)));
    return sphere;
  }

  public void render(GL3 gl) {
    spotlightRoot.draw(gl);
  }

  public void spinAnimation(double elapsedTime) {
    float rotateAngle = 180 * (float)Math.sin(elapsedTime);
    Mat4 m = Mat4Transform.rotateAroundY(rotateAngle);
    Mat4 positionDir = Mat4Transform.translate(0, -1, (float)Math.tan(Math.toRadians(spotlight.getDirection())));
    spotlight.setDirectionPoint(new Vec3(Mat4.multiply(Mat4.multiply(Mat4.multiply(wholeHeadTranslate.getTransform(), Mat4.multiply(m, positionDir)), lightPositionTranslate), new Vec4())));
    System.out.println(spotlight.getDirectionPoint());
    // Refactor
    Mat4 lightPosTrans = Mat4.multiply(Mat4.multiply(wholeHeadTranslate.getTransform(), m), lightPositionTranslate);

    headSpin.setTransform(m);
    spotlight.setPosition(new Vec3(Mat4.multiply(lightPosTrans, new Vec4())));
    headSpin.update();
  }

  public void dispose(GL3 gl) {
    sphere.dispose(gl);
  }
}
