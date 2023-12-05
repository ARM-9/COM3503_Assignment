import gmaths.*;
import com.jogamp.opengl.*;

public abstract class ObjectModel extends Model {
  
  protected LightModel firstLight;
  protected LightModel secondLight;
  protected LightModel spotlight;

  public ObjectModel() {
    super();

    firstLight = null;
    secondLight = null;
    spotlight = null;
  }
  
  public ObjectModel(String name, Mesh mesh, Mat4 modelMatrix, Shader shader, Material material, Camera camera, LightModel firstLight, LightModel secondLight, LightModel spotlight) {
    super(name, mesh, modelMatrix, shader, material, camera);

    this.firstLight = firstLight;
    this.secondLight = secondLight;
    this.spotlight = spotlight;
  }

  public LightModel getFirstLight() {
    return this.firstLight;
  }

  public void setFirstLight(LightModel light) {
      this.firstLight = light;
  }

  public LightModel getSecondLight() {
    return this.secondLight;
  }

  public void setSecondLight(LightModel light) {
      this.secondLight = light;
  }

  public LightModel getSpotlight() {
    return this.spotlight;
  }
  
  public void setSpotlight(LightModel light) {
    this.spotlight = light;
  }
  
}