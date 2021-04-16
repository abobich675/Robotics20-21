 package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;



@TeleOp(name="Basic Test Op Mode", group="Tests")
public class BasicBotTestOpMode extends LinearOpMode {
    private DcMotor leftDrive;
    private DcMotor rightDrive;
    private DcMotor motorOne;
    private DcMotor motorTwo;
    private ServoState servoOne;
    private ServoState servoTwo;
    private ServoState servoThree;
    private ServoState servoFour;
    private ElapsedTime runtime = new ElapsedTime();

    private static double DEFAULT_MOTOR_POWER = 0.5;
    private static double DEFAULT_LIFT_POS = 0.5;
    private static double DEFAULT_SCOOP_POS = 1;
    private static double MAXIMUM_SERVO_HIGHT = 0.8;
    private final double INCREMENT = 0.001;
    double incrementCoeffecient = 0.69;

    @Override
    public void runOpMode() {


        // go through and try to map various motors and servos based on some common names
        // if it fails (no config) it will throw an exception. we will catch and mark that
        // device as null so we can proceed but know later
        //
        // In a competition bot, you would probably want to fail, at least for critical
        // systems so you would know and be able to rectify the problem rather than having something
        // not work. But for testing, we want robustness and logging

        try {
            leftDrive = hardwareMap.get(DcMotor.class, "motor_left");
        } catch(Exception e) {
            leftDrive = null;
            telemetry.log().add("No motor_left configured");
        }
        try {
            rightDrive = hardwareMap.get(DcMotor.class, "motor_right");
        } catch(Exception e) {
            rightDrive = null;
            telemetry.log().add("No motor_right configured");
        }
        try {
            motorOne = hardwareMap.get(DcMotor.class, "motor_one");
        } catch(Exception e) {
            motorOne = null;
            telemetry.log().add("No motor_one configured");
        }
        try {
            motorTwo = hardwareMap.get(DcMotor.class, "motor_two");
        } catch(Exception e) {
            motorTwo = null;
            telemetry.log().add("No motor_two configured");
        }
        try {
            servoOne = new ServoState("servo_one", hardwareMap, telemetry, .7);
        } catch(Exception e) {
            servoOne = null;
            telemetry.log().add("No servo_one configured");
        }
        try {
            servoTwo = new ServoState("servo_two", hardwareMap, telemetry, .7);
        } catch(Exception e) {
            servoTwo = null;
            telemetry.log().add("No servo_two configured");
        }
        try {
            servoThree = new ServoState("servo_three", hardwareMap, telemetry, 1);
        } catch(Exception e) {
            servoThree = null;
            telemetry.log().add("No servo_three configured");
        }
//        try {
//            servoFour = new ServoState("servo_four", hardwareMap, telemetry);
//        } catch(Exception e) {
//            servoFour = null;
//            telemetry.log().add("No servo_four configured");
//        }

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        if (leftDrive != null) {
            leftDrive.setDirection(DcMotor.Direction.REVERSE);
        }
        if (rightDrive != null) {
            rightDrive.setDirection(DcMotor.Direction.FORWARD);
        }
        if (servoTwo != null) {
            servoTwo.servo.setDirection(Servo.Direction.REVERSE);
//            servoTwo.setPosition(DEFAULT_LIFT_POS);
//            servoThree.position = 1.0;
        }
//        if (servoOne != null) {
//            servoOne.servo.setPosition(DEFAULT_LIFT_POS);
//        }
//        if (servoThree != null) {
//            servoThree.servo.setPosition(DEFAULT_SCOOP_POS);
//        }

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Running");
            telemetry.update();



            /////////////////////////
            // Drive the robot
            ////////////////////////


            // Setup a variable for each drive wheel to save power level for telemetry
            double leftPower;
            double rightPower;

            // Choose to drive using either Tank Mode, or POV Mode
            // Comment out the method that's not used.  The default below is POV.

            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            double drive = -gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;
            leftPower = Range.clip(drive + turn, -1.0, 1.0) * 0.75;
            rightPower = Range.clip(drive - turn, -1.0, 1.0) * 0.75;

            // Tank Mode uses one stick to control each wheel.
            // - This requires no math, but it is hard to drive forward slowly and keep straight.
            // leftPower  = -gamepad1.left_stick_y ;
            // rightPower = -gamepad1.right_stick_y ;


            // Send calculated power to wheels
            if (leftDrive != null) {
                leftDrive.setPower(leftPower);
            } else {
                if (leftPower != 0) {
                    // wonder why when you're pressing the stick nothing is happening?
                    telemetry.log().add("No left motor configured");
                }
            }

            if (rightDrive != null) {
                rightDrive.setPower(rightPower);
            } else {
                if (rightPower != 0) {
                    // wonder why when you're pressing the stick nothing is happening?
                    telemetry.log().add("No right motor configured");
                }
            }


            //////////////////////////////////
            // Move the servos to do things
            //////////////////////////////////

            // overload x/b as both servoOne and servoTwo
            if (gamepad1.x) {
                if (servoOne != null && servoOne.position < MAXIMUM_SERVO_HIGHT) {
                    if (servoOne != null) {
                        servoOne.moveServo(true, INCREMENT);
                    }
                    if (servoTwo != null) {
                        servoTwo.moveServo(true, INCREMENT);
                    }
                    if (servoThree != null) {
                        servoThree.moveServo(false, incrementCoeffecient * INCREMENT);
                    }
                }
            }
            if (gamepad1.b) {
                if (servoOne != null && servoOne.position > 0) {
                    if (servoOne != null) {
                        servoOne.moveServo(false, INCREMENT);
                    }
                    if (servoTwo != null) {
                        servoTwo.moveServo(false, INCREMENT);
                    }
                    if (servoThree != null) {
                        servoThree.moveServo(true, incrementCoeffecient * INCREMENT);
                    }
                }
            }
//            if (gamepad1.y) {
//                incrementCoeffecient += 0.000001;
//            }
//            if (gamepad1.a) {
//                incrementCoeffecient -= 0.000001;
//            }
            /*if (gamepad1.y) {
                maxHeight();
            }
            if (gamepad1.a) {
                minHeight();
            }*/

            if (gamepad1.dpad_left && servoThree != null) { servoThree.moveServo(true, INCREMENT); }
            if (gamepad1.dpad_right && servoThree != null) { servoThree.moveServo(false, INCREMENT); }
            // if (gamepad1.dpad_up && servoFour != null) { servoFour.moveServo(true); }
            // if (gamepad1.dpad_down && servoFour != null) { servoFour.moveServo(false); }


            /////////////////////////
            // Move other motors
            ////////////////////////

            // Pull the right or the left trigger to make the motor move. Let go and they go back
            // to zero/stopped. Use the shoulder button to spin the other way

            if (motorOne != null) {

                // in a fancier bot we could remember our last power state and only adjust
                // if needed. we're not fancy here

                if (gamepad1.left_trigger >0) {
                    motorOne.setPower(DEFAULT_MOTOR_POWER);
                } else if (gamepad1.left_bumper) {
                    motorOne.setPower(-DEFAULT_MOTOR_POWER);
                } else {
                    motorOne.setPower(0);
                }
            }

            if (motorTwo != null) {

                // in a fancier bot we could remember our last power state and only adjust
                // if needed. we're not fancy here
                if (gamepad1.right_trigger >0) {
                    motorTwo.setPower(DEFAULT_MOTOR_POWER);
                } else if (gamepad1.right_bumper) {
                    motorTwo.setPower(-DEFAULT_MOTOR_POWER);
                } else {
                    motorTwo.setPower(0);
                }
            }


            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.addData("Increment Coeffecient:", incrementCoeffecient);

        }
    }
    public void wait(int time) {
        double startTime = runtime.seconds();
        while (opModeIsActive() && runtime.seconds() < (startTime + time)) {
        }
    }
    public void wait(double time) {
        double startTime = runtime.seconds();
        while (opModeIsActive() && runtime.seconds() < (startTime + time)) {
        }
    }
}
