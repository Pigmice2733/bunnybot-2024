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

  /**
   * Continuously checks if a balloon is in the indexer and sorts it according to
   * our alliance color.
   * Default command for the indexer subsystem.
   * 
   * @param indexer
   */
  public IndexBalloon(Indexer indexer) {
    this.indexer = indexer;
    alliance = DriverStation.getAlliance();
    addRequirements(indexer);
  }

  @Override
  public void execute() {
    if (alliance.isPresent()) {
      if ((alliance.get() == Alliance.Red
          && indexer.checkColor(IndexerConfig.BALLOON_BLUE_COLOR))
          || (alliance.get() == Alliance.Blue
              && indexer.checkColor(IndexerConfig.BALLOON_RED_COLOR))) {
        indexer.extend();
      } else {
        indexer.retract();
      }
    } else {
      indexer.retract();
    }
  }
}
