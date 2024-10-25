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

public class Grabber extends SubsystemBase {
  private final CANSparkMax motor;
  private final DoubleSolenoid piston;

  /** Creates a new Grabber. */
  public Grabber() {
    motor = new CANSparkMax(CANConfig.GRABBER_MOTOR_PORT, MotorType.kBrushless);

    piston = new DoubleSolenoid(PneumaticsModuleType.REVPH, CANConfig.GRABBER_FORWARD_PORT,
        CANConfig.GRABBER_REVERSE_PORT);
    piston.set(Value.kOff);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setMotorSpeed(double speed) {
    motor.set(speed);
  }

  public void stopMotor() {
    motor.set(0);
  }

  public void closeFinger() {
    piston.set(Value.kForward);
  }

  public void openFinger() {
    piston.set(Value.kReverse);
  }
}
