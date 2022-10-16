package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.Enums.RobotMode;

public class Robot extends TimedRobot
{
    private Dashboard _dashboard;

    @Override
    public void robotInit()
    {
        _dashboard = new Dashboard("2019", 1, 20, 1, 63);

        _dashboard.startHost();
        _dashboard.setRobotMode(RobotMode.rmInit);
    }

    @Override
    public void autonomousInit()
    {
        _dashboard.setRobotMode(RobotMode.rmAutonomous);
    }

    @Override
    public void teleopInit()
    {
        _dashboard.setRobotMode(RobotMode.rmTeleop);
    }

    @Override
    public void disabledInit()
    {
        _dashboard.setRobotMode(RobotMode.rmDisabled);
    }

    @Override
    public void testInit()
    {
        _dashboard.setRobotMode(RobotMode.rmTest);
    }
}
