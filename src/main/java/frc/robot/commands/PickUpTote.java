package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
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
        grabber.raiseGrabber(),
        new WaitCommand(2),
        grabber.stop());
    addRequirements(grabber);
  }
}
