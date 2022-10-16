package frc.robot;

public class Enums 
{
    public enum RobotMode
    {
        rmInit,
        rmDisabled,
        rmAutonomous,
        rmTeleop,
        rmTest
    }

    public enum RobotStatus 
    {
        rsShifterLow,
        rsFootRetracted,
        rsFloorFront,
        rsFloorRear,
        rsCargo,
        rsHatchMode,
        rsHatchGrab,
        rsClimb,
        rsNoClimb,
        rsTargetFound, 
        rsCargoLoaded
    }

    public enum RobotValue
    {
        rvDriveGyro,
        rvDriveEncoderL,
        rvDriveEncoderR,
        rvDriveAmpsLeft1,
        rvDriveAmpsLeft2,
        rvDriveAmpsRight1,
        rvDriveAmpsRight2,
        rvElevatorPosition,
        rvElevatorSetpoint,
        rvElevatorAmps,
        rvShoulderPosition,
        rvShoulderSetpoint,
        rvShoulderAmps,
        rvWristPosition,
        rvWristSetpoint,
        rvWristAmps,
        rvVisionStatus,
        rvVisionSelect,
        rvVisionAngle,
        rvVisionDistance
    }

    public enum DashButton
    {
        dbRunPid,
        dbDataCapture,
        dbVisionAuto,
        dbSandStormAuto
    }

    public enum DashValue
    {
        dvAutoHatchPlace,
        dvAutoStop,
        dvPidSelect,
        dvPidSetpoint,
        dvPidMaxOut,
        dvPthreshold,
        dvPabove,
        dvPbelow,
        dvIthreshold,
        dvIabove,
        dvIbelow,
        dvDthreshold,
        dvDabove,
        dvDbelow,
        dvElevOffset,
        dvElevMin,
        dvElevMax,
        dvFloorSensorMin,
        dvElevRetracted,
        dvElevLevel2,
        dvElevLevel3,
        dvShoulderOffset,
        dvShoulderMin, 
        dvShoulderMax,
        dvShoulderClear,
        dvShoulderTravel,
        dvSCPickup,
        dvSCLoad,
        dvSCCargoShip,
        dvSCRocketLow,
        dvSCRocketMid,
        dvSCRocketHigh,
        dvSCCatch,
        dvSHRocketLow,
        dvSHRocketMid,
        dvSHRocketHigh,
        dvWristOffset,
        dvWristMin,
        dvWristMax,
        dvWristClear,
        dvWristTravel,
        dvWCPickup,
        dvWCLoad,
        dvWCCargoShip,
        dvWCRocketLow,
        dvWCRocketMid,
        dvWCRocketHigh,
        dvWCCatch,
        dvWHRocketLow,
        dvWHRocketMid,
        dvWHRocketHigh,
        dvCargoSpeedIn,
        dvCargoSpeedOut,
        dvCargoSpeedRotate,
        dvCargoRotateRatio,
        dvCargoEjectTime,
        dvCargoRotateTime,
        dvVisionTargetAngle,
        dvVisionCargoLoad,
        dvVisionHatchLoad,
        dvVisionHatchLow,
        dvVisionHatchMid,
        dvVisionHatchHigh
    }
}
