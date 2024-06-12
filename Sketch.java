import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;
import gifAnimation.*;

  /* Todo:
   * Add game win/lose conditions (energy <= 0 for too long and anomalies escape)
   * do something with employees (maybe make them die with a respawn timer when a task is failed)
   */

public class Sketch extends PApplet {

  // Game states
  int intState = 0; // 0 for menu, 1 for game, 2 for tutorial, 3 for task menu

  // Button dimensions
  int intButtonWidth = 200;
  int intButtonHeight = 75;

  // Entities
  ArrayList<Employee> lstEmployees = new ArrayList<>();
  ArrayList<Anomaly> lstAnomalies = new ArrayList<>();
  ArrayList<ContainmentUnit> lstContainmentUnits = new ArrayList<>();
  ArrayList<Employee> employees = new ArrayList<Employee>();

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
  int taskCompletedTime = 0;
  /**
   * Initializes the game entities and sets up the game environment.
   */
  public void setup() {
    // Initialize entities
    lstEmployees.add(new Employee(this, 100, 100));
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
    employees.add(new Employee(this, 100, 100));
    employees.add(new Employee(this, 200, 100));
    employees.add(new Employee(this, 300, 100));
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
   *
   * @author Austin L
   */
  public void drawMenu() {
    background(backMain);
    fill(50);
    textSize(48);
    textAlign(CENTER, CENTER);
    fill(255); // Set the fill color for the rectangle
    rect(width / 2 - 250, height / 8 - 30, 500, 60); // Increase the rectangle size
    fill(0); // Set the fill color for the text
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
    if (taskCompleted) {
      if (taskCompletedTime == 0) { // Record the completion time if not already recorded
          taskCompletedTime = millis();
      }

      background(0); // Set background color to black
      fill(255); // Set text color to white
      textSize(32); // Set text size
      textAlign(CENTER, CENTER);
      text(taskCompletedMessage, width / 2, height / 2); // Display the completion message

      if (millis() - taskCompletedTime > 2000) { // Check if 2 seconds have passed
          taskCompleted = false; // Reset the task completion flag
          taskCompletedTime = 0; // Reset the completion time for the next use
      }
    } else {
        taskCompletedTime = 0;
        background(backGame);
        fill(0);
        textSize(32);
        textAlign(CENTER, CENTER);
        fill(255); // Set the fill color for the rectangle
        rect(width / 2 - 130, 30, 260, 35); // Decrease the size of the rectangle for "Game Screen"
        rect(width - 230, 30, 230, 35); // Decrease the size of the rectangle for "Energy"
        fill(0); // Set the fill color for the text
        text("Game Screen", width / 2, 50);
        text("Energy: " + intEnergy, width - 150, 50);

        // Draw entities
        for (Employee employee : lstEmployees) {
            employee.display();
        }
        for (ContainmentUnit containmentUnit : lstContainmentUnits) {
            containmentUnit.display();
        }
        // Add this loop to display anomalies
        for (Anomaly anomaly : lstAnomalies) {
            anomaly.display();
        }
          // Display all employees
          for (Employee emp : employees) {
            emp.display();
        }
        // Draw back button
        drawButton("Back", 70, 45);
    }
}

  /**
   * This method is responsible for drawing the tutorial screen.
   * TBD
   */
  public void drawTutorial() {
    background(255, 200, 150);
    fill(0);
    textSize(32);
    textAlign(CENTER, CENTER);
    text("Tutorial Screen", width / 2, height / 2);

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

  boolean taskCompleted = false;
  String taskCompletedMessage = "";
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
        int energyPointsChange = 0; // Initialize the variable to track energy points change

        if (strTaskType.equals("Research")) {
            blnSuccess = random(1) > 0.5;
            energyPointsChange = blnSuccess ? 10 : -5;
        } else if (strTaskType.equals("Repression")) {
            blnSuccess = random(1) > 0.3;
            energyPointsChange = blnSuccess ? 15 : -25;
        }

        intEnergy += energyPointsChange; // Update the energy based on the task outcome
        taskCompletedMessage = strTaskType + " task was " + (blnSuccess ? "a success! You gained " : "failed. You lost ") + Math.abs(energyPointsChange) + " energy points.";
        taskCompleted = true;
        if (!blnSuccess) {
          //employee.startFlashing(); // Assuming 'employee' is an instance of Employee
      }
    }

}

  /**
   * Represents an employee in the game.
   * Each employee is represented as a blue circle on the game screen.
   */
  class Employee {
    PApplet p;
    float fltX, fltY;
    boolean isFlashing = false;
    int flashStartTime;
    static final int FLASH_DURATION = 30000; // 30 seconds in milliseconds

    Employee(PApplet p, float fltX, float fltY) {
        this.p = p;
        this.fltX = fltX;
        this.fltY = fltY;
    }

    void startFlashing() {
        isFlashing = true;
        flashStartTime = p.millis();
    }

    void update() {
        if (isFlashing && p.millis() - flashStartTime > FLASH_DURATION) {
            isFlashing = false; // Stop flashing after 30 seconds
        }
    }

    void display() {
        if (isFlashing) {
            // Flash by toggling visibility every half second
            if ((p.millis() / 500) % 2 == 0) {
                p.fill(0, 0, 255);
                p.ellipse(fltX, fltY, 20, 20);
            }
        } else {
            p.fill(0, 0, 255);
            p.ellipse(fltX, fltY, 20, 20);
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

    /**
     * Displays the anomaly on the screen.
     * The anomaly is represented as a red square.
     * If the anomaly's energy is less than 0, the square's transparency oscillates/flashes.
     */
    void display() {
      if (intEnergy < 0) {
        p.fill(255, 0, 0, (int) (abs(PApplet.sin((float) (p.frameCount * 0.2))) * 255));
      } else {
        p.fill(255, 0, 0);
      }
      p.rect(fltX, fltY, 30, 30);
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