// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CANConfig;
import frc.robot.Constants.IntakeConfig;

public class Intake extends SubsystemBase {
  private final CANSparkMax motor;
  private final DoubleSolenoid piston;

  /** Creates a new Intake. */
  public Intake() {
    motor = new CANSparkMax(CANConfig.INTAKE_MOTOR_PORT, MotorType.kBrushless);
    piston = new DoubleSolenoid(PneumaticsModuleType.REVPH, CANConfig.INTAKE_FORWARD_PORT,
        CANConfig.INTAKE_REVERSE_PORT);
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

  public void extend() {
    piston.set(Value.kForward);
  }

  public void retract() {
    piston.set(Value.kReverse);
  }

  public Command runIntake() {
    return new InstantCommand(() -> {
      setMotorSpeed(IntakeConfig.MOTOR_SPEED);
    }, this);
  }
}