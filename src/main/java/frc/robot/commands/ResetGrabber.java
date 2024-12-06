package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.GrabberConfig;
import frc.robot.subsystems.Grabber;

public class ResetGrabber extends Command {
  private Grabber grabber;

  /**
   * Lower the grabber until it reaches the limit switch.
   * 
   * @param grabber
   */
  public ResetGrabber(Grabber grabber) {
    this.grabber = grabber;

    addRequirements(grabber);
  }

  @Override
  public void initialize() {
    grabber.setMotorSpeed(-1 * GrabberConfig.GRABBER_MOTOR_SPEED);
  }

  @Override
  public void end(boolean interrupted) {
    grabber.stopMotor();
  }

  @Override
  public boolean isFinished() {
    return grabber.getSwitch();
  }
}
