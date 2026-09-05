@Composable
fun Jitters(modifier: Modifier = Modifier) {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(5000)
        )
    }

    val text = "spatial experiment"
    val words = text.split(" ")

    val jitters = remember {
        words[0].map {
            Offset(
                x = Random.nextFloat() * 10f - 5f,
                y = Random.nextFloat() * 10f - 5f
            )
        }
    }

    val jatters = remember {
        words[1].map {
            Offset(
                x = Random.nextFloat() * 10f - 5f,
                y = Random.nextFloat() * 10f - 5f
            )
        }
    }

    Column(modifier = modifier) {
        Row {
            words[0].forEachIndexed { index, char ->
                Text(
                    text = char.toString(),
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.offset(
                        x = (jitters[index].x * progress.value).dp,
                        y = (jitters[index].y * progress.value).dp
                    )
                )
            }
        }
        Row {
            words[1].forEachIndexed { index, char ->
                Text(
                    text = char.toString(),
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.offset(
                        x = (jatters[index].x * progress.value).dp,
                        y = (jatters[index].y * progress.value).dp
                    )
                )
            }
        }
    }
}
