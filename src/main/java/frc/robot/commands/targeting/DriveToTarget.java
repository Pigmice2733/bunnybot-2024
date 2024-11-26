package frc.robot.commands.targeting;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Vision;

public class DriveToTarget extends SequentialCommandGroup {
  public DriveToTarget(Drivetrain drivetrain, Vision vision) {
    addCommands(
        // turn until target is in view
        new FindTarget(drivetrain, vision),

        // point camera in perpendicular direction to target surface
        new TurnToTarget(drivetrain, vision),

        // move laterally until TX is 0 -- directly in front of target
        new CenterOnTarget(drivetrain, vision),

        // move forward until target is out of view -- a couple of feet in front of
        // target
        new GetToTarget(drivetrain, vision));
    addRequirements(drivetrain, vision);
  }

}
