package frc.robot.commands;

import java.util.Optional;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.IndexerConfig;
import frc.robot.subsystems.Indexer;

public class IndexBalloon extends Command {
    private final Indexer indexer;
    private Optional<Alliance> alliance;
    private boolean correctColor;

    public IndexBalloon(Indexer indexer) {
        this.indexer = indexer;
        alliance = DriverStation.getAlliance();
    }

    @Override
    public void initialize() {
        if (alliance.isPresent()) {
            if ((alliance.get() == Alliance.Red && indexer.getSensorOutputs().equals(IndexerConfig.BALLOON_RED_COLOR))
                    || (alliance.get() == Alliance.Blue
                            && indexer.getSensorOutputs().equals(IndexerConfig.BALLOON_BLUE_COLOR))) {
                correctColor = true;
            } else {
                correctColor = false;
            }
        }

        if (correctColor) {
            indexer.spinWheel();
        } else {
            indexer.extend();
        }

    }

    @Override
    public void end(boolean interrupted) {
        indexer.hold();
    }

    @Override
    public boolean isFinished() {
        return !(indexer.getSensorOutputs().equals(IndexerConfig.BALLOON_RED_COLOR)
                || indexer.getSensorOutputs().equals(IndexerConfig.BALLOON_BLUE_COLOR));
    }
}
