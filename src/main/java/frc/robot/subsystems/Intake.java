// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.CANConfig;

public class Intake extends SubsystemBase {
  private final CANSparkMax intakeMotor, indexerMotor;
  private final DoubleSolenoid piston = new DoubleSolenoid(
      Constants.CANConfig.PNEUMATICS_HUB_PORT,
      PneumaticsModuleType.REVPH,
      CANConfig.INTAKE_FORWARD_PORT,
      CANConfig.INTAKE_REVERSE_PORT);;
  private boolean stopped = true;

  private ShuffleboardLayout intakeEntries;
  private GenericEntry intakeMotorEntry, indexerMotorEntry, pistonEntry;

  /** Creates a new Intake. */
  public Intake() {
    intakeMotor = new CANSparkMax(CANConfig.INTAKE_MOTOR_PORT, MotorType.kBrushless);
    intakeMotor.restoreFactoryDefaults();
    intakeMotor.setInverted(true);

    indexerMotor = new CANSparkMax(CANConfig.INDEXER_MOTOR_PORT, MotorType.kBrushless);
    indexerMotor.restoreFactoryDefaults();

    intakeEntries = Constants.SUBSYSTEM_TAB.getLayout("Intake", BuiltInLayouts.kList).withSize(1, 3).withPosition(0, 0);
    intakeMotorEntry = intakeEntries.add("Intake Motor Speed", Constants.IntakeConfig.INTAKE_MOTOR_SPEED)
        .withWidget(BuiltInWidgets.kNumberSlider).getEntry();
    indexerMotorEntry = intakeEntries.add("Indexer Motor Speed", Constants.IntakeConfig.INDEXER_MOTOR_SPEED)
        .withWidget(BuiltInWidgets.kNumberSlider).getEntry();
    pistonEntry = intakeEntries.add("Piston Value", "In").getEntry();
  }

  @Override
  public void periodic() {
    updateEntries();
  }

  private void updateEntries() {
    // intakeMotorEntry.setDouble(intakeMotor.get());
    // indexerMotorEntry.setDouble(indexerMotor.get());
  }

  private void toggleMotors(boolean forwards) {
    if (stopped) {
      intakeMotor.set((forwards ? 1 : -1) * intakeMotorEntry.getDouble(Constants.IntakeConfig.INTAKE_MOTOR_SPEED));
      indexerMotor.set((forwards ? 1 : -1) * indexerMotorEntry.getDouble(Constants.IntakeConfig.INDEXER_MOTOR_SPEED));
      stopped = false;
    } else {
      stopMotors();
    }
  }

  private void stopMotors() {
    intakeMotor.set(0);
    indexerMotor.set(0);
    stopped = true;
  }

  private void extend() {
    piston.set(Value.kForward);
    pistonEntry.setString("Out");
  }

  private void retract() {
    piston.set(Value.kReverse);
    pistonEntry.setString("In");
  }

  public boolean isStopped() {
    return stopped;
  }

  /** Run the intake and indexer motors forwards. */
  public Command toggleForwards() {
    return new InstantCommand(() -> toggleMotors(true));
  }

  /** Run the intake and indexer motors backwards. */
  public Command toggleBackwards() {
    return new InstantCommand(() -> toggleMotors(false));
  }

  /** Stop running both motors. */
  public Command stopIntake() {
    return new InstantCommand(() -> stopMotors());
  }

  /** Deploy the intake. */
  public Command extendIntake() {
    return new InstantCommand(() -> extend(), this);
  }

  /** Reset the intake to its un-deployed position. */
  public Command retractIntake() {
    return new InstantCommand(() -> retract(), this);
  }
}