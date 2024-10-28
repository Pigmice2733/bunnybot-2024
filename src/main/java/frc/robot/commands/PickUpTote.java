package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.GrabberConfig;
import frc.robot.subsystems.Grabber;

public class PickUpTote extends SequentialCommandGroup {
  /**
   * Pick up a tote.
   * 
   * @param grabber
   */
  public PickUpTote(Grabber grabber) {
    addCommands(
        grabber.closeFinger(),
        new InstantCommand(() -> grabber.setMotorSpeed(GrabberConfig.GRABBER_MOTOR_SPEED), grabber),
        new WaitCommand(2),
        new InstantCommand(() -> grabber.stopMotor(), grabber));
    addRequirements(grabber);
  }
}
