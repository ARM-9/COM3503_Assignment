import gmaths.*;
 
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.texture.*;

 /**
 * This class stores the Robot
 *
 * @author    Dr Steve Maddock
 * @version   1.0 (31/08/2022)
 */

public class Alien {

  private Camera camera;
  private LightModel light1;
  private LightModel light2;
  private LightModel spotlight;

  private ObjectModelTwoTex bodyModel;
  private ObjectModelTwoTex headModel;
  private ObjectModelTwoTex armModel;
  private ObjectModelTwoTex earModel;
  private ObjectModelColoured eyeModel;
  private ObjectModelColoured antennaRodModel;
  private ObjectModelColoured antennaBallModel;


  private SGNode alienRoot;
  private TransformNode bodyRock, headRoll;
  private TransformNode leftArmRotate, rightArmRotate;
  private TransformNode leftEarRotate, rightEarRotate;
  private TransformNode antennaRotate;
   
  public Alien(GL3 gl, Camera camera, LightModel light1, LightModel light2, LightModel spotlight, Texture t1, Texture t2, Texture t3, Texture t4, Texture t5, Texture t6, Texture t7, Texture t8, float pos) {
    // TODO: Add a litt of textures as a parameter
    // Textures with specular maps may be part of a pair list
    // and singles may be part of another list

    this.camera = camera;
    this.light1 = light1;
    this.light2 = light2;
    this.spotlight = spotlight;

    bodyModel = makeSphere(gl, t1,t2);
    headModel = makeSphere(gl, t3,t4);
    armModel = makeSphere(gl, t5,t6);
    earModel = makeSphere(gl, t7,t8);
    eyeModel = makeSphere(gl, new Vec3(1, 1, 1));
    antennaRodModel = makeSphere(gl, new Vec3(1, 1, 1));
    antennaBallModel = makeSphere(gl, new Vec3(1, 1, 1));

    // alien
    
    float bodyScale = 3f;
    float headScale = 2f;
    float armBendAngle = 30f; // So that arms do not stick out completely straight
    float armLength = 2f;
    Vec3 armScale = new Vec3(armLength, 0.8f, 0.8f);
    float eyeOffset = headScale/4; // How far eyes are offset from centre of head
    float eyeScale = 0.5f;
    float earLength = 2f;
    Vec3 earScale = new Vec3(0.5f, earLength, 0.5f);
    float antennaLength = 1.25f;
    Vec3 antennaScale = new Vec3(0.25f, antennaLength, 0.25f);
    float antennaBallScale = 0.4f;
    
    Mat4 m;

    // Root

    alienRoot = new NameNode("root");
    TransformNode bodyUpTranslate = new TransformNode("position body (scene)", Mat4Transform.translate(pos, bodyScale/2, 0));
    bodyRock = new TransformNode("rock body", Mat4Transform.rotateAroundZ(0));
    TransformNode bodyDownTranslate = new TransformNode("position body (origin)", Mat4Transform.translate(0, -(bodyScale/2), 0));

    // Body

    NameNode body = new NameNode("body");
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(bodyScale, bodyScale, bodyScale));
    m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode bodyTransform = new TransformNode("position body", m);
    ModelNode bodyShape = new ModelNode("Sphere(body)", bodyModel);

    // Arms

    TransformNode leftArmTranslate = new TransformNode("position left arm", Mat4Transform.translate(-(bodyScale/2), (bodyScale/2), 0));
    leftArmRotate = new TransformNode("rotate left arm", Mat4Transform.rotateAroundX(0));
    TransformNode leftArmBend = new TransformNode("bend left arm", Mat4Transform.rotateAroundZ(-armBendAngle));

