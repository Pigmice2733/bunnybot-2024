package frc.robot.subsystems;

import frc.robot.Constants;
import frc.robot.Constants.PDHConfig;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ColorSensorV3;

import java.util.Map;

import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Indexer extends SubsystemBase {
    private CANSparkMax motor;
    private DoubleSolenoid piston;
    private ColorSensorV3 sensor;

    private GenericEntry motorEntry, sensorEntry;
    private ShuffleboardLayout indexerEntries;

    public Indexer() {
        motor = new CANSparkMax(PDHConfig.INDEXER_MOTOR_PORT, MotorType.kBrushless);
        motor.restoreFactoryDefaults();

        piston = new DoubleSolenoid(PneumaticsModuleType.REVPH, PDHConfig.INDEXER_PISTON_FORWARD_PORT, PDHConfig.INDEXER_PISTON_REVERSE_PORT);
        piston.set(Value.kOff);

        sensor = new ColorSensorV3(Port.kOnboard);

        indexerEntries = Constants.SYSTEMS_TAB.getLayout("Indexer", BuiltInLayouts.kList);
        motorEntry = Constants.SYSTEMS_TAB.add("Indexer Motor Speed", 0)
        .withWidget(BuiltInWidgets.kNumberBar)
        .withProperties(Map.of("min", 0, "max", 1)).getEntry();
        sensorEntry = Constants.SYSTEMS_TAB.add("Color Sensor Valued", )
    }

}
