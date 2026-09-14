import androidx.compose.runtime.Composable
import kotlin.random.Random
import androidx.compose.material3.Card
import androidx.compose.ui.graphics.drawscope.drawCircle
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme

Card (
  shape = RoundedCornerShape(16.dp),
  elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 2.dp
        )
    ) {
        BoxWithConstraints(
            modifier = Modifier.size(width = 50.dp, height = 50.dp)
        ) {
            val boundWidth = maxWidth
            val boundHeight = maxHeight

            data class DustParticle(
                var x: Float,
                var y: Float,
                var vx: Float,
                var vy: Float,
                var radius: Float,
                var alpha: Float
)

@Composable
fun DustEffect(
    modifier: Modifier = Modifier,
    particleCount: Int = 40
) {
    val particles = remember {
        List(particleCount) {
            DustParticle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                vx = (Random.nextFloat() - 0.5f) * 0.02f,
                vy = (Random.nextFloat() - 0.5f) * 0.02f,
                radius = Random.nextFloat() * 3f + 1f,
                alpha = Random.nextFloat() * 0.5f + 0.2f
                )
            )
        }
    }

    // single state value just to force recomposition of the Canvas each frame
    var frameTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos { time ->
                frameTime = time
                for (p in particles) {
                    p.x += p.vx
                    p.y += p.vy
                    // wrap around edges
                    if (p.x < 0f) p.x = 1f
                    if (p.x > 1f) p.x = 0f
                    if (p.y < 0f) p.y = 1f
                    if (p.y > 1f) p.y = 0f
                }
            }
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) ) {
        @Suppress("UNUSED_EXPRESSION") frameTime

        val w = maxWidth
        val h = maxHeight
        for (p in particles) {
            drawCircle(
                color = Color.White.copy(alpha = p.alpha),
                radius = p.radius,
                center = Offset(p.x * w, p.y * h)
            )
        }
    }

Row(modifier = Modifier
    .offset(x = maxWidth * 0.9, y = maxHeight * 0.9)
    ) {
  Text(
    text = "Pro?"
    style = MaterialTheme.typography.headlineMedium
    )
}
