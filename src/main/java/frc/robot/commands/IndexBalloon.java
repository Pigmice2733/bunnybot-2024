package frc.robot.commands;

import java.util.Optional;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.IndexerConfig;
import frc.robot.Constants.IndexerConfig.BalloonColor;
import frc.robot.subsystems.Indexer;

public class IndexBalloon extends Command {
    private final Indexer indexer;
    private Alliance color;
    private Optional<Alliance> alliance;

    public IndexBalloon(Indexer indexer) {
        this.indexer = indexer;
        alliance = DriverStation.getAlliance();
    }

    @Override
    public void initialize() {
        if (indexer.getSensorOutputs().equals(IndexerConfig.BALLOON_RED_COLOR)) {
            color = Alliance.Red;
        } else if (indexer.getSensorOutputs().equals(IndexerConfig.BALLOON_BLUE_COLOR)) {
            color = Alliance.Blue;
        }

        switch (color) {
            case Red:
                indexer.setPistonPosition(Value.kForward);
                break;
            case Blue:
                indexer.setMotorSpeed(IndexerConfig.INDEXER_MOTOR_SPEED);
                break;
            default:
                break;
        }
    }

    @Override
    public void end(boolean interrupted) {
        switch (color) {
            case Red:
                indexer.setPistonPosition(Value.kReverse);
                break;
            case Blue:
                indexer.setMotorSpeed(0);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean isFinished() {
        return (indexer.getSensorOutputs().equals(IndexerConfig.BALLOON_RED_COLOR)
                && indexer.getSensorOutputs().equals(IndexerConfig.BALLOON_BLUE_COLOR));
    }
}
