package frc.robot.subsystems;

import com.revrobotics.ColorSensorV3;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.Constants.CANConfig;

public class Indexer extends SubsystemBase {
  private DoubleSolenoid piston = new DoubleSolenoid(
      CANConfig.PNEUMATICS_HUB_PORT,
      PneumaticsModuleType.REVPH,
      CANConfig.INDEXER_FORWARD_PORT,
      CANConfig.INDEXER_REVERSE_PORT);;
  private ColorSensorV3 sensor;

  private GenericEntry redEntry, greenEntry, blueEntry, irEntry, pistonEntry, balloonPresent, balloonIsRed;
  private ShuffleboardLayout indexerEntries, sensorEntries;
  private Alliance alliance;
  private boolean extended = false;

  public Indexer(Alliance alliance) {
    piston.set(Value.kOff);

    sensor = new ColorSensorV3(Port.kOnboard);
    this.alliance = alliance;

    indexerEntries = Constants.SUBSYSTEM_TAB.getLayout("Indexer", BuiltInLayouts.kList).withSize(1, 1).withPosition(1,
        0);
    sensorEntries = Constants.SUBSYSTEM_TAB.getLayout("Color Sensor", BuiltInLayouts.kList).withSize(1, 2)
        .withPosition(3, 0);
    balloonPresent = indexerEntries.add("Balloon Present", false).getEntry();
    balloonIsRed = indexerEntries.add("Balloon Red", false).getEntry();
    redEntry = sensorEntries.add("Red", 0).withPosition(0, 0).getEntry();
    greenEntry = sensorEntries.add("Green", 0).withPosition(0, 1).getEntry();
    blueEntry = sensorEntries.add("Blue", 0).withPosition(0, 2).getEntry();
    irEntry = sensorEntries.add("IR", 0).withPosition(0, 3).getEntry();
    pistonEntry = indexerEntries.add("Piston Value", "In").getEntry();

  }

  @Override
  public void periodic() {
    updateEntries();
    if (alliance == null)
      return;
    if (!extended && isBalloonPresent() && alliance == Alliance.Red) {
      rejectBalloonCommand().schedule();
    }
  }

  private boolean isBalloonPresent() {
    return sensor.getIR() >= Constants.IndexerConfig.IRBalloonPresentThreshold;
  }

  private boolean isBalloonRed() {
    int red = sensor.getRed();
    int blue = sensor.getBlue();
    return (red > Constants.IndexerConfig.MinRedThreshold
        && (red - blue) > Constants.IndexerConfig.MinRedBlueDifferential);
  }

  private void updateEntries() {
    irEntry.setDouble(sensor.getIR());
    redEntry.setDouble(sensor.getRed());
    greenEntry.setDouble(sensor.getGreen());
    blueEntry.setDouble(sensor.getBlue());
    balloonPresent.setBoolean(isBalloonPresent());
    balloonIsRed.setBoolean(isBalloonRed());
  }

  public boolean isExtended() {
    return extended;
  }

  public Alliance getAlliance() {
    return alliance;
  }

  public void extend() {
    extended = true;
    piston.set(Value.kForward);
    pistonEntry.setString("Out");
  }

  public void retract() {
    extended = false;
    piston.set(Value.kReverse);
    pistonEntry.setString("In");
  }

  public Color getSensorOutputs() {
    return new Color(sensor.getRed(), sensor.getGreen(), sensor.getBlue());
  }

  /** Checks if the color sensor's output matches the given color. */
  public boolean checkColor(Color color) {
    return this.getSensorOutputs().equals(color);
  }

  /** Returns a command to extend and then retract the indexer piston. */
  public Command rejectBalloonCommand() {
    return new InstantCommand(() -> extend()).andThen(
        new WaitCommand(0.125),
        new InstantCommand(() -> retract()));
  }
}
