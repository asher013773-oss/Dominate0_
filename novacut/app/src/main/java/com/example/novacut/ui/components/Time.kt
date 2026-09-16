import androidx.compose.runtime.Composable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateTo
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.withFrameNanos
import androidx.compose.runtime.remember
import androidx.compose.foundation.Canvas
import kotlin.random.Random
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.draw.clip 
import kotlinx.coroutines.delay
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

data class DustParticle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    var radius: Float,
    var alpha: Float
)

@Composable
fun JustHere() {
    Card(
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
            DustEffect()
        }
    }
}

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
        }
    }

    var frameTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos { time ->
                frameTime = time
                for (p in particles) {
                    p.x += p.vx
                    p.y += p.vy
                    if (p.x < 0f) p.x = 1f
                    if (p.x > 1f) p.x = 0f
                    if (p.y < 0f) p.y = 1f
                    if (p.y > 1f) p.y = 0f
                }
            }
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        @Suppress("UNUSED_EXPRESSION") frameTime

        for (p in particles) {
            drawCircle(
                color = Color.White.copy(alpha = p.alpha),
                radius = p.radius,
                center = Offset(p.x * size.width, p.y * size.height)
            )
        }
    }
}

@Composable
fun AnimatedWord(modifier: Modifier = Modifier) {
    Text("Aurora Reinvigorate")
}

@Composable
fun Wait(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Text(
            text = "Pro?",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
