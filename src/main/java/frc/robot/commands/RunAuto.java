package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.AutoConfig;
import frc.robot.Constants.DrivetrainConfig;
import frc.robot.Constants.AutoConfig.AutoRoutine;
import frc.robot.commands.targeting.DriveToTarget;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Grabber;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Vision;

public class RunAuto extends SequentialCommandGroup {
  public RunAuto(Drivetrain drivetrain, Grabber grabber, Intake intake, Vision vision, AutoRoutine auto) {
    addCommands(
      new DriveToPose(drivetrain, auto.transform),
      new DriveToTarget(drivetrain, vision),
      new ZeroGrabberArm(grabber)
    );

    if (auto != AutoRoutine.LEFT_LOW && auto != AutoRoutine.RIGHT_LOW) {
      // TODO need locations of camera and grabber on robot
      addCommands(
        new DriveToPose(drivetrain, AutoConfig.TAG_TOTE_TRANSFORM_1),
        new DriveToPose(drivetrain, AutoConfig.TAG_TOTE_TRANSFORM_2),
        drivetrain.driveCommand(0, 0.01 * DrivetrainConfig.MAX_DRIVE_SPEED, 0),
        new PickUpTote(grabber),
        drivetrain.driveCommand(0, 0, 0)
      );
    }

    addCommands(intake.toggleForwards(), new WaitCommand(1.5), intake.stopIntake());

    addRequirements(drivetrain, grabber, intake, vision);
  }
}