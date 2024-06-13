import processing.core.PApplet;
import processing.core.PImage;
import java.util.*;
import gifAnimation.*;


@SuppressWarnings("unused")
public class Sketch extends PApplet {

  // Game states
  int intState = 0; // 0 for menu, 1 for game, 2 for tutorial, 3 for task menu

  // Button dimensions
  int intButtonWidth = 200;
  int intButtonHeight = 75;

  // Entities
  ArrayList<Anomaly> lstAnomalies = new ArrayList<>();
  ArrayList<ContainmentUnit> lstContainmentUnits = new ArrayList<>();

  // Selected containment unit
  ContainmentUnit selectedUnit = null;

  // Energy management
  int intEnergy = 0;

  public static void main(String[] args) {
    PApplet.main("Sketch");
  }

  /**
   * Sets the size of the window
   */
  public void settings() {
    size(800, 600);
  }

  PImage backMain;
  PImage backGame;
  PImage containUnit;
  int intTaskCompletedTime = 0;

  /**
   * Initializes the game entities and sets up the game environment.
   */
  public void setup() {
    ContainmentUnit cu1 = new ContainmentUnit(this, 400, 300);
    ContainmentUnit cu2 = new ContainmentUnit(this, 600, 300);
    lstAnomalies.add(new Anomaly(this, 400, 300, "Anomaly 1", cu1));
    lstAnomalies.add(new Anomaly(this, 600, 300, "Anomaly 2", cu2));
    lstContainmentUnits.add(cu1);
    lstContainmentUnits.add(cu2);
    backMain = loadImage("Background Malkuth.png");
    backMain.resize(width, height);
    backGame = loadImage("Background Images\\Background Tipereth.png");
    backGame.resize(width, height);
    containUnit = loadImage("Containment Unit.png");
    containUnit.resize(150, 150);
  }

  /**
   * Draws the game based on the current state, the numbers correspond to which screen is being displayed.
   */
  public void draw() {
    if (intState == 0) {
      drawMenu();
    } else if (intState == 1) {
      drawGame();
    } else if (intState == 2) {
      drawTutorial();
    } else if (intState == 3) {
      drawTaskMenu();
    }
  }

  /**
   * Intializes the layout, sets up the buttons, and handles user interaction for the main menu.
   */
  public void drawMenu() {
    background(backMain);
    fill(50);
    textSize(48);
    textAlign(CENTER, CENTER);
    fill(255);
    rect(width / 2 - 250, height / 8 - 30, 500, 60);
    fill(0);
    text("Anomaly Corporation", width / 2, height / 8);

    // Draw buttons
    drawButton("Play", width / 2, height / 2 - 50);
    drawButton("Tutorial", width / 2, height / 2 + 50);
  }

  /**
   * This method is responsible for drawing the game screen.
   * It sets the background color, displays the game title, and shows the player's current energy.
   * It also iterates through the lists of employees, containment units, and anomalies, calling their display methods to draw them on the screen.
   * It draws a "Back" button that allows the player to return to the previous screen.
   */
  public void drawGame() {
    if (blnTaskCompleted) {
      if (intTaskCompletedTime == 0) { // Record the completion time if not already recorded
          intTaskCompletedTime = millis();
      }

      background(0);
      fill(255);
      textSize(32);
      textAlign(CENTER, CENTER);
      text(strTaskCompletedMessage, width / 2, height / 2); // Display the completion message

      if (millis() - intTaskCompletedTime > 2000) { // Check if 2 seconds have passed
          blnTaskCompleted = false; // Reset the task completion flag
          intTaskCompletedTime = 0; // Reset the completion time for the next use
      }
    } else {
        intTaskCompletedTime = 0;
        background(backGame);
        fill(0);
        textSize(32);
        textAlign(CENTER, CENTER);
        fill(255);
        rect(width / 2 - 130, 30, 260, 35);
        rect(width - 230, 30, 230, 35);
        fill(0);
        text("Game Screen", width / 2, 50);
        text("Energy: " + intEnergy, width - 150, 50);

        for (ContainmentUnit containmentUnit : lstContainmentUnits) {
            containmentUnit.display();
        }

        for (Anomaly anomaly : lstAnomalies) {
            anomaly.display();
        }
        // Draw back button
        drawButton("Back", 70, 45);
    }
  }



