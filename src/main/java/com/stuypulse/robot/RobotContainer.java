/********************** PROJECT HIGHGROUND **********************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved. */
/* Use of this source code is governed by an MIT-style license */
/* that can be found in the repository LICENSE file.           */
/***************************************************************/
package com.stuypulse.robot;

import com.stuypulse.robot.commands.auton.Auton;
import com.stuypulse.robot.commands.auton.AutonFactory;
import com.stuypulse.robot.constants.FieldConstants;
import com.stuypulse.robot.constants.GlobalPorts;
import com.stuypulse.robot.constants.GlobalSettings;
import com.stuypulse.robot.subsystems.wrist.Wrist;
import com.stuypulse.robot.subsystems.wrist.WristIO;
import com.stuypulse.robot.subsystems.wrist.WristIOReal;
import com.stuypulse.robot.subsystems.wrist.WristIOSim;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

public class RobotContainer {

    // Gamepads
    public final CommandXboxController driver =
            new CommandXboxController(GlobalPorts.Gamepad.DRIVER); // model-agnostic controller
    // public final CommandGamepad operator = new CommandGamepad(GlobalPorts.Gamepad.OPERATOR);

    // Subsystems
    private final Wrist wrist;
    // Autons
    private LoggedDashboardChooser<Auton> autonChooser = new LoggedDashboardChooser<>("Auton");

    // Robot container

    public RobotContainer() {
        switch (GlobalSettings.CURRENT_MODE) {
            case REAL -> wrist = new Wrist(new WristIOReal());

            case SIM -> wrist = new Wrist(new WristIOSim());

            default -> wrist = new Wrist(new WristIO() {});
        }

        configureLogging();
        configureDefaultCommands();
        configureButtonBindings();
        configureAutons();

        SmartDashboard.putData("Field", FieldConstants.FIELD_2D);
    }

    /***************/
    /*** LOGGING ***/
    /***************/

    private void configureLogging() {}

    /****************/
    /*** DEFAULTS ***/
    /****************/

    private void configureDefaultCommands() {}

    /***************/
    /*** BUTTONS ***/
    /***************/

    private void configureButtonBindings() {
        driver.a().onTrue(wrist.deploy());

        driver.y().onTrue(wrist.stow());

        driver.b().onTrue(wrist.rotate());

        driver.x().onTrue(wrist.neutralRoll());
    }

    /**************/
    /*** AUTONS ***/
    /**************/

    public void configureAutons() {
        AutonFactory autonFactory = new AutonFactory();

        autonChooser.addOption("Do Nothing", autonFactory.doNothingAuton());
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return The command to run in autonomous
     */
    public Auton getAutonomousCommand() {
        return autonChooser.get();
    }
}
