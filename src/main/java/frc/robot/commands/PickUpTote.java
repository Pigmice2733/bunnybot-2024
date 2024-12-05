package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Grabber;

public class PickUpTote extends Command {
  private Grabber grabber;
  private PIDController motorController;

  /**
   * Pick up a tote.
   * 
   * @param grabber
   */
  public PickUpTote(Grabber grabber) {
    this.grabber = grabber;
    motorController = grabber.getMotorController();

    addRequirements(grabber);
  }

  @Override
  public void initialize() {
    motorController.setSetpoint(40);
  }

  @Override
  public void execute() {
    grabber.setMotorSpeed(motorController.calculate(grabber.getAngle()));
  }

  @Override
  public void end(boolean interrupted) {
    grabber.stopMotor();
    grabber.closeFinger();
  }

  @Override
  public boolean isFinished() {
    return motorController.atSetpoint();
  }
}
