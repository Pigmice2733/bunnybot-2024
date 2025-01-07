package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Vision;

public class VisionTesting extends Command {
  private Drivetrain dvt;
  private Vision vis;

  public VisionTesting(Drivetrain drivetrain, Vision vision) {
    dvt = drivetrain;
    vis = vision;
  }

  @Override
  public void initialize() {

  }
}
