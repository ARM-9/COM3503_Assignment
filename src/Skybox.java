import gmaths.*;

import com.jogamp.opengl.*;
import com.jogamp.opengl.util.texture.Texture;

 /**
 * This class stores the Robot
 *
 * @author    Dr Steve Maddock
 * @version   1.0 (31/08/2022)
 */

public class Skybox {

  private Camera camera;
  private LightModel light1;
  private LightModel light2;
  private LightModel spotlight;

  private ObjectModelMovingTwoTex rearWall;
  private ObjectModelMovingTwoTex leftWall;
  private ObjectModelMovingTwoTex rightWall;
  private ObjectModelMovingTwoTex frontWall;
  private ObjectModelMovingOneTex skyWall;
  private ObjectModelColoured floorWall;

  private SGNode skyboxRoot;
   
  public Skybox(GL3 gl, Camera camera, LightModel light1, LightModel light2, LightModel spotlight, Texture t1, Texture t2, Texture t3, Texture t4) {
    this.camera = camera;
    this.light1 = light1;
    this.light2 = light2;
    this.spotlight = spotlight;

    rearWall = makeWallTwoTex(gl, t1, t4);
    leftWall = makeWallTwoTex(gl, t2, t4);
    rightWall = makeWallTwoTex(gl, t2, t4);
    frontWall = makeWallTwoTex(gl, t1, t4);
    skyWall = makeWallOneTex(gl, t3);
    floorWall = makeWallColoured(gl, new Vec3(0.68f, 1, 1));

    float skyboxScale = 80;
    
    Mat4 m;

    // Root
    skyboxRoot = new NameNode("root");

    // rear wall
    m = Mat4Transform.translate(0, skyboxScale/2, skyboxScale/2);
    m = Mat4.multiply(m, Mat4Transform.rotateAroundY(180));
    m = Mat4.multiply(m, Mat4Transform.rotateAroundX(90));
    m = Mat4.multiply(m, Mat4Transform.scale(skyboxScale, skyboxScale, skyboxScale));
    TransformNode rearWallTransform = new TransformNode("position rear wall", m);
    ModelNode rearWallShape = new ModelNode("Plane(rear wall)", rearWall);

    // left wall
    m = Mat4Transform.translate(-skyboxScale/2, skyboxScale/2, 0);
    m = Mat4.multiply(m, Mat4Transform.rotateAroundX(180));
    m = Mat4.multiply(m, Mat4Transform.rotateAroundY(-90));
    m = Mat4.multiply(m, Mat4Transform.rotateAroundX(-90));
    m = Mat4.multiply(m, Mat4Transform.scale(skyboxScale, skyboxScale, skyboxScale));
    TransformNode leftWallTransform = new TransformNode("position left wall", m);
    ModelNode leftWallShape = new ModelNode("Plane(left wall)", leftWall);

    // right wall
    m = Mat4Transform.translate(skyboxScale/2, skyboxScale/2, 0);
    m = Mat4.multiply(m, Mat4Transform.rotateAroundX(180));
    m = Mat4.multiply(m, Mat4Transform.rotateAroundY(90));
    m = Mat4.multiply(m, Mat4Transform.rotateAroundX(-90));
    m = Mat4.multiply(m, Mat4Transform.scale(skyboxScale, skyboxScale, skyboxScale));
    TransformNode rightWallTransform = new TransformNode("position right wall", m);
    ModelNode rightWallShape = new ModelNode("Plane(right wall)", rightWall);

    // front wall
    m = Mat4Transform.translate(0, skyboxScale/2, -skyboxScale/2);
    m = Mat4.multiply(m, Mat4Transform.rotateAroundZ(180));
    m = Mat4.multiply(m, Mat4Transform.rotateAroundY(180));
    m = Mat4.multiply(m, Mat4Transform.rotateAroundX(-90));
    m = Mat4.multiply(m, Mat4Transform.scale(skyboxScale, skyboxScale, skyboxScale));
    TransformNode frontWallTransform = new TransformNode("position front wall", m);
    ModelNode frontWallShape = new ModelNode("Plane(front wall)", frontWall);

    // sky wall
    m = Mat4Transform.translate(0, skyboxScale, 0);
    m = Mat4.multiply(m, Mat4Transform.rotateAroundX(180));
    m = Mat4.multiply(m, Mat4Transform.scale(skyboxScale, skyboxScale, skyboxScale));
    TransformNode skyWallTransform = new TransformNode("position sky wall", m);
    ModelNode skyWallShape = new ModelNode("Plane(sky wall)", skyWall);

    // floor wall
    m = Mat4Transform.scale(skyboxScale, skyboxScale, skyboxScale);
    TransformNode floorWallTransform = new TransformNode("position floor wall", m);
    ModelNode floorWallShape = new ModelNode("Plane(floor wall)", floorWall);

    // Scene graph hierarchy
    skyboxRoot.addChild(rearWallTransform);
      rearWallTransform.addChild(rearWallShape);
    skyboxRoot.addChild(leftWallTransform);
      leftWallTransform.addChild(leftWallShape);
    skyboxRoot.addChild(rightWallTransform);
      rightWallTransform.addChild(rightWallShape);
    skyboxRoot.addChild(frontWallTransform);
      frontWallTransform.addChild(frontWallShape);
    skyboxRoot.addChild(skyWallTransform);
      skyWallTransform.addChild(skyWallShape);
    skyboxRoot.addChild(floorWallTransform);
      floorWallTransform.addChild(floorWallShape);
    
    skyboxRoot.update();  // IMPORTANT - don't forget this

  }

