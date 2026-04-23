package com.bxr.trainingapp.data

import com.bxr.trainingapp.R
import com.bxr.trainingapp.model.HowToContent
import com.bxr.trainingapp.model.HowToItem

object HowToData {

    val list = listOf(

        HowToItem(
            title = "General",
            content = listOf(
                HowToContent.Image(R.drawable.practice),
                HowToContent.Text("Clicking the practice button will open the list of moves available that you can train. " +
                        "Each move comes with a Tutorial tab that shows you how it is executed, and a Logs tab that shows you all previously " +
                        "recorded training sessions for that move."),
                HowToContent.Image(R.drawable.settings),
                HowToContent.Text("You may change your name and handedness here, as well as which camera is used during the training sessions. " +
                        "You can also toggle the skeletal overlay on or off.\n" +
                        "\n" +
                        "You can also choose to clear some of your training logs here.")
            )
        ),

        HowToItem(
            title = "Camera",
            content = listOf(
                HowToContent.Text("During the training session, your phone must be placed somewhere it can stay upright and your whole body is visible to the camera (around 2 meters away from you)."),
                HowToContent.Text("Your body must be oriented in such a way that you are oriented sideways, with your striking hand is facing the camera."),
                HowToContent.Image(R.drawable.move)
            )
        ),

        HowToItem(
            title = "Training Sessions",
            content = listOf(
                HowToContent.Text("Your body must be oriented in such a way that you are oriented sideways, with your striking hand is facing the camera."),
                HowToContent.Image(R.drawable.move),
                HowToContent.Text("Once you have a proper Guard Stance, the application will display"),
                HowToContent.Subtitle("Go!"),
                HowToContent.Text("prompting you to perform a punch."),
                HowToContent.Image(R.drawable.move),
                HowToContent.Text("Once a punch is performed correctly, if one of these errors appears at any point in between entering the Guard stance and the rep counting, it will return an Incorrect Rep. Otherwise, if no errors appear meaning that the punch was executed correctly after the Guard Stance, it will return Correct Rep."),
                HowToContent.Subtitle("After each punch, it will wait again until you enter Guard Stance correctly, and will again say Go! when you have."),
                HowToContent.Text("You may click End Session to end the current training session and it will be saved in a log that can be accessed by the Logs tab under a move.")
            )
        ),

        HowToItem(
            title = "Error Types",
            content = listOf(
                HowToContent.Subtitle("Punch Not Straight"),
                HowToContent.Text("Your punch deviates from the correct path."),

                HowToContent.Subtitle("Punch Not Fully Extended"),
                HowToContent.Text("Your arm does not reach full extension."),

                HowToContent.Subtitle("Low Guard"),
                HowToContent.Text("Your non-punching hand drops too low."),

                HowToContent.Subtitle("Improper Stance"),
                HowToContent.Text("Your body is not aligned correctly during the punch.")
            )
        )

    )
}