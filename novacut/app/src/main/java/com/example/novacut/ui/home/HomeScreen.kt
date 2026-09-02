
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.foundation.layout.height

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    val configuration = LocalConfiguration.current
    val squirclesHeight = (configuration.screenHeightDp * 0.3f).dp

    val infiniteTransition = rememberInfiniteTransition(label = "ColorLoop")

    val blendedColor by infiniteTransition.animateColor(
        initialValue = Color(0xFFD9F4DA),
        targetValue = Color(0xFF81C784),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 11000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ColorBlend"
    )

    var selectedTab by remember { mutableStateOf(HomeTab.EDITS) }
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(blendedColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AnimatedTwoWords()
            SlidingSpinningSquircles(modifier = Modifier.height(squirclesHeight))
            HomeTabRow(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    }
}
