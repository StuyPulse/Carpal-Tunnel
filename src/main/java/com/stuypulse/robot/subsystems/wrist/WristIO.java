/************************ PROJECT ZIXI ************************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.subsystems.wrist;

import static org.wpilib.units.Units.*;

import org.wpilib.units.measure.*;

import org.littletonrobotics.junction.AutoLog;
import org.littletonrobotics.junction.AutoLogOutput;

public interface WristIO {
    @AutoLog
    public static class WristIOInputs {
        public Current leftMotorSupplyCurrent = Amps.zero();
        public Current leftMotorStatorCurrent = Amps.zero();
        public Temperature leftMotorTemperature = Celsius.zero();
        public Angle leftMotorPosition = Degrees.zero();
        public Voltage leftMotorAppliedVoltage = Volts.zero();
        public AngularVelocity leftMotorVelocity = DegreesPerSecond.zero();

        public Current rightMotorSupplyCurrent = Amps.zero();
        public Current rightMotorStatorCurrent = Amps.zero();
        public Temperature rightMotorTemperature = Celsius.zero();
        public Angle rightMotorPosition = Degrees.zero();
        public Voltage rightMotorAppliedVoltage = Volts.zero();
        public AngularVelocity rightMotorVelocity = DegreesPerSecond.zero();
    }

    public default void updateInputs(WristIOInputs inputs) {}

    public static enum WristIOOutputMode {
        POSITION,
        VOLTAGE,
        STOP
    }

    public static class WristIOOutputs {
        @AutoLogOutput(key = "Wrist/Output Mode")
        public WristIOOutputMode wristMode = WristIOOutputMode.POSITION;

        @AutoLogOutput(key = "Wrist/Target Pitch")
        public Angle wristTargetPitch = Degrees.zero();

        @AutoLogOutput(key = "Wrist/Target Roll")
        public Angle wristTargetRoll = Degrees.zero();

        @AutoLogOutput(key = "Wrist/Target Voltage")
        public Voltage wristTargetVoltage = Volts.zero();
    }

    public default void applyOutputs(WristIOOutputs outputs) {}
}
