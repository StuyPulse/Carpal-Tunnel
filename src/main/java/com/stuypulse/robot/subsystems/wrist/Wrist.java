/************************ PROJECT ZIXI ************************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.subsystems.wrist;

import com.stuypulse.robot.constants.GlobalSettings;
import com.stuypulse.robot.subsystems.wrist.Wrist.WristPitchState;
import com.stuypulse.robot.subsystems.wrist.WristConstants.WristSettings;
import com.stuypulse.robot.subsystems.wrist.WristIO.WristIOOutputMode;
import com.stuypulse.robot.subsystems.wrist.WristIO.WristIOOutputs;
import com.stuypulse.robot.util.PeriodicSubsystem;

import org.wpilib.command3.Command;
import org.wpilib.units.measure.Angle;

import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;

public class Wrist extends PeriodicSubsystem {
    private final WristIO io;
    private final WristIOInputsAutoLogged inputs;
    private final WristIOOutputs outputs;

    public enum WristPitchState {
        DEPLOY,
        STOW
    }

    public enum WristRollState {
        NEUTRAL,
        ROTATE
    }

    @AutoLogOutput(key = "Wrist/PitchState")
    private WristPitchState pitchState;

    @AutoLogOutput(key = "Wrist/RollState")
    private WristRollState rollState;

    public Wrist(WristIO io) {
        this.io = io;
        inputs = new WristIOInputsAutoLogged();
        outputs = new WristIOOutputs();

        this.pitchState = WristPitchState.STOW;
        this.rollState = WristRollState.NEUTRAL;
    }

    private Command setPitchState(WristPitchState pitchState) {
        return run(coroutine -> this.pitchState = pitchState).named(getName() + "Set" + pitchState.name());
    }

    private Command setRollState(WristRollState rollState) {
        return run(coroutine -> this.rollState = rollState).named(getName() + "Set" + rollState.name());
    }

    private void runWristPosition(Angle pitch, Angle roll) {
        outputs.wristMode = WristIOOutputMode.POSITION;
        outputs.wristTargetPitch = pitch;
        outputs.wristTargetRoll = roll;
    }

    private void stopWristMotor() {
        outputs.wristMode = WristIOOutputMode.STOP;
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs("Wrist", inputs);

        if (!GlobalSettings.EnabledSubsystems.WRIST.get()) {
            stopWristMotor();
            return;
        }

        Angle pitch =
                switch (pitchState) {
                    case DEPLOY -> WristSettings.WRIST_DEPLOY_ANGLE;
                    case STOW -> WristSettings.WRIST_STOW_ANGLE;
                };

        Angle roll =
                switch (rollState) {
                    case NEUTRAL -> WristSettings.WRIST_NEUTRAL_ROLL;
                    case ROTATE -> WristSettings.WRIST_ROTATE_ANGLE;
                };

        runWristPosition(pitch, roll);
    }

    @Override
    public void periodicAfterScheduler() {
        io.applyOutputs(outputs);
    }

    public Command deploy() {
        return setPitchState(WristPitchState.DEPLOY);
    }

    public Command stow() {
        return setPitchState(WristPitchState.STOW);
    }

    public Command rotate() {
        return setRollState(WristRollState.ROTATE);
    }

    public Command neutralRoll() {
        return setRollState(WristRollState.NEUTRAL);
    }
}
