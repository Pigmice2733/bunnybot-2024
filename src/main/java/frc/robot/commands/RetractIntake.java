package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Intake;

public class RetractIntake extends SequentialCommandGroup {
    public RetractIntake(Intake intake) {
        addCommands(
            new InstantCommand(() -> intake.setMotorOverSpeed(0), intake),
            new InstantCommand(() -> intake.setMotorThroughSpeed(0), intake),
            new InstantCommand(intake::retract, intake)
        );

        addRequirements(intake);
    }
}
