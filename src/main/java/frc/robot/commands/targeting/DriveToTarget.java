package frc.robot.commands.targeting;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Vision;

public class DriveToTarget extends SequentialCommandGroup {
  /**
   * Drives from any place to the first target the camera sees. Composed of four
   * commands which each take a part of the process.
   * 
   * @param drivetrain
   * @param vision
   */
  public DriveToTarget(Drivetrain drivetrain, Vision vision) {
    addCommands(
        new FindTarget(drivetrain, vision),
        new TurnToTarget(drivetrain),
        new CenterOnTarget(drivetrain, vision),
        new GetToTarget(drivetrain, vision));
    addRequirements(drivetrain, vision);
  }

}
