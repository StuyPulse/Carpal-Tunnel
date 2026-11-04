/************************ PROJECT ZIXI ************************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.subsystems.wrist;

import com.stuypulse.robot.subsystems.wrist.WristConstants.WristMotorConfigs;

import org.wpilib.units.measure.Angle;
import org.wpilib.units.measure.AngularVelocity;
import org.wpilib.units.measure.Current;
import org.wpilib.units.measure.Temperature;
import org.wpilib.units.measure.Voltage;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;

public abstract class WristIOBase implements WristIO {
    private final TalonFX motorLeft;
    private final TalonFX motorRight;

    private final PositionVoltage leftPositionController;
    private final VoltageOut leftVoltageController;
    private final PositionVoltage rightPositionController;
    private final VoltageOut rightVoltageController;

    private final StatusSignal<Angle> leftPosition;
    private final StatusSignal<Current> leftSupplyCurrent;
    private final StatusSignal<Current> leftStatorCurrent;
    private final StatusSignal<Temperature> leftTemperature;
    private final StatusSignal<Voltage> leftAppliedVoltage;
    private final StatusSignal<AngularVelocity> leftVelocity;

    private final StatusSignal<Angle> rightPosition;
    private final StatusSignal<Current> rightSupplyCurrent;
    private final StatusSignal<Current> rightStatorCurrent;
    private final StatusSignal<Temperature> rightTemperature;
    private final StatusSignal<Voltage> rightAppliedVoltage;
    private final StatusSignal<AngularVelocity> rightVelocity;

    public WristIOBase(TalonFX motorLeft, TalonFX motorRight) {
        this.motorLeft = motorLeft;
        this.motorRight = motorRight;

        WristMotorConfigs.LEFT_MOTOR_CONFIG.configure(motorLeft);
        WristMotorConfigs.RIGHT_MOTOR_CONFIG.configure(motorRight);

        leftPositionController = new PositionVoltage(0).withEnableFOC(true);
        leftVoltageController = new VoltageOut(0).withEnableFOC(true);
        rightPositionController = new PositionVoltage(0).withEnableFOC(true);
        rightVoltageController = new VoltageOut(0).withEnableFOC(true);

        leftPosition = motorLeft.getPosition();
        leftSupplyCurrent = motorLeft.getSupplyCurrent();
        leftStatorCurrent = motorLeft.getStatorCurrent();
        leftTemperature = motorLeft.getDeviceTemp();
        leftAppliedVoltage = motorLeft.getMotorVoltage();
        leftVelocity = motorLeft.getVelocity();

        rightPosition = motorRight.getPosition();
        rightSupplyCurrent = motorRight.getSupplyCurrent();
        rightStatorCurrent = motorRight.getStatorCurrent();
        rightTemperature = motorRight.getDeviceTemp();
        rightAppliedVoltage = motorRight.getMotorVoltage();
        rightVelocity = motorRight.getVelocity();
    }

    @Override
    public void updateInputs(WristIOInputs inputs) {
        BaseStatusSignal.refreshAll(
                leftPosition,
                leftSupplyCurrent,
                leftStatorCurrent,
                leftTemperature,
                leftAppliedVoltage,
                leftVelocity,
                rightPosition,
                rightSupplyCurrent,
                rightStatorCurrent,
                rightTemperature,
                rightAppliedVoltage,
                rightVelocity);

        inputs.leftMotorPosition = leftPosition.getValue();
        inputs.leftMotorSupplyCurrent = leftSupplyCurrent.getValue();
        inputs.leftMotorStatorCurrent = leftStatorCurrent.getValue();
        inputs.leftMotorTemperature = leftTemperature.getValue();
        inputs.leftMotorAppliedVoltage = leftAppliedVoltage.getValue();
        inputs.leftMotorVelocity = leftVelocity.getValue();

        inputs.rightMotorPosition = rightPosition.getValue();
        inputs.rightMotorSupplyCurrent = rightSupplyCurrent.getValue();
        inputs.rightMotorStatorCurrent = rightStatorCurrent.getValue();
        inputs.rightMotorTemperature = rightTemperature.getValue();
        inputs.rightMotorAppliedVoltage = rightAppliedVoltage.getValue();
        inputs.rightMotorVelocity = rightVelocity.getValue();
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
                motorLeft.setControl(leftVoltageController.withOutput(outputs.wristTargetVoltage));
                motorRight.setControl(
                        rightVoltageController.withOutput(outputs.wristTargetVoltage));
            }

            case STOP -> {
                motorLeft.stopMotor();
                motorRight.stopMotor();
            }
        }
    }
}
