// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DigitalInput;
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
import frc.robot.Constants.GrabberConfig;

public class Grabber extends SubsystemBase {
  private final CANSparkMax motor;
  private final DoubleSolenoid piston;
  private final DigitalInput limitSwitch;
  private boolean zeroed;

  private double angle;
  private PIDController motorController;

  private ShuffleboardLayout grabberEntries;
  private GenericEntry motorEntry, pistonEntry, switchEntry, angleEntry, zeroedEntry;

  /** Creates a new Grabber. */
  public Grabber() {
    motor = new CANSparkMax(CANConfig.GRABBER_MOTOR_PORT, MotorType.kBrushless);
    motor.restoreFactoryDefaults();
    motor.getEncoder().setPositionConversionFactor(GrabberConfig.GEAR_RATIO);

    piston = new DoubleSolenoid(
        CANConfig.PNEUMATICS_HUB_PORT,
        PneumaticsModuleType.REVPH,
        CANConfig.GRABBER_FORWARD_PORT,
        CANConfig.GRABBER_REVERSE_PORT);
    piston.set(Value.kOff);

    limitSwitch = new DigitalInput(CANConfig.LIMIT_SWITCH_PORT);

    motorController = GrabberConfig.GRABBER_PID;
    motorController.setTolerance(GrabberConfig.PID_TOLERANCE);

    zeroed = false;
    angle = GrabberConfig.INITIAL_GRABBER_ANGLE;

    grabberEntries = Constants.SUBSYSTEM_TAB
        .getLayout("Grabber", BuiltInLayouts.kList)
        .withSize(1, 5)
        .withPosition(2, 0);
    motorEntry = grabberEntries.add("Motor Speed", 0).withWidget(BuiltInWidgets.kNumberSlider).withPosition(0, 0)
        .getEntry();
    angleEntry = grabberEntries.add("Grabber Angle", 0).withPosition(0, 1).getEntry();
    pistonEntry = grabberEntries.add("Piston Extended", false).withPosition(0, 2).getEntry();
    switchEntry = grabberEntries.add("Limit Switch", false).withPosition(0, 3).getEntry();
    zeroedEntry = grabberEntries.add("Zeroed", false).withPosition(0, 4).getEntry();
  }

  @Override
  public void periodic() {
    angle = motor.getEncoder().getPosition() * 360;
    motor.set(applySoftwareStops(motor.get()));

    updateEntries();
  }

  private void updateEntries() {
    motorEntry.setDouble(motor.get());
    switchEntry.setBoolean(getSwitch());
    angleEntry.setDouble(angle);
    zeroedEntry.setBoolean(zeroed);
    pistonEntry.setBoolean(isPistonExtended());
  }

  /**
   * Prevents the grabber arm from moving if: it tries to move upward and it isn't
   * zeroed yet; it tries to move upward and it has hit the upper bound of its
   * range of motion; or it tried to move downward and the limit-switch is
   * activated.
   * 
   * @param speed Preferred speed if the grabber is allowed to move.
   * @return Input speed if allowed, or zero if not allowed.
   */
  private double applySoftwareStops(double speed) {
    boolean cannotMove = (speed > 0 && (!zeroed
        || angle >= GrabberConfig.MAX_ANGLE_PISTON_IN
        || (angle >= GrabberConfig.MAX_ANGLE_PISTON_OUT && isPistonExtended())))
        || (speed < 0 && getSwitch());
    return cannotMove ? 0 : speed;
  }

  public void setMotorSpeed(double speed) {
    speed = applySoftwareStops(speed);
    motor.set(speed);
  }

  public void close() {
    piston.set(Value.kForward);
  }

  public void open() {
    piston.set(Value.kReverse);
  }

  public boolean getSwitch() {
    return !limitSwitch.get();
  }

  public double getAngle() {
    return angle;
  }

  public PIDController getMotorController() {
    return motorController;
  }

  private boolean isPistonExtended() {
    return piston.get() == Value.kForward;
  }

  public void zero() {
    angle = 0;
    motor.getEncoder().setPosition(0);
    zeroed = true;
  }

  public void unzero() {
    zeroed = false;
  }

  public Command closeFinger() {
    return new InstantCommand(this::close);
  }

  public Command openFinger() {
    return new InstantCommand(this::open);
  }

  public Command raiseGrabber() {
    return new InstantCommand(() -> setMotorSpeed(GrabberConfig.GRABBER_MOTOR_SPEED));
  }

  public Command lowerGrabber() {
    return new InstantCommand(() -> setMotorSpeed(-GrabberConfig.GRABBER_MOTOR_SPEED));
  }

  public Command stop() {
    return new InstantCommand(() -> setMotorSpeed(0));
  }
}
