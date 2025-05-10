package ir.tapsell.sample.ui.screens.rewarded

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.tapsell.sample.ui.components.LogText
import ir.tapsell.shared.TestTags
import ir.tapsell.shared.R as ShR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardedVideoScreen(
    modifier: Modifier = Modifier,
    viewModel: RewardedVideoViewModel = viewModel()
) {
    val context = LocalActivity.current as Activity
    val logMessage by viewModel.logMessage.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(ShR.string.rewarded_video))
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(TestTags.REQUEST),
                onClick = (viewModel::requestAd)
            ) {
                Text(text = stringResource(ShR.string.request))
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(TestTags.SHOW),
                enabled = viewModel.isShowButtonEnabled,
                onClick = { viewModel.showAd(context) }
            ) {
                Text(text = stringResource(ShR.string.show))
            }

            LogText(logMessage)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RewardedVideoScreenPreview() {
    RewardedVideoScreen()
}
