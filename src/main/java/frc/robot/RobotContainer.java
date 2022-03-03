// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.FlightStick;
import frc.robot.Constants.GamePad;
import frc.robot.Constants.Laptop;
import frc.robot.Constants.RoboRio;
import frc.robot.commands.ArcadeDriveCommand;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.CameraSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

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

    SmartDashboard.putData("Test Limit Switch", m_bottomLimit);    
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
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

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return null;
  }
}
