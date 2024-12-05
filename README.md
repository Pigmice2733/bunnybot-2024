# bunnybot-2024

Robot to play in the BunnyBots 2024 game Balloonapalooza. All motors are Neos with SparkMax encoders.

## Subsystems

- Drivetrain: Swerve (eight motors) with slowmode option.
- Intake: Two motors and two pistons. Arm pushes out with pistons. One motor spins duct-tape brushes to pull balloons in, while the other spins brushes to push them through the system. The two pistons move together controlling the arm. The arm extends automatically at the beginning of teleop.
- Indexer: One piston and a color sensor. Color sensor sorts out wrong-color balloons, and piston pushes them aside.
- Grabber: One motor and one piston. Motor lifts totes off the ground, piston catches overtop.

## Automation

- Pick up and set down totes.
- Extend and retract the intake system.
- Continuously separate balloons based on color.
- Find a tote and move into place to pick it up using vision.
- Drive from the Corral to the Low Zone.

## Controls

Driver:

- left joystick for moving & strafing
- right joystick X for turning
- A to reset odometry
- Y to toggle slowmode

Operator:

- intake:
- - A to run forward (toggle)
- - X to run reverse (toggle)
- - Y to deploy
- - B to retract
- grabber:
- - right bumper to raise
- - left bumper to lower
- - right trigger to close finger
- - left trigger to open finger
- - D-pad up to pick up tote
- - D-pad down to drop tote
