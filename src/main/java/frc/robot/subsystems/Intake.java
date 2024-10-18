// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CANConfig;

public class Intake extends SubsystemBase {
  private final CANSparkMax motorOver;
  private final CANSparkMax motorThrough;
  private final DoubleSolenoid piston;

  /** Creates a new Intake. */
  public Intake() {
    motorOver = new CANSparkMax(CANConfig.INTAKE_OVER_PORT, MotorType.kBrushless);
    motorThrough = new CANSparkMax(CANConfig.INTAKE_THROUGH_PORT, MotorType.kBrushless);
    piston = new DoubleSolenoid(PneumaticsModuleType.REVPH, CANConfig.INTAKE_FORWARD_PORT, CANConfig.INTAKE_BACKWARD_PORT);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setMotorOverSpeed(double speed) {
    motorOver.set(speed);
  }

  public void stopMotorOver() {
    motorOver.set(0);
  }

  public void setMotorThroughSpeed(double speed) {
    motorThrough.set(speed);
  }

  public void stopThroughOver() {
    motorThrough.set(0);
  }

  public void extend() {
    piston.set(Value.kForward);
  }

  public void retract() {
    piston.set(Value.kReverse);
  }
}