package ir.tapsell.sample.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.viewinterop.AndroidView
import ir.tapsell.shared.ConsoleView
import ir.tapsell.shared.TestTags

@Composable
fun LogText(
    text: String,
    modifier: Modifier = Modifier,
) {
    AndroidView(
        factory = ::ConsoleView,
        modifier = modifier
            .fillMaxWidth()
            .testTag(TestTags.Log)
            .clipToBounds(),
        update = {
            it.text = text
        }
    )
}
