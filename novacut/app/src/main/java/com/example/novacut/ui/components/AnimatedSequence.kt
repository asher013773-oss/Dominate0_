@Composable
fun AnimatedSequence() {
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
     val produce = path.forEachIndexed(
         path[0].value + path - path[0].value * progress.value

       for (i in 0 until path.lastIndex) {
    drawLine(
        start = points[i],
        end = produce,
        color = Color.Black,
        strokeWidth = 8f
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
