import gifAnimation.Gif;
import processing.core.PApplet;
import gifAnimation.*;

public class gifTesting extends PApplet {


  /**
   * Called once at the beginning of execution, put your size all in this method
   */
  public void settings() {
	// put your size call here
    size(400, 400);
  }
  Gif myAnimation;
  /**
   * Called once at the beginning of execution.  Add initial set up
   * values here i.e background, stroke, fill etc.
   */
public void setup() {
    size(400,400);
    myAnimation = new Gif(this, "u69a9ti3mdma1.gif");
    myAnimation.play();
    background(210, 255, 173);
    fill(100);
}

public void draw() {
    image(myAnimation, 10, 10);

}

public static void main(String[] args) {
    PApplet.main("gifTesting");
  }

  // define other methods down here.
}