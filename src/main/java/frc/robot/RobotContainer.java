// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.Optional;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.CANConfig;
import frc.robot.commands.DriveJoysticks;
import frc.robot.commands.DropTote;
import frc.robot.commands.PickUpTote;
import frc.robot.commands.RunAuto;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Grabber;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Vision;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  private Grabber grabber;
  public Intake intake;
  private Indexer indexer;
  private Vision vision;
  private Drivetrain drivetrain;

  private Compressor compressor = new Compressor(CANConfig.PNEUMATICS_HUB_PORT, PneumaticsModuleType.REVPH);

  private final CommandXboxController driver;
  private final CommandXboxController operator;
  private final Controls controls;

  private Optional<Alliance> alliance;

  private final SendableChooser<Command> autoChooser;

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    driver = new CommandXboxController(0);
    operator = new CommandXboxController(1);
    controls = new Controls(driver, operator);

    alliance = DriverStation.getAlliance();

    grabber = new Grabber();
    intake = new Intake();
    indexer = new Indexer(getAlliance());
    // vision = new Vision();
    drivetrain = new Drivetrain(getAlliance());

    autoChooser = new SendableChooser<Command>();

    compressor.enableAnalog(90, 120);

    // Configure the trigger bindings
    configureBindings();

    setDefaultCommands();
    buildAutoChooser();
  }

  private void buildAutoChooser() {
    autoChooser.setDefaultOption("NONE", Commands.none());
    // autoChooser.addOption("Right Close", new RunAuto(drivetrain, grabber, intake,
    // vision, AutoRoutine.RIGHT_CLOSE));
    // autoChooser.addOption("Right Mid", new RunAuto(drivetrain, grabber, intake,
    // vision, AutoRoutine.RIGHT_MID));
    // autoChooser.addOption("Right Far", new RunAuto(drivetrain, grabber, intake,
    // vision, AutoRoutine.RIGHT_FAR));
    // autoChooser.addOption("Left Close", new RunAuto(drivetrain, grabber, intake,
    // vision, AutoRoutine.LEFT_CLOSE));
    // autoChooser.addOption("Left Mid", new RunAuto(drivetrain, grabber, intake,
    // vision, AutoRoutine.LEFT_MID));
    // autoChooser.addOption("Left Far", new RunAuto(drivetrain, grabber, intake,
    // vision, AutoRoutine.LEFT_FAR));
    // autoChooser.addOption("Left Low Zone", new RunAuto(drivetrain, grabber,
    // intake, vision, AutoRoutine.LEFT_LOW));
    // autoChooser.addOption("Right Low Zone", new RunAuto(drivetrain, grabber,
    // intake, vision, AutoRoutine.RIGHT_LOW));
  }

  /**
   * Declares default commands for any subsystems that have them.
   */
  private void setDefaultCommands() {
    if (drivetrain != null) {
      drivetrain.setDefaultCommand(new DriveJoysticks(
          drivetrain,
          controls::getDriveSpeedX,
          controls::getDriveSpeedY,
          controls::getTurnSpeed));
    }
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be
   * created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with
   * an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
   * {@link
   * CommandXboxController
   * Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or
   * {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    if (drivetrain != null) {
      driver.a().onTrue(drivetrain.reset());
    }
    driver.y().onTrue(controls.toggleSlowmode());

    operator.a().onTrue(intake.toggleForwards());
    operator.x().onTrue(intake.toggleBackwards());
    operator.b().onTrue(intake.retractIntake());
    operator.y().onTrue(intake.extendIntake());
    operator.rightBumper().onTrue(grabber.raiseGrabber()).onFalse(grabber.stop());
    operator.leftBumper().onTrue(grabber.lowerGrabber()).onFalse(grabber.stop());
    operator.rightTrigger().onTrue(grabber.closeFinger());
    operator.leftTrigger().onTrue(grabber.openFinger());
    operator.povUp().onTrue(new PickUpTote(grabber));
    operator.povDown().onTrue(new DropTote(grabber));
  }

  private Alliance getAlliance() {
    return alliance.isEmpty() ? alliance.get() : null;
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }

  /**
   * Runs at the beginning of teleop.
   */
  public void teleopInit() {
    intake.extendIntake();
  }
}
