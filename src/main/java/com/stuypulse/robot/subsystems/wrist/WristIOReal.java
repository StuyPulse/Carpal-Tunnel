/********************** PROJECT HIGHGROUND **********************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved. */
/* Use of this source code is governed by an MIT-style license */
/* that can be found in the repository LICENSE file.           */
/***************************************************************/
package com.stuypulse.robot.subsystems.wrist;

import com.stuypulse.robot.subsystems.wrist.WristConstants.DeviceIDs;
import com.stuypulse.robot.util.talonfx.logged.LoggedTalonFX;

public class WristIOReal extends WristIOBase {
    public WristIOReal() {
        super(new LoggedTalonFX(DeviceIDs.LEFT_MOTOR), new LoggedTalonFX(DeviceIDs.RIGHT_MOTOR));
    }
}
