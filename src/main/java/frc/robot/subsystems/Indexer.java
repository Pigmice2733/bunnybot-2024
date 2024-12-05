package frc.robot.subsystems;

import com.revrobotics.ColorSensorV3;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.CANConfig;

public class Indexer extends SubsystemBase {
  private DoubleSolenoid piston;
  private ColorSensorV3 sensor;

  private GenericEntry redEntry, greenEntry, blueEntry, pistonEntry;
  private ShuffleboardLayout indexerEntries, sensorEntries;

  public Indexer() {
    piston = new DoubleSolenoid(
        CANConfig.PNEUMATICS_HUB_PORT,
        PneumaticsModuleType.REVPH,
        CANConfig.INDEXER_FORWARD_PORT,
        CANConfig.INDEXER_REVERSE_PORT);
    piston.set(Value.kOff);

    sensor = new ColorSensorV3(Port.kOnboard);

    indexerEntries = Constants.SUBSYSTEM_TAB.getLayout("Indexer", BuiltInLayouts.kList).withSize(1, 1).withPosition(1,
        0);
    pistonEntry = indexerEntries.add("Piston Value", "In").getEntry();

    sensorEntries = Constants.SUBSYSTEM_TAB.getLayout("Color Sensor", BuiltInLayouts.kList).withSize(1, 2)
        .withPosition(3, 0);
    redEntry = sensorEntries.add("Red", 0).getEntry();
    greenEntry = sensorEntries.add("Green", 0).getEntry();
    blueEntry = sensorEntries.add("Blue", 0).getEntry();
  }

  @Override
  public void periodic() {
    updateEntries();
  }

  private void updateEntries() {
    redEntry.setDouble(sensor.getRed());
    greenEntry.setDouble(sensor.getGreen());
    blueEntry.setDouble(sensor.getBlue());
  }

  public void extend() {
    piston.set(Value.kForward);
    pistonEntry.setString("Out");
  }

  public void retract() {
    piston.set(Value.kReverse);
    pistonEntry.setString("In");
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
}
