// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.*;
import frc.robot.subsystems.*;

import static frc.robot.Constants.*;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private DriveSubsystem m_driveSubsystem = null;
  private ClimberSubsystem m_climberSubsystem = null;
  private ArmSubsystem m_armSubsystem = null;
  private IntakeSubsystem m_intakeSubsystem = null;

  private Joystick m_flightStick = null;
  private Joystick m_gamePad = null;

  private UsbCamera m_camera1;
  private UsbCamera m_camera2;
  
  SendableChooser<Command> m_autonomousChooser = new SendableChooser<>();


  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Subsystems (Comment out to exclude a subsystem from the robot)
    m_driveSubsystem = new DriveSubsystem();
    m_climberSubsystem = new ClimberSubsystem();
    m_armSubsystem = new ArmSubsystem();
    m_intakeSubsystem = new IntakeSubsystem();
    
    // Controllers (comment out to exclude a controller from the robot)
    m_flightStick = new Joystick(Laptop.UsbPort.kFlightstick);
    m_gamePad = new Joystick(Laptop.UsbPort.kGamePad);
    
    m_camera1 = CameraServer.startAutomaticCapture(0);
    if (m_camera1 != null) {
      m_camera1.setResolution(320, 240);
    }
    //m_camera2 = CameraServer.startAutomaticCapture(0);
    if (m_camera2 != null) {
      m_camera2.setResolution(320, 240);
    }

    // Configure the button bindings
    configureButtonBindings();

    configureDefaultCommands();

    setUpAutonomousChooser();
  }


  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link edu.wpi.first.wpilibj.GenericHID} or one of its subclasses 
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link edu.wpi.first.wpilibj.XboxController}), 
   * and then passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    if (m_driveSubsystem != null && m_gamePad != null) {
      new JoystickButton(m_gamePad, GamePad.Button.kRB)
        .whenPressed(() -> m_driveSubsystem.resetEncoders()
      );
    }

    if (m_climberSubsystem != null && m_gamePad != null) {
      new JoystickButton(m_gamePad, GamePad.Button.kY)
        .whileHeld(() -> m_climberSubsystem.move(0.8)
        );
      new JoystickButton(m_gamePad, GamePad.Button.kY)
        .whenReleased(() -> m_climberSubsystem.stop()
        );

      new JoystickButton(m_gamePad, GamePad.Button.kA)
        .whileHeld(() -> m_climberSubsystem.move(-1.5)
        );
      new JoystickButton(m_gamePad, GamePad.Button.kA)
        .whenReleased(() -> m_climberSubsystem.stop()
        );    }

    if (m_gamePad != null) {
      // Uncomment to allow testing of the autonomous commands during teleop 
      // new JoystickButton(m_gamePad, GamePad.Button.kB)
      //   .whileHeld(getAutonomousCommand());
    }
  }


  /**
   * Use this method to define the default commands for subsystems
   */
  private void configureDefaultCommands() {

    if (m_driveSubsystem != null && m_flightStick != null) {
      m_driveSubsystem.setDefaultCommand(new ArcadeDriveCommand(m_driveSubsystem, 
        () -> -m_flightStick.getRawAxis(FlightStick.Axis.kFwdBack), 
        () -> m_flightStick.getRawAxis(FlightStick.Axis.kRotate)
      ));
    }

    if (m_climberSubsystem != null && m_gamePad != null) {
      // Use buttons for raising & lowering, but this can be used to find the appropriate speed.
      // Do not uncomment unless the intake subsystem has been commented out (it's using the same stick)
      // m_climberSubsystem.setDefaultCommand(new RunCommand(
      //   () -> m_climberSubsystem.move(m_gamePad.getRawAxis(GamePad.RightStick.kUpDown))
      // , m_climberSubsystem));
    }

    if (m_armSubsystem != null && m_gamePad != null) {
      m_armSubsystem.setDefaultCommand(new RunCommand(
        () -> m_armSubsystem.go(-m_gamePad.getRawAxis(GamePad.LeftStick.kUpDown))
        , m_armSubsystem));
    }

    if (m_intakeSubsystem != null && m_gamePad != null) {
      m_intakeSubsystem.setDefaultCommand(new RunCommand(
        () -> m_intakeSubsystem.go(-m_gamePad.getRawAxis(GamePad.RightStick.kUpDown))
        , m_intakeSubsystem));
    }
  }


  /**
   * Configure the options to present in the Autonomous drop list on the dashboard.
   * When the Autonomous period starts, it will run whichever command has been selected. 
   */
  private void setUpAutonomousChooser() {
    
    final double driveSpeed = 0.5;
    final double intakeSpeed = 0.4;

    if (m_driveSubsystem != null) {
      Command justBackupCmd = new ParallelDeadlineGroup(
        new WaitCommand(2.5),
        new RunCommand(() -> m_driveSubsystem.arcadeDrive(-driveSpeed, 0), m_driveSubsystem)
      );
      m_autonomousChooser.setDefaultOption("Just Back Up", justBackupCmd);
    }

    m_autonomousChooser.addOption("Do Nothing", null);
    
    if (m_driveSubsystem != null && m_intakeSubsystem != null) {
      Command timedStepsCmd = 
      new SequentialCommandGroup(
        // new ParallelDeadlineGroup(
        //   new WaitCommand(1.5),
        //   new RunCommand(() -> m_driveSubsystem.arcadeDrive(driveSpeed, 0), m_driveSubsystem)
        // ),
        // new InstantCommand(() -> m_driveSubsystem.stop(), m_driveSubsystem),
        new ParallelDeadlineGroup(
          new WaitCommand(2.0),
          new RunCommand(() -> m_intakeSubsystem.go(intakeSpeed), m_intakeSubsystem)
        ),
        new InstantCommand(() -> m_intakeSubsystem.stop(), m_intakeSubsystem),
        new ParallelDeadlineGroup(
          new WaitCommand(2.5),
          new RunCommand(() -> m_driveSubsystem.arcadeDrive(-driveSpeed, 0), m_driveSubsystem)
        ),
        new InstantCommand(() -> m_driveSubsystem.stop(), m_driveSubsystem)
      );
      m_autonomousChooser.addOption("Timed Steps", timedStepsCmd);
    }

    SmartDashboard.putData("Autonomous", m_autonomousChooser);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_autonomousChooser.getSelected();
  }
}
