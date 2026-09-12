@Composable
fun AnimatedWord(
    word: String,
    startDelay: Long = 0L,
    modifier: Modifier = Modifier
) {
    val letters = remember(word) { word.toCharArray().toList() }
    val offsets = remember(word) { letters.map { Animatable(300f) } }
    val alphas = remember(word) { letters.map { Animatable(0f) } }
    val configuration = LocalConfiguration.current
    val positionx = (configuration.screenHeightDp * 0.5f).dp
    val positiony = (configuration.screenHeightDp * 0.3f).dp

    LaunchedEffect(word) {
        letters.indices.forEach { i ->
            launch {
                delay(startDelay + i * 40L)
                offsets[i].animateTo(
                    targetValue = 0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
            launch {
                delay(startDelay + i * 40L)
                alphas[i].animateTo(1f, tween(300))
            }
        }
    }

    Column(modifier = Modifier
        .padding(end = 8.dp)
        .offset(x = positionx, y = positiony) {
            Row{
                letters.take(5).forEachIndexed { i, c ->
                    Text(
                        text = c.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier
                       .offset(x = offsets[i].value.dp)
                       .alpha(alphas[i].value)
            )
        }
    }
            Row{
                letters.take(5).forEachIndexed { i, c ->
                    Text(
                        text = c.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier
                       .offset(x = offsets[i].value.dp)
                       .alpha(alphas[i].value)
            )
        }
    }
}
