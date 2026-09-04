@Composable
fun MyText(modifier = Modifier.Offset (caucaPos)) {
    val progress = remember (Animatable {0f} )
    progress.animateTo(
        targetValue = 1f,
            animationSpec = tween(5000)
     )

    val text = "satial experiment"
    val words = text.split(" ")
     Column {
            Text(
                text = words[0]) 
                style = MaterialTheme.AppTypography.displaySmall
                size = 4.dp
                modifier = Modifier
                .onGloballyPositioned { coordinates ->
        val position = coordinates.positionInRoot()
                .offset( position + x = 5.dp, y = 5.dp * progress.value)
           )

        Text(
            text = words[1])
            style = MaterialTheme.AppTypography.displaySmall
            size = 4.dp
            modifier = Modifier
            .onGloballyPositioned { coordinates ->
        val position = coordinates.positionInRoot()
            .offset( position + x = -5.dp, y = -5.dp * progress.value)
    )
}
