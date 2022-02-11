// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Constants.RoboRio;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ArmSubsystem extends SubsystemBase {

  private CANSparkMax m_motor;
  private final DigitalInput m_topLimmit = new DigitalInput(RoboRio.DioPort.kTopLimmit);
  private final DigitalInput m_bottomLimmit = new DigitalInput(RoboRio.DioPort.kBottomLimmit);
  
  /** Creates a new IntakeSubsystem. */
  public ArmSubsystem() { 
    
    m_motor = new CANSparkMax(RoboRio.CanId.kIntake, MotorType.kBrushless);
  
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }


  public void go (double speed){ 
    if (speed > 0.0) {
      if (isAtTop()) {
        speed = 0.0;
      }
      if (speed > 1.0) {
        speed = 1.0;
      }
    }
    if (speed < 0.0) {
      if (isAtBottom()) {
        speed = 0.0;
      }
      if(speed < -1.0) {
        speed = -1.0;
      }
    }
  
    m_motor.set(speed);
  }

  public boolean isAtTop() {
    return m_topLimmit.get();
  }

  public boolean isAtBottom() {
    return m_bottomLimmit.get();
  }
}
