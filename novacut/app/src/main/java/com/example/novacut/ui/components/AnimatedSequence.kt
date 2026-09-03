@Composable
fun DrawingLine() {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(1000)
        )
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
    ) {
            val startX = SequenceHeight.width * 0.5f
            val startY = SequenceHeight.height * 0.0f

            val endX = RoraWide.size.width * 0.0f
            val endY = RoraWide.size.height / 2
       }

     Path().apply {
         moveTo(startX, startY)

    cubicTo(
        SequenceHeight.width * 0.6 , SequenceHeight.height * 0.3 ,
        SequenceHeight.width * 0.4 , SequenceHeight.height * 0.5 ,
        endX, endY
    )
}

        drawLine(
            color = Color.Black,
            start = start,
            end = currentEnd,
            strokeWidth = 8f
        )
    }
}
