/********************** PROJECT HIGHGROUND **********************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved. */
/* Use of this source code is governed by an MIT-style license */
/* that can be found in the repository LICENSE file.           */
/***************************************************************/
package com.stuypulse.robot.subsystems.wrist;

import static edu.wpi.first.units.Units.*;

import com.stuypulse.robot.constants.GlobalSettings;
import com.stuypulse.robot.subsystems.wrist.WristConstants.*;
import com.stuypulse.robot.util.talonfx.sim.SystemSim;
import com.stuypulse.robot.util.talonfx.sim.TalonFXSimulation;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj.simulation.SingleJointedArmSim;
import org.littletonrobotics.junction.Logger;

public class WristIOSim extends WristIOBase {
    private static final boolean FLIP_LEFT_SIM = true;
    private static final boolean FLIP_RIGHT_SIM = false;

    private final SystemSim<SingleJointedArmSim> simLeft;
    private final SystemSim<SingleJointedArmSim> simRight;

    private final TalonFXSimulation motorLeft;
    private final TalonFXSimulation motorRight;

    private WristIOSim(
            TalonFXSimulation simMotorLeft,
            TalonFXSimulation simMotorRight,
            SystemSim<SingleJointedArmSim> simLeft,
            SystemSim<SingleJointedArmSim> simRight) {
        super(simMotorLeft, simMotorRight);

        motorLeft = simMotorLeft;
        motorRight = simMotorRight;

        this.simLeft = simLeft;
        this.simRight = simRight;
    }

    private WristIOSim(
            SystemSim<SingleJointedArmSim> simLeft, SystemSim<SingleJointedArmSim> simRight) {
        this(
                new TalonFXSimulation(
                        DeviceIDs.LEFT_MOTOR,
                        FLIP_LEFT_SIM
                                ? -WristSettings.WRIST_GEAR_RATIO
                                : WristSettings.WRIST_GEAR_RATIO,
                        simLeft),
                new TalonFXSimulation(
                        DeviceIDs.RIGHT_MOTOR,
                        FLIP_RIGHT_SIM
                                ? -WristSettings.WRIST_GEAR_RATIO
                                : WristSettings.WRIST_GEAR_RATIO,
                        simRight),
                simLeft,
                simRight);
    }

    public WristIOSim() {
        // left = pitch + roll, right = pitch - roll (same mixing as WristIOImpl)
        this(
                makeMotorSim(WristSettings.WRIST_STOW_ANGLE.plus(WristSettings.WRIST_NEUTRAL_ROLL)),
                makeMotorSim(
                        WristSettings.WRIST_STOW_ANGLE.minus(WristSettings.WRIST_NEUTRAL_ROLL)));
    }

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