    NameNode leftArm = new NameNode("left arm");
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.translate(-(armLength/2), 0, 0));
    m = Mat4.multiply(m, Mat4Transform.scale(armScale));
    TransformNode leftArmTransform = new TransformNode("position left arm", m);
    ModelNode leftArmShape = new ModelNode("Sphere(left arm)", armModel);

    TransformNode rightArmTranslate = new TransformNode("position right arm", Mat4Transform.translate(bodyScale/2, (bodyScale/2), 0));
    rightArmRotate = new TransformNode("rotate right arm", Mat4Transform.rotateAroundX(0));
    TransformNode rightArmBend = new TransformNode("bend right arm", Mat4Transform.rotateAroundZ(armBendAngle));

    NameNode rightArm = new NameNode("right arm");
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.translate(armLength/2, 0, 0));
    m = Mat4.multiply(m, Mat4Transform.scale(armScale));
    TransformNode rightArmTransform = new TransformNode("position right arm", m);
    ModelNode rightArmShape = new ModelNode("Sphere(right arm)", armModel);
    
    // Head

    TransformNode headTopTranslate = new TransformNode("position head (body top)", Mat4Transform.translate(0, bodyScale/2, 0));
    headRoll = new TransformNode("roll head", Mat4Transform.rotateAroundZ(0));
    TransformNode headCentreTranslate = new TransformNode("position head (body centre)", Mat4Transform.translate(0, bodyScale/2, 0));
    
    NameNode head = new NameNode("head"); 
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(headScale, headScale, headScale));
    m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
    TransformNode headTransform = new TransformNode("position head", m);
    ModelNode headShape = new ModelNode("Sphere(head)", headModel);

    // Eyes

    TransformNode leftEyeTranslate = new TransformNode("position left eye", Mat4Transform.translate(-eyeOffset, headScale/2, headScale/2.5f));

    NameNode leftEye = new NameNode("left eye");
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(eyeScale, eyeScale, eyeScale));
    m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
    TransformNode leftEyeTransform = new TransformNode("position left eye", m);
    ModelNode leftEyeShape = new ModelNode("Sphere(left eye)", eyeModel);
    
    TransformNode rightEyeTranslate = new TransformNode("position right eye", Mat4Transform.translate(eyeOffset, headScale/2, headScale/2.5f));

    NameNode rightEye = new NameNode("right eye");
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(eyeScale, eyeScale, eyeScale));
    m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
    TransformNode rightEyeTransform = new TransformNode("position right eye", m);
    ModelNode rightEyeShape = new ModelNode("Sphere(right eye)", eyeModel);

    // Ears

    TransformNode leftEarTranslate = new TransformNode("position left ear", Mat4Transform.translate(-(headScale/2), headScale/2, 0));
    leftEarRotate = new TransformNode("rotate left ear", Mat4Transform.rotateAroundX(0));

    NameNode leftEar = new NameNode("left ear");
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(earScale));
    m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
    TransformNode leftEarTransform = new TransformNode("left ear transform", m);
    ModelNode leftEarShape = new ModelNode("Sphere(left ear)", earModel);

    TransformNode rightEarTranslate = new TransformNode("position right ear", Mat4Transform.translate(headScale/2, headScale/2, 0));
    rightEarRotate = new TransformNode("rotate right ear", Mat4Transform.rotateAroundX(0));

    NameNode rightEar = new NameNode("right ear");
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(earScale)); // Todo add ear scale attr
    m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
    TransformNode rightEarTransform = new TransformNode("right ear transform", m);
    ModelNode rightEarShape = new ModelNode("Sphere(right ear)", earModel);

    // Antenna

    TransformNode antennaTranslate = new TransformNode("position antenna", Mat4Transform.translate(0, headScale, 0));
    antennaRotate = new TransformNode("rotate antenna", Mat4Transform.rotateAroundZ(0));

    NameNode antennaRod = new NameNode("Antenna rod");
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(antennaScale));
    m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
    TransformNode antennaRodTransform = new TransformNode("antenna rod transform", m);
    ModelNode antennaRodShape = new ModelNode("Sphere(antenna rod)", antennaRodModel);

    m = Mat4Transform.translate(0, antennaLength, 0);
    TransformNode antennaBallTranslate = new TransformNode("position antenna ball", m);
    
    NameNode antennaBall = new NameNode("Antenna ball");
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(antennaBallScale, antennaBallScale, antennaBallScale));
    m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
    TransformNode antennaBallTransform = new TransformNode("antenna ball transform", m);
    ModelNode antennaBallShape = new ModelNode("Sphere(antenna ball)", antennaBallModel);

    // Scene graph hierarchy

    alienRoot.addChild(bodyUpTranslate);
      bodyUpTranslate.addChild(bodyRock);
      bodyRock.addChild(bodyDownTranslate);
      bodyDownTranslate.addChild(body);
      body.addChild(bodyTransform);
        bodyTransform.addChild(bodyShape);
      body.addChild(leftArmTranslate);
        leftArmTranslate.addChild(leftArmRotate);
        leftArmRotate.addChild(leftArmBend);
        leftArmBend.addChild(leftArm);
        leftArm.addChild(leftArmTransform);
        leftArmTransform.addChild(leftArmShape);
      body.addChild(rightArmTranslate);
        rightArmTranslate.addChild(rightArmRotate);
        rightArmRotate.addChild(rightArmBend);
        rightArmBend.addChild(rightArm);
        rightArm.addChild(rightArmTransform);
        rightArmTransform.addChild(rightArmShape);
      body.addChild(headTopTranslate);
        headTopTranslate.addChild(headRoll);
        headRoll.addChild(headCentreTranslate);
        headCentreTranslate.addChild(head);
          head.addChild(headTransform);
          headTransform.addChild(headShape);
        headCentreTranslate.addChild(leftEyeTranslate);
          leftEyeTranslate.addChild(leftEye);
          leftEye.addChild(leftEyeTransform);
          leftEyeTransform.addChild(leftEyeShape);
        headCentreTranslate.addChild(rightEyeTranslate);
          rightEyeTranslate.addChild(rightEye);
          rightEye.addChild(rightEyeTransform);
          rightEyeTransform.addChild(rightEyeShape);
        headCentreTranslate.addChild(leftEarTranslate);
          leftEarTranslate.addChild(leftEarRotate);
          leftEarRotate.addChild(leftEar);
          leftEar.addChild(leftEarTransform);
          leftEarTransform.addChild(leftEarShape);
        headCentreTranslate.addChild(rightEarTranslate);
          rightEarTranslate.addChild(rightEarRotate);
          rightEarRotate.addChild(rightEar);
          rightEar.addChild(rightEarTransform);
          rightEarTransform.addChild(rightEarShape);
        headCentreTranslate.addChild(antennaTranslate);
          antennaTranslate.addChild(antennaRotate);
          antennaRotate.addChild(antennaRod);
            antennaRod.addChild(antennaRodTransform);
            antennaRodTransform.addChild(antennaRodShape);
          antennaRotate.addChild(antennaBallTranslate);
            antennaBallTranslate.addChild(antennaBall);
            antennaBall.addChild(antennaBallTransform);
            antennaBallTransform.addChild(antennaBallShape); 
    
    alienRoot.update();  // IMPORTANT - don't forget this

  }

  private ObjectModelTwoTex makeSphere(GL3 gl, Texture t1, Texture t2) {
    String name= "sphere";
    Mesh mesh = new Mesh(gl, Sphere.vertices, Sphere.indices);
    Shader shader = new Shader(gl, "shaders/vs_standard.txt", "shaders/fs_standard_2t.txt");
    Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    Mat4 modelMatrix = new Mat4(1);
    ObjectModelTwoTex sphere = new ObjectModelTwoTex(name, mesh, modelMatrix, shader, material, camera, light1, light2, spotlight, t1, t2);
    return sphere;
  }

  private ObjectModelColoured makeSphere(GL3 gl, Vec3 colour) {
    String name= "sphere";
    Mesh mesh = new Mesh(gl, Sphere.vertices, Sphere.indices);
    Shader shader = new Shader(gl, "shaders/vs_standard.txt", "shaders/fs_standard_c.txt");
    Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    Mat4 modelMatrix = new Mat4(1);
    ObjectModelColoured sphere = new ObjectModelColoured(name, mesh, modelMatrix, shader, material, camera, light1, light2, spotlight, colour);
    return sphere;
  }

  public void render(GL3 gl) {
    alienRoot.draw(gl);
  }

  public void resetPosition() {
    bodyRock.setTransform(Mat4Transform.rotateAroundZ(0));
    bodyRock.update();
    leftArmRotate.setTransform(Mat4Transform.rotateAroundX(0));
    rightArmRotate.setTransform(Mat4Transform.rotateAroundX(0));
    leftArmRotate.update();
    rightArmRotate.update();
    headRoll.setTransform(Mat4Transform.rotateAroundZ(0));
    headRoll.update();
    leftEarRotate.setTransform(Mat4Transform.rotateAroundX(0));
    rightEarRotate.setTransform(Mat4Transform.rotateAroundX(0));
    leftEarRotate.update();
    rightEarRotate.update();
    leftEarRotate.setTransform(Mat4Transform.rotateAroundX(0));
    rightEarRotate.setTransform(Mat4Transform.rotateAroundX(0));
    leftEarRotate.update();
    rightEarRotate.update();
    antennaRotate.setTransform(Mat4Transform.rotateAroundZ(0));
    antennaRotate.update();
  }

  public void rockBodyAnimation(double elapsedTime) {
    float rotateAngle = 30f*(float)Math.sin(elapsedTime);
    bodyRock.setTransform(Mat4Transform.rotateAroundZ(rotateAngle));
    bodyRock.update();
  }

  public void waveArmsAnimation(double elapsedTime) {
    float rotateAngle = 60f*(float)Math.sin(elapsedTime);
    leftArmRotate.setTransform(Mat4Transform.rotateAroundX(rotateAngle));
    rightArmRotate.setTransform(Mat4Transform.rotateAroundX(rotateAngle));
    leftArmRotate.update();
    rightArmRotate.update();
  }

  public void rollHeadAnimation(double elapsedTime) {
    float rotateAngle = 30f*(float)Math.sin(elapsedTime);
    headRoll.setTransform(Mat4Transform.rotateAroundZ(rotateAngle));
    headRoll.update();
  }

  public void flapEarsAnimation(double elapsedTime) {
    float rotateAngle = 30f*(float)Math.sin(elapsedTime);
    leftEarRotate.setTransform(Mat4Transform.rotateAroundX(rotateAngle));
    rightEarRotate.setTransform(Mat4Transform.rotateAroundX(rotateAngle));
    leftEarRotate.update();
    rightEarRotate.update();
  }

  public void spinAntennaAnimation(double elapsedTime) {
    float rotateAngle = 30f*(float)Math.sin(elapsedTime);
    antennaRotate.setTransform(Mat4Transform.rotateAroundZ(rotateAngle));
    antennaRotate.update();
  }

  public void dispose(GL3 gl) {
    bodyModel.dispose(gl);
    headModel.dispose(gl);
    armModel.dispose(gl);
    earModel.dispose(gl);
    eyeModel.dispose(gl);
    antennaRodModel.dispose(gl);
    antennaBallModel.dispose(gl);
  }
}
