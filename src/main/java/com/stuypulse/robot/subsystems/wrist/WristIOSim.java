package com.stuypulse.robot.subsystems.wrist;

import static org.wpilib.units.Units.*;

import org.littletonrobotics.junction.Logger;
import org.wpilib.math.system.DCMotor;
import org.wpilib.simulation.SingleJointedArmSim;
import org.wpilib.units.measure.Angle;

import com.stuypulse.robot.constants.GlobalSettings;
import com.stuypulse.robot.subsystems.wrist.WristConstants.*;

import com.stuypulse.robot.util.talonfx.sim.SystemSim;
import com.stuypulse.robot.util.talonfx.sim.TalonFXSimulation;

public class WristIOSim extends WristIOBase {
  private static final boolean FLIP_LEFT_SIM = true;
  private static final boolean FLIP_RIGHT_SIM = false;

  private final SystemSim<SingleJointedArmSim> simLeft;
  private final TalonFXSimulation motorLeft;

  private final SystemSim<SingleJointedArmSim> simRight;
  private final TalonFXSimulation motorRight;

  private static SystemSim<SingleJointedArmSim> makeMotorSim(Angle startAngle) {
    final double minAngle =
        WristSettings.WRIST_PITCH_MIN_ANGLE.in(Radians)
            - WristSettings.WRIST_ROLL_MAX_ANGLE.in(Radians);
    final double maxAngle =
        WristSettings.WRIST_PITCH_MAX_ANGLE.in(Radians)
            + WristSettings.WRIST_ROLL_MAX_ANGLE.in(Radians);

    return SystemSim.of(
        new SingleJointedArmSim(
            DCMotor.getKrakenX60Foc(1),
            WristSettings.WRIST_GEAR_RATIO,
            WristSettings.WRIST_MOI.in(KilogramSquareMeters),
            WristSettings.WRIST_ARM_LENGTH.in(Meters),
            minAngle,
            maxAngle,
            false,
            startAngle.in(Radians)));
  }

  public WristIOSim() {
    final double gearRatio = WristSettings.WRIST_GEAR_RATIO;

    // left = pitch + roll, right = pitch - roll (same mixing as WristIOImpl)
    final Angle leftStart = WristSettings.WRIST_STOW_ANGLE.plus(WristSettings.WRIST_NEUTRAL_ROLL);
    final Angle rightStart = WristSettings.WRIST_STOW_ANGLE.minus(WristSettings.WRIST_NEUTRAL_ROLL);

    final SystemSim<SingleJointedArmSim> simLeft = makeMotorSim(leftStart);
    final TalonFXSimulation motorLeft =
        new TalonFXSimulation(
            DeviceIDs.LEFT_MOTOR, FLIP_LEFT_SIM ? -gearRatio : gearRatio, simLeft);

    final SystemSim<SingleJointedArmSim> simRight = makeMotorSim(rightStart);
    final TalonFXSimulation motorRight =
        new TalonFXSimulation(
            DeviceIDs.RIGHT_MOTOR, FLIP_RIGHT_SIM ? -gearRatio : gearRatio, simRight);

    super(motorLeft, motorRight);

    this.simLeft = simLeft;
    this.motorLeft = motorLeft;

    this.simRight = simRight;
    this.motorRight = motorRight;
  }

  @Override
  public void updateInputs(WristIOInputs inputs) {
    simLeft.update(GlobalSettings.DT);
    motorLeft.refresh();

    simRight.update(GlobalSettings.DT);
    motorRight.refresh();

    super.updateInputs(inputs);

    // TODO: remove once the flip flags are confirmed
    Logger.recordOutput("WristSim/LeftSimAngle", simLeft.getMechanismPosition());
    Logger.recordOutput("WristSim/LeftReported", motorLeft.getPosition().getValue());
    Logger.recordOutput("WristSim/RightSimAngle", simRight.getMechanismPosition());
    Logger.recordOutput("WristSim/RightReported", motorRight.getPosition().getValue());
  }
}
