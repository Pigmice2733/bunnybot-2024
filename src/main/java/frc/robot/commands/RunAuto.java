package frc.robot.commands;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.AutoConfig.AutoRoutine;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Grabber;
import frc.robot.subsystems.Indexer;

public class RunAuto extends SequentialCommandGroup {
  public RunAuto(Drivetrain drivetrain, Grabber grabber, Indexer indexer, AutoRoutine auto) {
    switch (auto) {
      case LEFT_CLOSE:
        addCommands(
            new InstantCommand(() -> drivetrain.getSwerve()
                .resetOdometry(PathPlannerPath.fromPathFile("leftClose").getPreviewStartingHolonomicPose())),
            AutoBuilder.followPath(PathPlannerPath.fromPathFile("leftClose")));
        break;
      case LEFT_MID:

        break;
      case LEFT_FAR:

        break;
      case RIGHT_CLOSE:

        break;
      case RIGHT_MID:

        break;
      case RIGHT_FAR:

        break;
      default:
        break;
    }
  }
}
