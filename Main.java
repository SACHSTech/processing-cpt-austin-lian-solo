import processing.core.PApplet;

/**
 * Main class to execute sketch
 * @author
 *
 */
class Main {
  public static void main(String[] args) {

    String[] processingArgs = {"MySketch"};
		Sketch mySketch = new Sketch();  //comment this out to run the other sketch files
	  // Sketch1 mySketch = new Sketch1();  // uncomment this to run this sketch file
	  // Sketch2 mySketch = new Sketch2();  // uncomment this to run this sketch file
	

	  PApplet.runSketch(processingArgs, mySketch);
  }
  
}
