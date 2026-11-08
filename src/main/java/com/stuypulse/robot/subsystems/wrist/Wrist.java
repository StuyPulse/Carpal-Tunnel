/********************** PROJECT HIGHGROUND **********************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved. */
/* Use of this source code is governed by an MIT-style license */
/* that can be found in the repository LICENSE file.           */
/***************************************************************/
package com.stuypulse.robot.subsystems.wrist;

import static edu.wpi.first.units.Units.Degrees;

import com.stuypulse.robot.constants.GlobalSettings;
import com.stuypulse.robot.subsystems.wrist.WristConstants.WristSettings;
import com.stuypulse.robot.subsystems.wrist.WristIO.WristIOOutputMode;
import com.stuypulse.robot.subsystems.wrist.WristIO.WristIOOutputs;
import com.stuypulse.robot.util.PeriodicSubsystem;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj2.command.Command;
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

    private void setPitchState(WristPitchState pitchState) {
        this.pitchState = pitchState;
    }

    private void setRollState(WristRollState rollState) {
        this.rollState = rollState;
    }

    private void runWristPosition(Angle pitch, Angle roll) {
        outputs.wristMode = WristIOOutputMode.POSITION;
        outputs.wristTargetPitch = pitch;
        outputs.wristTargetRoll = roll;
    }

    private void stopWristMotor() {
        outputs.wristMode = WristIOOutputMode.STOP;
    }

    private Angle getRollDelta(Angle target, Angle current) {
        double delta = Math.IEEEremainder(target.minus(current).in(Degrees), 360.0);

        double min = WristSettings.ROLL_MIN.in(Degrees);
        double max = WristSettings.ROLL_MAX.in(Degrees);
        double currentDeg = current.in(Degrees);

        if (currentDeg + delta > max) {
            delta -= 360;
        } else if (currentDeg + delta < min) {
            delta += 360;
        }

        return current.plus(Degrees.of(delta));
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

        // roll = (L - R) / 2, matching the pitch +/- roll mixing in the IO layer
        Angle measuredRoll =
                inputs.motorLeftInputs.position.minus(inputs.motorRightInputs.position).div(2.0);

        runWristPosition(pitch, getRollDelta(roll, measuredRoll));
    }

    @Override
    public void periodicAfterScheduler() {
        io.applyOutputs(outputs);
    }

    public Command deploy() {
        return runOnce(() -> setPitchState(WristPitchState.DEPLOY));
    }

    public Command stow() {
        return runOnce(() -> setPitchState(WristPitchState.STOW));
    }

    public Command rotate() {
        return runOnce(() -> setRollState(WristRollState.ROTATE));
    }

    public Command neutralRoll() {
        return runOnce(() -> setRollState(WristRollState.NEUTRAL));
    }
}
