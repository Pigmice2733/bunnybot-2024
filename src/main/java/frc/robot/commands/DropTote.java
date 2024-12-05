package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Grabber;

public class DropTote extends Command {
  private Grabber grabber;
  private PIDController motorController;

  /**
   * Drops a tote that the grabber is holding.
   * 
   * @param grabber
   */
  public DropTote(Grabber grabber) {
    this.grabber = grabber;
    motorController = grabber.getMotorController();

    addRequirements(grabber);
  }

  @Override
  public void initialize() {
    motorController.setSetpoint(0);
  }

  @Override
  public void execute() {
    grabber.setMotorSpeed(motorController.calculate(grabber.getAngle()));
  }

  @Override
  public void end(boolean interrupted) {
    grabber.stopMotor();
    grabber.openFinger();
  }

  @Override
  public boolean isFinished() {
    return motorController.atSetpoint();
  }
}
