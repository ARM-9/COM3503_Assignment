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

  private LightModel bulbModel;

  private ObjectModelColoured poleModel;
  private ObjectModelColoured headModel;

  private SGNode spotlightRoot;
  private TransformNode headTranslate;
  private TransformNode headSpin;

  private Mat4 bulbPositionTransform;
  private Mat4 directionPointTransform;
   
  public SecuritySpotlight(GL3 gl, Camera camera, LightModel light1, LightModel light2, Texture t1, Texture t2, Vec3 pos) {
    this.camera = camera;
    this.light1 = light1;
    this.light2 = light2;
    this.bulbModel = makeBulb(gl);

    poleModel = makeSphere(gl, new Vec3());
    headModel = makeSphere(gl, new Vec3());
    
    float poleLength = 8f;
    Vec3 poleScale = new Vec3(0.5f, poleLength, 0.5f);
    float headLength = 2f;
    float headWidth = 0.5f;
    Vec3 headScale = new Vec3(headWidth, headWidth, headLength);
    float headTiltAngle = 30;
    Vec3 bulbScale = new Vec3(headWidth/2, headWidth/2, headWidth/2);
    
    Mat4 m;

    // Root
    spotlightRoot = new NameNode("root");
    m = Mat4Transform.translate(pos);
    TransformNode spotlightTranslate = new TransformNode("position spotlight", m);

    // Pole

    NameNode pole = new NameNode("pole");
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(poleScale));
    m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode poleTransform = new TransformNode("position pole", m);
    ModelNode poleShape = new ModelNode("Sphere(pole)", poleModel);

    // Head
    
    headTranslate = new TransformNode("position head", Mat4Transform.translate(0, poleLength, 0));
    headSpin = new TransformNode("spin head", Mat4Transform.rotateAroundY(0));
    Mat4 headTilt = Mat4Transform.rotateAroundX(headTiltAngle);
    TransformNode tiltHead = new TransformNode("tilt head", headTilt);

    NameNode head = new NameNode("head");
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(headScale));
    m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
    TransformNode headTransform = new TransformNode("head transform", m);
    ModelNode headShape = new ModelNode("Sphere(head)", headModel);

    Mat4 translateBulbZ = Mat4Transform.translate(0, 0, headLength/2);
    TransformNode bulbTranslate = new TransformNode("position bulb", translateBulbZ);
    
    NameNode bulb = new NameNode("light");
    Mat4 translateBulbY = Mat4Transform.translate(0, headWidth/2, 0);
    Mat4 scaleBulb = Mat4Transform.scale(bulbScale);
    m = Mat4.multiply(translateBulbY, scaleBulb);
    TransformNode lightTransform = new TransformNode("light transform", m);
    ModelNode lightShape = new ModelNode("Sphere(light)", bulbModel);

    // Scene graph hierarchy
    spotlightRoot.addChild(spotlightTranslate);
    spotlightTranslate.addChild(pole);
    pole.addChild(poleTransform);
      poleTransform.addChild(poleShape);
    pole.addChild(headTranslate);
      headTranslate.addChild(headSpin);
      headSpin.addChild(tiltHead);
      tiltHead.addChild(head);
      head.addChild(headTransform);
        headTransform.addChild(headShape);
      head.addChild(bulbTranslate);
        bulbTranslate.addChild(bulb);
        bulb.addChild(lightTransform);
        lightTransform.addChild(lightShape);
    
    bulbPositionTransform = Mat4.multiply(Mat4.multiply(headTilt, translateBulbZ), translateBulbY);
    bulbModel.setPosition(applyTransform(calcBulbPosTransform()));

    directionPointTransform = Mat4Transform.translate(
      0, -1, (float)Math.tan(Math.toRadians(bulbModel.getDirection()))
    );
    bulbModel.setDirectionPoint(applyTransform(calcBulbDirPointTransform()));
    
    spotlightRoot.update();  // IMPORTANT - don't forget this

  }

  private Vec3 applyTransform(Mat4 transform) {
    return new Vec3(Mat4.multiply(transform, new Vec4()));
  }

  private Mat4 calcBulbPosTransform() {
    Mat4 transform = Mat4.multiply(
      Mat4.multiply(
        headTranslate.getTransform(),
        headSpin.getTransform()),
      bulbPositionTransform
    );
    return transform;
  }

  private Mat4 calcBulbDirPointTransform() {
    Mat4 transform = Mat4.multiply(
      Mat4.multiply(
        Mat4.multiply(
          headTranslate.getTransform(),
          headSpin.getTransform()),
        directionPointTransform),
      bulbPositionTransform
    );
    return transform;
  }

  public LightModel getBulbModel() {
    return bulbModel;
  }

  private ObjectModelColoured makeSphere(GL3 gl, Vec3 colour) {
    String name= "sphere";
    Mesh mesh = new Mesh(gl, Sphere.vertices, Sphere.indices);
    Shader shader = new Shader(gl, "shaders/vs_standard.txt", "shaders/fs_standard_2t.txt");
    Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    Mat4 modelMatrix = new Mat4(1);
    ObjectModelColoured sphere = new ObjectModelColoured(name, mesh, modelMatrix, shader, material, camera, light1, light2, bulbModel, colour);
    return sphere;
  }

  private LightModel makeBulb(GL3 gl) {
    String name = "bulb";
    Mesh mesh = new Mesh(gl, Sphere.vertices, Sphere.indices);
    Shader shader = new Shader(gl, "shaders/vs_light_01.txt", "shaders/fs_light_01.txt");
    Material material = new Material();
    Mat4 modelMatrix = new Mat4(1.0f);
    float cutoff = (float)Math.cos(Math.toRadians(30));
    LightModel bulb = new LightModel(name, mesh, modelMatrix, shader, material, camera, 15, cutoff);
    return bulb;
  }

  public void render(GL3 gl) {
    spotlightRoot.draw(gl);
  }

  public void spinAnimation(double elapsedTime) {
    float rotateAngle = 180 * (float)Math.sin(elapsedTime);
    headSpin.setTransform(Mat4Transform.rotateAroundY(rotateAngle));

    bulbModel.setPosition(applyTransform(calcBulbPosTransform()));
    bulbModel.setDirectionPoint(applyTransform(calcBulbDirPointTransform()));

    headSpin.update();
  }

  public void dispose(GL3 gl) {
    poleModel.dispose(gl);
    headModel.dispose(gl);
    bulbModel.dispose(gl);
  }

}
