package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.IntakeConfig;
import frc.robot.subsystems.Intake;

public class ExtendIntake extends SequentialCommandGroup {
    public ExtendIntake(Intake intake) {
        addCommands(
            new InstantCommand(intake::extend, intake),
            new InstantCommand(() -> intake.setMotorOverSpeed(IntakeConfig.OVER_SPEED), intake),
            new InstantCommand(() -> intake.setMotorThroughSpeed(IntakeConfig.THROUGH_SPEED), intake)
        );

        addRequirements(intake);
    }
}
