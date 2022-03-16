// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

// import com.revrobotics.CANSparkMax;
// import com.revrobotics.CANSparkMax.IdleMode;
// import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Constants.RoboRio;
import edu.wpi.first.wpilibj.motorcontrol.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeSubsystem extends SubsystemBase {

  //private CANSparkMax m_motor = new CANSparkMax(RoboRio.CanId.kIntake, MotorType.kBrushless);
  private final VictorSP m_motor = new VictorSP(RoboRio.PwmPort.kIntakeMotor);

  /** Creates a new IntakeSubsystem. */
  public IntakeSubsystem() { 
    // m_motor.setSmartCurrentLimit(25, 90, 10);
    // m_motor.clearFaults();
    // m_motor.enableVoltageCompensation(12);
    // m_motor.setIdleMode(IdleMode.kBrake);
    // m_motor.setClosedLoopRampRate(1.0);
    // m_motor.setSecondaryCurrentLimit(95,250);      
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Intake Motor Speed", m_motor.get());
  }

  public void stop() {
    m_motor.set(0);
  }

  /***
   * Rotate the intake in or out.
   * @param speed A positive value rotates the intake in.
   */
  public void go(double speed){
    if (speed < -1) {
      speed = -1;
    }
    else if (speed > 1) {
      speed = 1;
    }

    m_motor.set(speed);
  }

}
