package com.stuypulse.robot.subsystems.wrist;

import com.ctre.phoenix6.hardware.TalonFX;
import com.stuypulse.robot.constants.GlobalPorts;
import com.stuypulse.robot.subsystems.wrist.WristConstants.DeviceIDs;

public class WristIOReal extends WristIOBase {
  public WristIOReal() {
    final TalonFX motorLeft = new TalonFX(DeviceIDs.LEFT_MOTOR, GlobalPorts.CAN_S1);
    final TalonFX motorRight = new TalonFX(DeviceIDs.RIGHT_MOTOR, GlobalPorts.CAN_S2);

    super(motorLeft, motorRight);
  }
}
