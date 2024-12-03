# bunnybot-2024

Robot to play in the BunnyBots 2024 game Balloonapalooza. All motors are Neos with SparkMax encoders.

## Subsystems

- Drivetrain: Swerve (eight motors) with slowmode option.
- Intake: Two motors and two pistons. Arm pushes out with pistons. One motor spins duct-tape brushes to pull balloons in, while the other spins brushes to push them through the system. The two pistons move together controlling the arm. The arm extends automatically at the beginning of teleop.
- Indexer: One piston, plus a color sensor. Color sensor sorts out wrong-color balloons, and piston pushes them aside.
- Grabber: One motor and one piston. Motor lifts totes off the ground, piston catches overtop.

## Automation

- Pick up and set down totes.
- Extend and retract the intake system.
- Continuously separate balloons based on color.
- Find a tote and move into place to pick it up using vision.

## Controls

Driver: left joystick for moving & strafing, right joystick X for turning, A to reset odometry, Y to toggle slowmode
Operator: A to toggle intake, X to extend intake, B to retract intake, left bumper to pick up tote, right bumper to drop tote
