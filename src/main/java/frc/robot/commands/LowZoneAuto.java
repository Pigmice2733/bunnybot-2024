package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.AutoConfig;
import frc.robot.commands.targeting.DriveToTarget;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Grabber;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Vision;

public class LowZoneAuto extends SequentialCommandGroup {
  public LowZoneAuto(Drivetrain drivetrain, Vision vision, Indexer indexer, Grabber grabber) {
    addCommands(
        new DriveToPose(drivetrain, AutoConfig.CORRAL_TO_LOW_ZONE),
        new DriveToTarget(drivetrain,
            vision),
        new DriveToPose(drivetrain, AutoConfig.TAG_TO_LOW_ZONE),
        indexer.runIndexer(),
        new WaitCommand(1.5),
        indexer.stop());
  }
}