  /**
   * This method is responsible for drawing the tutorial screen.
   * It sets the background color, displays the tutorial title, and provides detailed instructions on how to play the game.
   * The text size is adjusted to make the instructions clear and easy to follow.
   * The instructions are stored in an array of strings for easy access and modification.
   * The starting y position for the instructions is calculated based on the height of the screen and the number of instructions.
   */
  public void drawTutorial() {
    background(255, 200, 150); // Set background color
    fill(0); // Set text color
    textSize(24); // Adjust text size for better readability

    // Title
    textAlign(CENTER, CENTER);
    text("Game Tutorial", width / 2, height / 2 - 200);

    // Reset text alignment for instructions
    textAlign(LEFT, CENTER);
    textSize(17);

    // Instructions array
    String[] instructions = {
        "Welcome to Anomaly Corporation!",
        "As the Director of this facility, you are tasked with containing various anomalies within containment units.",
        "You can perform tasks to manage the anomalies and earn energy points.",
        "If you run out of energy, the anomalies will breach containment and you will lose the game.",
        "Click the 'Back' button to start the game.",
        "Good luck!"
    };

    // Calculate starting y position for instructions
    int startY = height / 2 - 150;
    int stepY = 30; // Step in y direction for each instruction

    // Loop through instructions and display them
    for (int i = 0; i < instructions.length; i++) {
      text(instructions[i], 20, startY + (i * stepY)); // Changed width / 4 to 20
  }

    // Draw back button
    drawButton("Back", 70, 45);
}

  /**
   * This method is responsible for drawing the task menu screen.
   * It sets the background color, displays the menu title, and draws buttons for each task.
   * Each task button displays the task name and its success rate.
   * When a task button is clicked, the performTask method is called to simulate the task outcome.
   * The player's energy is updated based on the task outcome.
   * The player can also return to the game screen by clicking the "Back" button.
   */
  public void drawTaskMenu() {
    background(210, 255, 173);
    fill(50);
    textSize(32);
    textAlign(CENTER, CENTER);
    text("Task Menu", width / 2, height / 4);

    // Draw task buttons
    drawButton("Research - 50% Success", width / 2, height / 2 - 50);
    drawButton("Repression - 70% Success", width / 2, height / 2 + 50);

    // Draw back button
    drawButton("Back", 70, 45);
  }

  /**
   * This method is responsible for drawing a button on the screen.
   * It first draws a rectangle at the specified coordinates, then displays the provided label centered within the rectangle.
   *
   * @param strLabel The label to be displayed on the button.
   * @param intX The x-coordinate of the center of the button.
   * @param intY The y-coordinate of the center of the button.
   */
  public void drawButton(String strLabel, int intX, int intY) {
    fill(255);
    rect(intX - intButtonWidth / 2, intY - intButtonHeight / 2, intButtonWidth, intButtonHeight);
    fill(0);
    textSize(32);
    textAlign(CENTER, CENTER);
    text(strLabel, intX, intY);
  }

  /**
   * This method is called whenever the mouse is pressed.
   * Depending on the current state of the game (represented by intState), it checks if certain buttons are clicked and performs the corresponding actions.
   *
   * If the game is in the main menu (intState == 0), it checks if the game start button or the tutorial button is clicked.
   * If the game is in the game screen (intState == 1), it checks if the back button is clicked or if a containment unit is clicked.
   * If the game is in the tutorial screen (intState == 2), it checks if the back button is clicked.
   * If the game is in the task menu screen (intState == 3), it checks if the "Instinct" task button, the "Repression" task button, or the back button is clicked.
   */
  public void mousePressed() {
    if (intState == 0) {
      if (isButtonClicked(width / 2, height / 2 - 50)) {
        intState = 1; // Switch to game screen
      } else if (isButtonClicked(width / 2, height / 2 + 50)) {
        intState = 2; // Switch to tutorial screen
      }
    } else if (intState == 1) {
      if (isButtonClicked(70, 45)) {
        intState = 0; // Back to menu
        intEnergy = 0; // Reset energy
      } else {
        // Check if a containment unit is clicked
        for (ContainmentUnit containmentUnit : lstContainmentUnits) {
          if (containmentUnit.isClicked(mouseX, mouseY)) {
            selectedUnit = containmentUnit;
            intState = 3; // Switch to task menu
            break;
          }
        }
      }
    } else if (intState == 2) {
      if (isButtonClicked(70, 45)) {
        intState = 1; // Back to game screen
      }
    } else if (intState == 3) {
      if (isButtonClicked(width / 2, height / 2 - 50)) {
        performTask("Research");
        intState = 1; // Back to game screen
      } else if (isButtonClicked(width / 2, height / 2 + 50)) {
        performTask("Repression");
        intState = 1; // Back to game screen
      } else if (isButtonClicked(70, 45)) {
        intState = 1; // Back to game screen
      }
    }
  }

