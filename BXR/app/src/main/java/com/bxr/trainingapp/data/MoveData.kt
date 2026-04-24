package com.bxr.trainingapp.data

import com.bxr.trainingapp.R
import com.bxr.trainingapp.model.Move

object MoveRepository {

    val moves = listOf(

        Move(
            name = "Jab",
            imageRes = R.drawable.move,
            description = "Straight punch to the front with your non-dominant hand.",
            criticalPoints = listOf(
                "Extend your lead hand straight without telegraphing.",
                "Snap the punch and retract quickly to guard.",
                "Keep your rear hand protecting your chin.",
                "Rotate your shoulder slightly to protect your jaw.",
                "Stay balanced—do not overcommit your weight."
            ),
            videoRes = R.raw.jab_preview,
            animRes = R.drawable.jab_anim
        ),

        Move(
            name = "Straight",
            imageRes = R.drawable.move,
            description = "Straight punch with your dominant hand.",
            criticalPoints = listOf(
                "Rotate your hips and shoulders to generate power.",
                "Pivot your rear foot as you throw the punch.",
                "Extend your rear hand fully, aiming straight ahead.",
                "Keep your lead hand up to guard your face.",
                "Return quickly to your stance after impact."
            ),
            videoRes = R.raw.straight_preview,
            animRes = R.drawable.straight_anim
        ),

        Move(
            name = "Lead Hook",
            imageRes = R.drawable.move,
            description = "Curled punch with your non-dominant hand.",
            criticalPoints = listOf(
                "Keep your elbow level with your fist during the hook.",
                "Rotate your lead foot and hips for power.",
                "Swing in a tight arc—do not loop too wide.",
                "Keep your rear hand up to guard your chin.",
                "Maintain balance—avoid leaning forward."
            ),
            videoRes = R.raw.front_hook_preview,
            animRes = R.drawable.leadhook_anim
        ),

        Move(
            name = "Rear Hook",
            imageRes = R.drawable.move,
            description = "Curled punch with your dominant hand.",
            criticalPoints = listOf(
                "Pivot your rear foot and rotate your hips strongly.",
                "Keep the hook compact and controlled.",
                "Your lead hand should stay up to defend.",
                "Avoid dropping your hand before throwing.",
                "Return quickly to guard after the punch."
            ),
            videoRes = R.raw.rear_hook_preview,
            animRes = R.drawable.rearhook_anim
        ),

    )
}