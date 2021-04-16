package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ServoState {
    public Servo servo;
    public String name;
    public boolean goingUp;
    public double position;
    private Telemetry telemetry;

    static final double INCREMENT   = 0.001;     // amount to slew servo each CYCLE_MS cycle
    static final double MAX_POS     =  1.0;     // Maximum rotational position
    static final double MIN_POS     =  0.0;     // Minimum rotational position

    public ServoState(String myName, HardwareMap hardwareMap, Telemetry myTelemetry, double startPosition) {
        name = myName;
        servo = hardwareMap.get(Servo.class, name);
        telemetry = myTelemetry;

        if (servo == null) {
            telemetry.log().add("unable to map servo "+name+". might not exist");
            return;
        }
        goingUp = true;
//        position = (MAX_POS - MIN_POS) / 2; // Start at halfway position
        position = startPosition;
        servo.setPosition(position);
    }

//    public void setPosition(double pos) {
//        position = pos;
//        this.setPosition(position);
//    }

    public void moveServo(boolean up, double increment) {
        goingUp = up;
        this.moveServo(increment);
    }

    public void moveServo(double increment) {

        if (servo == null) {
            telemetry.log().add("servo "+name+" not initialized, cannot move");
            return;
        }

        if (goingUp) {
            // Keep stepping up until we hit the max value.
            position += increment;
            if (position >= MAX_POS) {
                position = MAX_POS;
            }
            telemetry.log().add("servo "+name+" going up to "+position);
        } else {
            // Keep stepping down until we hit the min value.
            position -= increment;
            if (position <= MIN_POS) {
                position = MIN_POS;
            }
            telemetry.log().add("servo "+name+" going down to "+position);
        }

        // Set the servo to the new position and pause;
        servo.setPosition(position);
    }
    public void servoPosition(double targetPosition, double increment) {
        increment = (increment != 0) ? increment : INCREMENT;
        if (targetPosition >= MAX_POS) {
            targetPosition = MAX_POS;
        }
        if (targetPosition <= MIN_POS) {
            targetPosition = MIN_POS;
        }
        while (position < targetPosition - increment){
            moveServo(true, increment);
        }
        while (position > targetPosition + increment){
            moveServo(false, increment);
        }
    }
}
