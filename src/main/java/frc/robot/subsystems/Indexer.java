package frc.robot.subsystems;

import frc.robot.Constants;
import frc.robot.Constants.CANConfig;
import frc.robot.Constants.IndexerConfig;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ColorSensorV3;

import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Indexer extends SubsystemBase {
  private CANSparkMax motor;
  private DoubleSolenoid piston;
  private ColorSensorV3 sensor;

  private GenericEntry motorEntry, redEntry, greenEntry, blueEntry;
  private ShuffleboardLayout sensorEntries;

  public Indexer() {
    motor = new CANSparkMax(CANConfig.INDEXER_MOTOR_PORT, MotorType.kBrushless);
    motor.restoreFactoryDefaults();

    piston = new DoubleSolenoid(PneumaticsModuleType.REVPH, CANConfig.INDEXER_FORWARD_PORT,
        CANConfig.INDEXER_REVERSE_PORT);
    piston.set(Value.kOff);

    sensor = new ColorSensorV3(Port.kOnboard);

    sensorEntries = Constants.INDEXER_TAB.getLayout("Color Sensor", BuiltInLayouts.kList);
    redEntry = sensorEntries.add("Red", 0).getEntry();
    greenEntry = sensorEntries.add("Green", 0).getEntry();
    blueEntry = sensorEntries.add("Blue", 0).getEntry();
    // TODO fix Shuffleboard
  }

  @Override
  public void periodic() {
    updateEntries();
  }

  public void updateEntries() {
    redEntry.setDouble(sensor.getRed());
    greenEntry.setDouble(sensor.getGreen());
    blueEntry.setDouble(sensor.getBlue());

    motorEntry.setDouble(motor.get());
  }

  public void setMotorSpeed(double speed) {
    motor.set(speed);
  }

  public double getMotorSpeed() {
    return motor.get();
  }

  public void extend() {
    piston.set(Value.kForward);
  }

  public void retract() {
    piston.set(Value.kReverse);
  }

  public Color getSensorOutputs() {
    return new Color(sensor.getRed(), sensor.getGreen(), sensor.getBlue());
  }

  /**
   * Checks if the color sensor's output matches the given color.
   * 
   * @param color color to check against
   * @return whether the colors match
   */
  public boolean checkColor(Color color) {
    return this.getSensorOutputs().equals(color);
  }

  /**
   * Stops the motor and closes the piston.
   * 
   * @return command that executes the code
   */
  public Command stop() {
    return new InstantCommand(() -> {
      setMotorSpeed(0);
      retract();
    }, this);
  }

  public Command runIndexer() {
    return new InstantCommand(() -> {
      setMotorSpeed(IndexerConfig.INDEXER_MOTOR_SPEED);
    }, this);
  }
}
