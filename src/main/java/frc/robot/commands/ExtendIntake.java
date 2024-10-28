package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Intake;

public class ExtendIntake extends SequentialCommandGroup {
  /**
   * Puts the intake in position to intake balloons and starts running the motor.
   * 
   * @param intake
   */
  public ExtendIntake(Intake intake) {
    addCommands(
        new InstantCommand(intake::extend, intake),
        intake.runIntakeOver(),
        intake.runIntakeThrough());
    addRequirements(intake);
  }
}
