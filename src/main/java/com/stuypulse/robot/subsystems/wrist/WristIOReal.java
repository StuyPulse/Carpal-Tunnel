/************************ PROJECT ZIXI ************************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.subsystems.wrist;

import com.stuypulse.robot.subsystems.wrist.WristConstants.DeviceIDs;
import com.stuypulse.robot.util.talonfx.logged.LoggedTalonFX;

public class WristIOReal extends WristIOBase {
    public WristIOReal() {
        final LoggedTalonFX motorLeft = new LoggedTalonFX(DeviceIDs.LEFT_MOTOR);
        final LoggedTalonFX motorRight = new LoggedTalonFX(DeviceIDs.RIGHT_MOTOR);

        super(motorLeft, motorRight);
    }
}
