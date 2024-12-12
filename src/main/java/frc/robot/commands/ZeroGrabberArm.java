package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.GrabberConfig;
import frc.robot.subsystems.Grabber;

public class ZeroGrabberArm extends Command {
  private Grabber grabber;

  /**
   * Pick up a tote.
   * 
   * @param grabber
   */
  public ZeroGrabberArm(Grabber grabber) {
    this.grabber = grabber;
    addRequirements(grabber);
  }

  @Override
  public void initialize() {
    grabber.unzero();
    grabber.setMotorSpeed(-GrabberConfig.GRABBER_MOTOR_SPEED);
  }

  @Override
  public void end(boolean interrupted) {
    grabber.setMotorSpeed(0);
    if (grabber.getSwitch()) {
      grabber.zero();
    }
  }

  @Override
  public boolean isFinished() {
    return grabber.getSwitch();
  }
}