  /**
   * Checks if a button at the given coordinates was clicked.
   * The button's width and height are definedo by the intButtonWidth and intButtonHeight variables.
   * It checks if the mouse's current x and y coordinates (mouseX and mouseY) are within the area of the button.
   *
   * @param intX The x-coordinate of the center of the button.
   * @param intY The y-coordinate of the center of the button.
   * @return true if the button was clicked, false otherwise.
   */
  public boolean isButtonClicked(int intX, int intY) {
    return mouseX > intX - intButtonWidth / 2 && mouseX < intX + intButtonWidth / 2 &&
           mouseY > intY - intButtonHeight / 2 && mouseY < intY + intButtonHeight / 2;
  }

  boolean blnTaskCompleted = false;
  String strTaskCompletedMessage = "";
  /**
   * Performs a task of the given type on the selected containment unit.
   * If the task type is "Research", there is a 50% chance of success. On success, 10 energy is added. On failure, 5 energy is subtracted.
   * If the task type is "Repression", there is a 70% chance of success. On success, 15 energy is added. On failure, 25 energy is subtracted.
   *
   * @param strTaskType The type of the task to perform. Can be "Research" or "Repression".
   */
  public void performTask(String strTaskType) {
    if (selectedUnit != null) {
        boolean blnSuccess = false;
        int intEnergyPointsChange = 0;

        if (strTaskType.equals("Research")) {
            blnSuccess = random(1) > 0.5;
            intEnergyPointsChange = blnSuccess ? 1000 : -5;
        } else if (strTaskType.equals("Repression")) {
            blnSuccess = random(1) > 0.3;
            intEnergyPointsChange = blnSuccess ? 15 : -205;
        }

        intEnergy +=intEnergyPointsChange; // Update the energy based on the task outcome
        strTaskCompletedMessage = strTaskType + " task was " + (blnSuccess ? "a success! You gained " : "failed. You lost ") + Math.abs(intEnergyPointsChange) + " energy points.";
        blnTaskCompleted = true;
        if (!blnSuccess) {
          //employee.startFlashing(); // Assuming 'employee' is an instance of Employee
      }
    }
}

  /**
   * Represents an anomaly in the game.
   * Each anomaly is represented as a red square on the game screen.
   * Anomalies are contained within containment units and can move randomly within the bounds of their containment unit.
   */
  class Anomaly {
    PApplet p;
    float fltX, fltY;
    String strName;
    ContainmentUnit containmentUnit;

