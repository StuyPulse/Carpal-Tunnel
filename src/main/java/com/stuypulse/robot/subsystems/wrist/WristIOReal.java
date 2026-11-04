/************************ PROJECT ZIXI ************************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.subsystems.wrist;

import com.stuypulse.robot.constants.GlobalPorts;
import com.stuypulse.robot.subsystems.wrist.WristConstants.DeviceIDs;

import com.ctre.phoenix6.hardware.TalonFX;

public class WristIOReal extends WristIOBase {
    public WristIOReal() {
        final TalonFX motorLeft = new TalonFX(DeviceIDs.LEFT_MOTOR, GlobalPorts.CAN_S1);
        final TalonFX motorRight = new TalonFX(DeviceIDs.RIGHT_MOTOR, GlobalPorts.CAN_S2);

        super(motorLeft, motorRight);
    }
}
