/********************** PROJECT HIGHGROUND **********************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved. */
/* Use of this source code is governed by an MIT-style license */
/* that can be found in the repository LICENSE file.           */
/***************************************************************/
package com.stuypulse.robot.subsystems.wrist;

import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.stuypulse.robot.subsystems.wrist.WristConstants.WristMotorConfigs;
import com.stuypulse.robot.util.talonfx.logged.LoggedTalonFX;
import edu.wpi.first.units.measure.Angle;

public abstract class WristIOBase implements WristIO {
    public final LoggedTalonFX motorLeft;
    private final LoggedTalonFX motorRight;

    private final PositionVoltage leftPositionController;
    private final VoltageOut leftVoltageController;
    private final PositionVoltage rightPositionController;
    private final VoltageOut rightVoltageController;

    public WristIOBase(LoggedTalonFX motorLeft, LoggedTalonFX motorRight) {
        this.motorLeft = motorLeft;
        this.motorRight = motorRight;

        WristMotorConfigs.LEFT_MOTOR_CONFIG.configure(motorLeft);
        WristMotorConfigs.RIGHT_MOTOR_CONFIG.configure(motorRight);

        leftPositionController = new PositionVoltage(0).withEnableFOC(true);
        leftVoltageController = new VoltageOut(0).withEnableFOC(true);
        rightPositionController = new PositionVoltage(0).withEnableFOC(true);
        rightVoltageController = new VoltageOut(0).withEnableFOC(true);
    }

    @Override
    public void updateInputs(WristIOInputs inputs) {
        motorLeft.updateInputs(inputs.motorLeftInputs);
        motorRight.updateInputs(inputs.motorRightInputs);
    }

    @Override
    public void applyOutputs(WristIOOutputs outputs) {
        switch (outputs.wristMode) {
            case POSITION -> {
                Angle leftTarget = outputs.wristTargetPitch.plus(outputs.wristTargetRoll);
                Angle rightTarget = outputs.wristTargetPitch.minus(outputs.wristTargetRoll);

                motorLeft.setControl(leftPositionController.withPosition(leftTarget));
                motorRight.setControl(rightPositionController.withPosition(rightTarget));
            }

            case VOLTAGE -> {
                motorLeft.setControl(
                        leftVoltageController.withOutput(
                                outputs.wristTargetPitchVoltage.plus(
                                        outputs.wristTargetRollVoltage)));
                motorRight.setControl(
                        rightVoltageController.withOutput(
                                outputs.wristTargetPitchVoltage.minus(
                                        outputs.wristTargetRollVoltage)));
            }

            case STOP -> {
                motorLeft.stopMotor();
                motorRight.stopMotor();
            }
        }
    }
}
