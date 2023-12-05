import gmaths.*;

import java.util.HashMap;
import java.util.Map;
import com.jogamp.opengl.*;
  
public class A_GLEventListener implements GLEventListener {
  
  private static final boolean DISPLAY_SHADERS = false;
    
  public A_GLEventListener(Camera camera) {
    this.camera = camera;
    this.camera.setPosition(new Vec3(4f,12f,18f));
  }
  
  // ***************************************************
  /*
   * METHODS DEFINED BY GLEventListener
   */

  /* Initialisation */
  public void init(GLAutoDrawable drawable) {   
    GL3 gl = drawable.getGL().getGL3();
    System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
    gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); 
    gl.glClearDepth(1.0f);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(GL.GL_LESS);
    gl.glFrontFace(GL.GL_CCW);    // default is 'CCW'
    gl.glEnable(GL.GL_CULL_FACE); // default is 'not enabled'
    gl.glCullFace(GL.GL_BACK);   // default is 'back', assuming CCW
    initialise(gl);
  }
  
  /* Called to indicate the drawing surface has been moved and/or resized  */
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    GL3 gl = drawable.getGL().getGL3();
    gl.glViewport(x, y, width, height);
    float aspect = (float)width/(float)height;
    camera.setPerspectiveMatrix(Mat4Transform.perspective(45, aspect));
  }

  /* Draw */
  public void display(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    render(gl);
  }

  /* Clean up memory, if necessary */
  public void dispose(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    light1.dispose(gl);
    light2.dispose(gl);
    floor.dispose(gl);
    alien1.dispose(gl);
    alien2.dispose(gl);
    securitySpotlight.dispose(gl);
  }
  
  
  // ***************************************************
  /* INTERACTION
   *
   *
   */
  private Map<String, Animation> animations = new HashMap<>();

  private void initialiseAnimations() {
    // Alien 1
    animations.put("a1rb", new Animation((et -> alien1.rockBodyAnimation(et))));
    animations.put("a1rh", new Animation(et -> alien1.rollHeadAnimation(et)));
    animations.put("a1wa", new Animation(et -> alien1.waveArmsAnimation(et)));
    animations.put("a1fe", new Animation(et -> alien1.flapEarsAnimation(et)));
    animations.put("a1sa", new Animation(et -> alien1.spinAntennaAnimation(et)));
    // Alien 2
    animations.put("a2rb", new Animation((et -> alien2.rockBodyAnimation(et))));
    animations.put("a2rh", new Animation(et -> alien2.rollHeadAnimation(et)));
    animations.put("a2wa", new Animation(et -> alien2.waveArmsAnimation(et)));
    animations.put("a2fe", new Animation(et -> alien2.flapEarsAnimation(et)));
    animations.put("a2sa", new Animation(et -> alien2.spinAntennaAnimation(et)));
    // Spotlight
    animations.put("sp", new Animation(et -> securitySpotlight.spinAnimation(et)));
  }

  public void toggleAnimation(String animationKey) {
    if ( animationKey.equals("sp") ) {
      securitySpotlight.getBulbModel().toggleOnOff();
    }

    Animation animation = animations.get(animationKey);
    animation.toggleMoving();
    if ( animation.getMoving() ) {
      animation.setStart(getSeconds() - animation.getStop());
    } else {
      animation.setStop(getSeconds() - animation.getStart());
    }
  }

  public void resetAlien1() {
    animations.get("a1rb").setMoving(false);
    animations.get("a1rh").setMoving(false);
    animations.get("a1wa").setMoving(false);
    animations.get("a1fe").setMoving(false);
    animations.get("a1sa").setMoving(false);
    alien1.resetPosition();
  }

  public void resetAlien2() {
    animations.get("a2rb").setMoving(false);
    animations.get("a2rh").setMoving(false);
    animations.get("a2wa").setMoving(false);
    animations.get("a2fe").setMoving(false);
    animations.get("a2sa").setMoving(false);
    alien2.resetPosition();
  }

  public void toggleGlobalLights() {
    light1.getLightModel().toggleOnOff();
    light2.getLightModel().toggleOnOff();
  }

  
  // ***************************************************
  /* THE SCENE
   * Now define all the methods to handle the scene.
   * This will be added to in later examples.
   */

  // textures
  private TextureLibrary textures;

  private Camera camera;
  private Mat4 perspective;
  private ObjectModel floor;
  
  private Alien alien1;
  private Alien alien2;
  private GlobalLight light1;
  private GlobalLight light2;
  private SecuritySpotlight securitySpotlight;

  private void initialise(GL3 gl) {
    createRandomNumbers();

    textures = new TextureLibrary();
    textures.add(gl, "jade_diffuse", "textures/jade.jpg");
    textures.add(gl, "jade_specular", "textures/jade_specular.jpg");

    light1 = new GlobalLight(gl, camera, new Vec3(5, 10, 5));
    light2 = new GlobalLight(gl, camera, new Vec3(-5, 10, 5));

    toggleGlobalLights(); // Turning on global lights so scene isn't pitch black
    
    securitySpotlight = new SecuritySpotlight(gl, camera, light1.getLightModel(), light2.getLightModel(),
                                   textures.get("jade_diffuse"), textures.get("jade_specular"),
                                  new Vec3(0, 0, -2));

    // floor
    String name = "floor";
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "shaders/vs_moving.txt", "shaders/fs_moving_2t.txt");
    Material material = new Material(new Vec3(0.0f, 0.5f, 0.81f), new Vec3(0.0f, 0.5f, 0.81f), new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
    Mat4 modelMatrix = Mat4Transform.scale(16,1f,16);
    floor = new ObjectModelTwoTex(name, mesh, modelMatrix, shader, material, camera, light1.getLightModel(), light2.getLightModel(), securitySpotlight.getBulbModel(), textures.get("jade_diffuse"), textures.get("jade_specular"));
    
    alien1 = new Alien(gl, camera, light1.getLightModel(), light2.getLightModel(), securitySpotlight.getBulbModel(),
                      textures.get("jade_diffuse"), textures.get("jade_specular"), // body
                      textures.get("jade_diffuse"), textures.get("jade_specular"), // head
                      textures.get("jade_diffuse"), textures.get("jade_specular"), // arm
                      textures.get("jade_diffuse"), textures.get("jade_specular"), // ear
                      -3.5f);
    
    alien2 = new Alien(gl, camera, light1.getLightModel(), light2.getLightModel(), securitySpotlight.getBulbModel(),
                      textures.get("jade_diffuse"), textures.get("jade_specular"), // body
                      textures.get("jade_diffuse"), textures.get("jade_specular"), // head
                      textures.get("jade_diffuse"), textures.get("jade_specular"), // arm
                      textures.get("jade_diffuse"), textures.get("jade_specular"), // ear
                      3.5f);
    
    initialiseAnimations();
  }
 
  private void render(GL3 gl) {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    light1.render(gl);
    light2.render(gl);
    floor.render(gl);

    for (Animation a : animations.values()) {
      if ( a.getMoving() ) {
        a.getMovement().accept(getSeconds() - a.getStart());
      }
    }

    alien1.render(gl);
    alien2.render(gl);
    securitySpotlight.render(gl);
  }
  
  // ***************************************************
  /* TIME
   */ 
  
  private double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }

  // ***************************************************
  /* An array of random numbers
   */ 
  
  private int NUM_RANDOMS = 1000;
  private float[] randoms;
  
  private void createRandomNumbers() {
    randoms = new float[NUM_RANDOMS];
    for (int i=0; i<NUM_RANDOMS; ++i) {
      randoms[i] = (float)Math.random();
    }
  }
  
}