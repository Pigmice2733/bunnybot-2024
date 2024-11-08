package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;

public class DriveJoysticks extends Command {
  private Drivetrain drivetrain;
  private DoubleSupplier driveX, driveY, turn;

  public DriveJoysticks(Drivetrain drivetrain, DoubleSupplier driveSpeedX, DoubleSupplier driveSpeedY,
      DoubleSupplier turnSpeed) {
    this.drivetrain = drivetrain;
    driveX = driveSpeedX;
    driveY = driveSpeedY;
    turn = turnSpeed;

    addRequirements(drivetrain);
  }

  @Override
  public void execute() {
    drivetrain.drive(driveX.getAsDouble(), driveY.getAsDouble(), turn.getAsDouble());
  }
}
