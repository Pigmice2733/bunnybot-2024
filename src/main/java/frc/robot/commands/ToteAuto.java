package frc.robot.commands;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.AutoConfig;
import frc.robot.Constants.AutoConfig.AutoRoutine;
import frc.robot.commands.targeting.DriveToTarget;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Grabber;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Vision;

public class ToteAuto extends SequentialCommandGroup {
  public ToteAuto(Drivetrain drivetrain, Grabber grabber, Intake intake, Vision vision, AutoRoutine auto) {
    switch (auto) {
      case LEFT_CLOSE:
        addCommands(
            new InstantCommand(() -> drivetrain.getSwerve()
                .resetOdometry(PathPlannerPath.fromPathFile("leftClose").getPreviewStartingHolonomicPose())),
            AutoBuilder.followPath(PathPlannerPath.fromPathFile("leftClose")));
        break;
      case LEFT_MID:
        addCommands(
            new InstantCommand(() -> drivetrain.getSwerve()
                .resetOdometry(PathPlannerPath.fromPathFile("leftMid").getPreviewStartingHolonomicPose())),
            AutoBuilder.followPath(PathPlannerPath.fromPathFile("leftMid")));
        break;
      case LEFT_FAR:
        addCommands(
            new InstantCommand(() -> drivetrain.getSwerve()
                .resetOdometry(PathPlannerPath.fromPathFile("leftFar").getPreviewStartingHolonomicPose())),
            AutoBuilder.followPath(PathPlannerPath.fromPathFile("leftFar")));
        break;
      case RIGHT_CLOSE:
        addCommands(
            new InstantCommand(() -> drivetrain.getSwerve()
                .resetOdometry(PathPlannerPath.fromPathFile("rightClose").getPreviewStartingHolonomicPose())),
            AutoBuilder.followPath(PathPlannerPath.fromPathFile("rightClose")));
        break;
      case RIGHT_MID:
        addCommands(
            new InstantCommand(() -> drivetrain.getSwerve()
                .resetOdometry(PathPlannerPath.fromPathFile("rightMid").getPreviewStartingHolonomicPose())),
            AutoBuilder.followPath(PathPlannerPath.fromPathFile("rightMid")));
        break;
      case RIGHT_FAR:
        addCommands(
            new InstantCommand(() -> drivetrain.getSwerve()
                .resetOdometry(PathPlannerPath.fromPathFile("rightFar").getPreviewStartingHolonomicPose())),
            AutoBuilder.followPath(PathPlannerPath.fromPathFile("rightFar")));
        break;
      default:
        break;
    }

    addCommands(
        new DriveToTarget(drivetrain,
            vision),
        new DriveToPose(drivetrain, AutoConfig.TAG_TO_TOTE),
        new PickUpTote(grabber),
        intake.runIndexer(),
        new WaitCommand(1.5),
        intake.stopIntake());

    addRequirements(drivetrain, grabber, intake, vision);
  }
}