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

public class Grabber extends SubsystemBase {
  private final CANSparkMax motor;
  private final DoubleSolenoid piston;

  private ShuffleboardLayout grabberEntries;
  private GenericEntry motorEntry, pistonEntry;

  /** Creates a new Grabber. */
  public Grabber() {
    motor = new CANSparkMax(CANConfig.GRABBER_MOTOR_PORT, MotorType.kBrushless);
    motor.restoreFactoryDefaults();

    piston = new DoubleSolenoid(PneumaticsModuleType.REVPH, CANConfig.GRABBER_FORWARD_PORT,
        CANConfig.GRABBER_REVERSE_PORT);
    piston.set(Value.kOff);

    grabberEntries = Constants.SUBSYSTEM_TAB.getLayout("Grabber", BuiltInLayouts.kList).withSize(1, 2).withPosition(2,
        0);
    motorEntry = grabberEntries.add("Motor Speed", 0).withWidget(BuiltInWidgets.kNumberSlider).getEntry();
    pistonEntry = grabberEntries.add("Piston Value", "In").getEntry();
  }

  @Override
  public void periodic() {
    updateEntries();
  }

  private void updateEntries() {
    motorEntry.setDouble(motor.get());
  }

  public void setMotorSpeed(double speed) {
    motor.set(speed);
  }

  public void stopMotor() {
    motor.set(0);
  }

  public Command closeFinger() {
    pistonEntry.setString("In");
    return new InstantCommand(() -> piston.set(Value.kForward), this);
  }

  public Command openFinger() {
    pistonEntry.setString("Out");
    return new InstantCommand(() -> piston.set(Value.kReverse), this);
  }
}
