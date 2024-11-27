package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.DrivetrainConfig;

public class Controls {
  CommandXboxController driver, operator;
  private boolean slowmode;

  // If a value from a joystick is less than this, it will return 0.
  private double threshold = Constants.AXIS_THRESHOLD;

  public Controls(CommandXboxController driver, CommandXboxController operator) {
    this.driver = driver;
    this.operator = operator;
    slowmode = false;
  }

  /**
   * Returns the left joystick's Y-axis value.
   */
  public double getDriveSpeedY() {
    double joystickY = MathUtil.applyDeadband(driver.getLeftY(), threshold);

    return joystickY * (slowmode ? DrivetrainConfig.SLOWMODE_MULTIPLIER : 1);
  }

  /**
   * Returns the left joystick's X-axis value.
   */
  public double getDriveSpeedX() {
    double joystickX = MathUtil.applyDeadband(driver.getLeftX(), threshold);

    return joystickX * (slowmode ? DrivetrainConfig.SLOWMODE_MULTIPLIER : 1);
  }

  /**
   * Returns the right joystick's X-axis value.
   */
  public double getTurnSpeed() {
    double joystickTurn = MathUtil.applyDeadband(driver.getRightX(), threshold);

    return joystickTurn * (slowmode ? DrivetrainConfig.SLOWMODE_MULTIPLIER : 1);
  }

  public boolean getSlowmode() {
    return slowmode;
  }

  public boolean setSlowmode(boolean newMode) {
    slowmode = newMode;
    return slowmode;
  }

  public Command toggleSlowmode() {
    return new InstantCommand(() -> setSlowmode(!slowmode));
  }
}
