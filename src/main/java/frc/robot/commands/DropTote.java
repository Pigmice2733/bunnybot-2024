package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.GrabberConfig;
import frc.robot.subsystems.Grabber;

public class DropTote extends SequentialCommandGroup {
  /**
   * Drops a tote that the grabber is holding.
   * 
   * @param grabber
   */
  public DropTote(Grabber grabber) {
    addCommands(
        new InstantCommand(() -> grabber.setMotorSpeed(-1 * GrabberConfig.GRABBER_MOTOR_SPEED), grabber),
        new WaitCommand(2),
        grabber.openFinger(),
        new InstantCommand(() -> grabber.stopMotor(), grabber));
    addRequirements(grabber);
  }
}
