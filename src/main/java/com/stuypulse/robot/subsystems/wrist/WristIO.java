/********************** PROJECT HIGHGROUND **********************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved. */
/* Use of this source code is governed by an MIT-style license */
/* that can be found in the repository LICENSE file.           */
/***************************************************************/
package com.stuypulse.robot.subsystems.wrist;

import static edu.wpi.first.units.Units.*;

import com.stuypulse.robot.util.talonfx.logged.TalonFXInputs;
import edu.wpi.first.units.measure.*;
import org.littletonrobotics.junction.AutoLog;
import org.littletonrobotics.junction.AutoLogOutput;

public interface WristIO {
    @AutoLog
    public static class WristIOInputs {
        public TalonFXInputs motorLeftInputs = new TalonFXInputs();
        public TalonFXInputs motorRightInputs = new TalonFXInputs();
    }

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

        @AutoLogOutput(key = "Wrist/Target Pitch Voltage")
        public Voltage wristTargetPitchVoltage = Volts.zero();

        @AutoLogOutput(key = "Wrist/Target Pitch Voltage")
        public Voltage wristTargetRollVoltage = Volts.zero();
    }

    public default void applyOutputs(WristIOOutputs outputs) {}

    public default void updateInputs(WristIOInputs inputs) {}
}
