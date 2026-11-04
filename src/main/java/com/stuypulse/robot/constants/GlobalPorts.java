/************************ PROJECT ZIXI ************************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.constants;

import org.wpilib.hardware.bus.CANPort;

import com.ctre.phoenix6.CANBus;

/**
 * This file contains the different hardware ports of hardware not belonging to a particular
 * subsystem.
 */
public interface GlobalPorts {
    public interface Gamepad {
        int DRIVER = 0;
        int OPERATOR = 1;
        int DEBUGGER = 2;
    }

    CANBus CAN_S1 = new CANBus(CANPort.CAN_S1);
    CANBus CAN_S2 = new CANBus(CANPort.CAN_S2);
}
