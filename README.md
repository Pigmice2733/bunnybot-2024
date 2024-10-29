# bunnybot-2024
Robot to play in the BunnyBots 2024 game Balloonapalooza. All motors are Neos with SparkMax encoders.

## Subsystems
- Drivetrain: Swerve (eight motors) with slowmode option.
- Intake: Two motors and one piston. Arm pushes out with piston, arm has motor on the end to spin duct-tape brushes. Another part at the bumper has another motor with brushes.
- Indexer: One motor and one piston, plus a color sensor. Motor spins brushes to move balloons through. Color sensor sorts out wrong-color balloons, and piston pushes them aside.
- Grabber: One motor and one piston. Motor lifts totes off the ground, piston catches overtop.

## Automation
- Pick up and set down totes.
- Extend and retract the intake system.
- Continuously separate balloons based on color.

## Controls
Driver: left joystick for moving & strafing, right joystick X for turning.
Operator: A to extend intake; B to retract intake; left trigger to pick up tote; right trigger to drop tote.