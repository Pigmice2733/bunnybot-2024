package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Grabber;

public class DropTote extends SequentialCommandGroup {
  /**
   * Drops a tote that the grabber is holding.
   * 
   * @param grabber
   */
  public DropTote(Grabber grabber) {
    addCommands(
        grabber.lowerGrabber(),
        new WaitCommand(2),
        grabber.openFinger(),
        grabber.stop());
    addRequirements(grabber);
  }
}
