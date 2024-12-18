package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.GrabberConfig;
import frc.robot.subsystems.Grabber;

public class PickUpTote extends Command {
  private Grabber grabber;
  private PIDController motorController;

  /** Pick up a tote. */
  public PickUpTote(Grabber grabber) {
    this.grabber = grabber;
    motorController = grabber.getMotorController();

    addRequirements(grabber);
  }

  @Override
  public void initialize() {
    motorController.setSetpoint(GrabberConfig.GRABBED_TOTE_ANGLE);
  }

  @Override
  public void execute() {
    grabber.setMotorSpeed(motorController.calculate(grabber.getAngle()));
  }

  @Override
  public void end(boolean interrupted) {
    System.out.println("PickUpTote command ending");
    grabber.setMotorSpeed(0);
    System.out.println("Motor stopped");
    grabber.close();
    System.out.println("Piston closed");
  }

  @Override
  public boolean isFinished() {
    return motorController.atSetpoint();
  }
}