    /**
     * Constructs a new Anomaly object.
     *
     * @param p The PApplet instance used to draw the anomaly.
     * @param fltX The x-coordinate of the anomaly.
     * @param fltY The y-coordinate of the anomaly.
     * @param strName The name of the anomaly.
     * @param containmentUnit The containment unit in which the anomaly is contained.
     */
    Anomaly(PApplet p, float fltX, float fltY, String strName, ContainmentUnit containmentUnit) {
      this.p = p;
      this.fltX = fltX;
      this.fltY = fltY;
      this.strName = strName;
      this.containmentUnit = containmentUnit;
    }

long energyDropTime = -1;
/**
 * Displays the anomaly on the screen.
 * The anomaly is represented as a red square.
 * If the energy drops below zero, a warning message is displayed.
 * If the energy remains below zero for more than 10 seconds, a game over message is displayed.
 * The anomaly moves randomly within the bounds of its containment unit.
 * If the energy exceeds 500, a win message is displayed.
 * The anomaly is constrained to stay within its containment unit
 */
@SuppressWarnings("static-access")
void display() {
  // Check if energy is above 500 to display the win screen
  if (intEnergy > 500) {
    p.background(0); // Set background to black
    p.fill(255); // Set text color to white
    p.textSize(32); // Increase text size for visibility
    String winMessage = "Congratulations! You have contained all the anomalies!";
    p.textAlign(p.CENTER, p.CENTER); // Center the text
    p.text(winMessage, p.width / 2, p.height / 2); // Display the message in the center
    return; // Skip the rest of the display logic
  }

  String strWarningText = "WARNING: Low Energy"; // Existing variable for the warning text
  if (intEnergy < 0) {
    if (energyDropTime == -1) { // Energy just dropped below zero
      energyDropTime = System.currentTimeMillis();
    }
    long timeBelowZero = System.currentTimeMillis() - energyDropTime;
    if (timeBelowZero > 10000) { // More than 10 seconds
      p.background(0); // Set background to black
      p.fill(255); // Set text color to white
      p.textSize(32); // Increase text size for visibility
      String message = "The anomalies breached containment. Game Over.";
      p.textAlign(p.CENTER, p.CENTER); // Center the text
      p.text(message, p.width / 2, p.height / 2); // Display the message in the center
      return; // Skip drawing the anomaly
    }
    p.fill(255, 0, 0, (int) (abs(PApplet.sin((float) (p.frameCount * 0.2))) * 255));
    if (PApplet.sin((float) (p.frameCount * 0.2)) > 0) {
      p.fill(0); // Set fill color for background, black
      p.rect(p.width - p.textWidth(strWarningText) - 20, 80, p.textWidth(strWarningText) + 20, 40);
      p.fill(255, 0, 0); // Set fill color for text, red
      p.textSize(24); // Set the text size
      p.text(strWarningText, p.width - 200, 100); // Position the text on the right
    }
  } else {
    energyDropTime = -1; // Reset the timer if energy is back above zero
    p.fill(255, 0, 0);
  }
  p.rect(fltX, fltY, 30, 30); // Draw the anomaly
  move();
}

    /**
     * Moves the anomaly randomly within the bounds of its containment unit.
     */
    void move() {
      fltX += p.random(-1, 1);
      fltY += p.random(-1, 1);
      // Ensure the anomaly doesn't move out of its containment unit
      fltX = PApplet.constrain(fltX, containmentUnit.fltX - 40, containmentUnit.fltX + 40);
      fltY = PApplet.constrain(fltY, containmentUnit.fltY - 40, containmentUnit.fltY + 40);
    }
  }

  /**
   * Represents a containment unit in the game.
   * Each containment unit is represented as a gray square on the game screen.
   * Containment units can contain anomalies and are clickable.
   */
  class ContainmentUnit {
    PApplet p;
    float fltX, fltY;

    /**
     * Constructs a new ContainmentUnit object.
     *
     * @param p The PApplet instance used to draw the containment unit.
     * @param fltX The x-coordinate of the containment unit.
     * @param fltY The y-coordinate of the containment unit.
     */
    ContainmentUnit(PApplet p, float fltX, float fltY) {
      this.p = p;
      this.fltX = fltX;
      this.fltY = fltY;
    }

    /**
     * Displays the containment unit on the screen.
     * The containment unit is represented as a gray square.
     */
    void display() {
      image(containUnit, fltX - 40, fltY - 40, 150, 150); // Display the image
    }

    /**
     * Checks if the containment unit was clicked.
     * It checks if the mouse's current x and y coordinates (intMX and intMY) are within the area of the containment unit.
     *
     * @param intMX The x-coordinate of the mouse click.
     * @param intMY The y-coordinate of the mouse click.
     * @return true if the containment unit was clicked, false otherwise.
     */
    boolean isClicked(int intMX, int intMY) {
      return intMX > fltX - 75 && intMX < fltX + 75 && intMY > fltY - 75 && intMY < fltY + 75;
    }
  }
}