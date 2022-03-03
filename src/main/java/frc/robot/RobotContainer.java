// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
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
  private CameraSubsystem m_cameraSubsystem = null;

  private final DigitalInput m_bottomLimit = new DigitalInput(RoboRio.DioPort.kArmBottomLimit);

  //private Joystick m_flightStick = new Joystick(Laptop.UsbPort.kFlightstick);
  private Joystick m_gamePad = new Joystick(Laptop.UsbPort.kGamePad);
  
  SendableChooser<Command> m_autonomousChooser = new SendableChooser<>();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Subsystems (Comment out to exclude a subsystem from the robot)
    //m_driveSubsystem = new DriveSubsystem();
    //m_climberSubsystem = new ClimberSubsystem();
    //m_armSubsystem = new ArmSubsystem();
    m_intakeSubsystem = new IntakeSubsystem();
    //m_cameraSubsystem = new CameraSubsystem();
    

    // Configure the button bindings
    configureButtonBindings();

    configureDefaultCommands();

    setUpAutonomousChooser();

    SmartDashboard.putData("Test Limit Switch", m_bottomLimit);    
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link edu.wpi.first.wpilibj.GenericHID} or one of its subclasses 
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link edu.wpi.first.wpilibj.XboxController}), 
   * and then passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    if (m_driveSubsystem != null) {
      new JoystickButton(m_gamePad, GamePad.Button.kRB)
        .whenPressed(() -> m_driveSubsystem.resetEncoders()
      );
    }

    if (m_cameraSubsystem != null) {
      new JoystickButton(m_gamePad, GamePad.Button.kLT) 
        .whenPressed(() -> m_cameraSubsystem.switchToShooterCam()
      );
      new JoystickButton(m_gamePad, GamePad.Button.kRT) 
        .whenPressed(() -> m_cameraSubsystem.switchToClimberCam()
      );
    }

    // new JoystickButton(m_gamePad, GamePad.Button.kLB)
    //   .whenHeld(getAutonomousCommand());
  }

  /**
   * Use this method to define the default commands for subsystems
   */
  private void configureDefaultCommands() {
    if (m_driveSubsystem != null) {
      // Flight stick was buggy close to 0. Need to find a different stick.      
      // m_driveSubsystem.setDefaultCommand(new ArcadeDriveCommand(m_driveSubsystem, 
      //   () -> m_flightStick.getRawAxis(FlightStick.Axis.kFwdBack), 
      //   () -> m_flightStick.getRawAxis(FlightStick.Axis.kRotate)
      // ));

      // m_driveSubsystem.setDefaultCommand(new ArcadeDriveCommand(m_driveSubsystem, 
      //   () -> -m_gamePad.getRawAxis(GamePad.LeftStick.kUpDown), 
      //   () -> m_gamePad.getRawAxis(GamePad.LeftStick.kLeftRight )
      // ));
    }

    if (m_climberSubsystem != null) {
      m_climberSubsystem.setDefaultCommand(new RunCommand(
        () -> m_climberSubsystem.move(-m_gamePad.getRawAxis(GamePad.LeftStick.kUpDown))
      , m_climberSubsystem));
    }

    if (m_armSubsystem != null) {
      m_armSubsystem.setDefaultCommand(new RunCommand(
        () -> m_armSubsystem.go(-m_gamePad.getRawAxis(GamePad.RightStick.kUpDown))
        , m_armSubsystem));
    }

    if (m_intakeSubsystem != null) {
      m_intakeSubsystem.setDefaultCommand(new RunCommand(
        () -> m_intakeSubsystem.go(-m_gamePad.getRawAxis(GamePad.RightStick.kLeftRight))
        , m_intakeSubsystem));
    }
  }

  private void setUpAutonomousChooser() {
    
    final double driveSpeed = 0.4;
    final double intakeSpeed = 0.2;

    Command justBackupCmd = new StartEndCommand(
        () -> m_driveSubsystem.arcadeDrive(-driveSpeed, 0), 
        () -> m_driveSubsystem.stop(),
        m_driveSubsystem)
      .withTimeout(1.0);

    Command timedStepsCmd = 
    new SequentialCommandGroup(
      new ParallelDeadlineGroup(
        new WaitCommand(0.5),
        new RunCommand(() -> m_driveSubsystem.arcadeDrive(driveSpeed, 0), m_driveSubsystem)
      ),
      new InstantCommand(() -> m_driveSubsystem.stop(), m_driveSubsystem),
      new ParallelDeadlineGroup(
        new WaitCommand(1.0),
        new RunCommand(() -> m_intakeSubsystem.go(intakeSpeed), m_intakeSubsystem)
      ),
      new InstantCommand(() -> m_intakeSubsystem.stop(), m_intakeSubsystem),
      new ParallelDeadlineGroup(
        new WaitCommand(1.0),
        new RunCommand(() -> m_driveSubsystem.arcadeDrive(-driveSpeed, 0), m_driveSubsystem)
      ),
      new InstantCommand(() -> m_driveSubsystem.stop(), m_driveSubsystem)
    );

    m_autonomousChooser.setDefaultOption("Do Nothing", null);
    m_autonomousChooser.addOption("Just Back Up", justBackupCmd);
    m_autonomousChooser.addOption("Timed Steps", timedStepsCmd);

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
