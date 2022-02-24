// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.FlightStick;
import frc.robot.Constants.GamePad;
import frc.robot.Constants.Laptop;
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
  private DriveSubsystem m_driveSubsystem = new DriveSubsystem();
  private ClimberSubsystem m_climberSubsystem = new ClimberSubsystem();
  private ArmSubsystem m_armSubsystem = new ArmSubsystem();
  private IntakeSubsystem m_intakeSubsystem = new IntakeSubsystem();
  private CameraSubsystem m_cameraSubsystem = new CameraSubsystem();
  
  private Joystick m_flightStick = new Joystick(Laptop.UsbPort.kFlightstick);
  private Joystick m_gamePad = new Joystick(Laptop.UsbPort.kGamePad);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();

    configureDefaultCommands();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    new JoystickButton(m_gamePad, GamePad.Button.kRB)
      .whenPressed(() -> m_driveSubsystem.resetEncoders()
    );
    new JoystickButton(m_gamePad, GamePad.Button.kLT) 
      .whenPressed(() -> m_cameraSubsystem.switchToShooterCam()
    );
    new JoystickButton(m_gamePad, GamePad.Button.kRT) 
      .whenPressed(() -> m_cameraSubsystem.switchToClimberCam()
    );

  }

  /**
   * Use this method to define the default commands for subsystems
   */
  private void configureDefaultCommands() {
    m_driveSubsystem.setDefaultCommand(new ArcadeDriveCommand(m_driveSubsystem, 
      () -> m_flightStick.getRawAxis(FlightStick.Axis.kFwdBack), 
      () -> m_flightStick.getRawAxis(FlightStick.Axis.kRotate)
    ));

    m_climberSubsystem.setDefaultCommand(new RunCommand(
      () -> m_climberSubsystem.move(-m_gamePad.getRawAxis(GamePad.LeftStick.kUpDown))
    , m_climberSubsystem));

    m_armSubsystem.setDefaultCommand(new RunCommand(
      () -> m_armSubsystem.go(-m_gamePad.getRawAxis(GamePad.RightStick.kUpDown))
      , m_armSubsystem));

    m_intakeSubsystem.setDefaultCommand(new RunCommand(
      () -> m_intakeSubsystem.go(-m_gamePad.getRawAxis(GamePad.RightStick.kLeftRight))
      , m_intakeSubsystem));
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