  private ObjectModelMovingTwoTex makeWallTwoTex(GL3 gl, Texture t1, Texture t2) {
    String name = "wall";
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices, TwoTriangles.indices);
    Shader shader = new Shader(gl, "shaders/vs_moving.txt", "shaders/fs_moving_2t.txt");
    Material material = new Material();
    Mat4 modelMatrix = new Mat4(1);
    ObjectModelMovingTwoTex wall = new ObjectModelMovingTwoTex(name, mesh, modelMatrix, shader, material, camera, light1, light2, spotlight, t1, t2, 0, 0);
    return wall;
  }

  private ObjectModelMovingOneTex makeWallOneTex(GL3 gl, Texture t) {
    String name = "wall";
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices, TwoTriangles.indices);
    Shader shader = new Shader(gl, "shaders/vs_moving.txt", "shaders/fs_moving_1t.txt");
    Material material = new Material();
    Mat4 modelMatrix = new Mat4(1);
    ObjectModelMovingOneTex wall = new ObjectModelMovingOneTex(name, mesh, modelMatrix, shader, material, camera, light1, light2, spotlight, t, 0, 0);
    return wall;
  }
  
  private ObjectModelColoured makeWallColoured(GL3 gl, Vec3 colour) {
    String name = "wall";
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices, TwoTriangles.indices);
    Shader shader = new Shader(gl, "shaders/vs_standard.txt", "shaders/fs_standard_c.txt");
    Material material = new Material();
    Mat4 modelMatrix = new Mat4(1);
    ObjectModelColoured wall = new ObjectModelColoured(name, mesh, modelMatrix, shader, material, camera, light1, light2, spotlight, colour);
    return wall;
  }

  public void render(GL3 gl) {
    skyboxRoot.draw(gl);
  }

  public void moveTextures(double elapsedTime) {
    double t = 0.1 * elapsedTime;
    t -= Math.floor(t);
    //double t = 0;
    rearWall.setOffset(0.0f, (float)t);
    leftWall.setOffset(0.0f, (float)t);
    rightWall.setOffset(0.0f, (float)t);
    frontWall.setOffset(0.0f, (float)t);
    skyWall.setOffset((float)t, 0.0f);

    skyboxRoot.update();
  }

  public void dispose(GL3 gl) {
    rearWall.dispose(gl);
    leftWall.dispose(gl);
    rightWall.dispose(gl);
    frontWall.dispose(gl);
    skyWall.dispose(gl);
    floorWall.dispose(gl);
  }
}
