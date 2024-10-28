package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class Controls {
  CommandXboxController driver;
  CommandXboxController operator;

  // If a value from a joystick is less than this, it will return 0.
  private double threshold = Constants.AXIS_THRESHOLD;

  public Controls(CommandXboxController driver, CommandXboxController operator) {
    this.driver = driver;
    this.operator = operator;
  }

  /**
   * @return the left joystick y-axis multiplied by the drive speed. When the Y
   *         button is held, the result is multiplied by the slowmode multiplier
   *         before
   *         returning.
   */
  public double getDriveSpeedY() {
    double joystickY = MathUtil.applyDeadband(-driver.getLeftY(), threshold);

    return joystickY;
  }

  /**
   * @return the left joystick x-axis multiplied by the drive speed. When the Y
   *         button is held, the result is multiplied by the slowmode multiplier
   *         before
   *         returning.
   */
  public double getDriveSpeedX() {
    double joystickX = MathUtil.applyDeadband(-driver.getLeftX(), threshold);

    return joystickX;
  }

  /**
   * @return the right joystick x-axis multiplied by the drive speed. When the Y
   *         button is held, the result is multiplied by the slowmode multiplier
   *         before
   *         returning.
   */
  public double getTurnSpeed() {
    double joystickTurn = MathUtil.applyDeadband(driver.getRightX(), threshold);

    return -joystickTurn;
  }
}
