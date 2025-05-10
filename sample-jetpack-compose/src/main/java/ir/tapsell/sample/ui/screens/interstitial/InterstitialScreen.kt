package ir.tapsell.sample.ui.screens.interstitial

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.tapsell.sample.ui.components.LogText
import ir.tapsell.shared.R as ShR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterstitialScreen(
    modifier: Modifier = Modifier,
    viewModel: InterstitialViewModel = viewModel()
) {
    val activity = LocalActivity.current
    val logMessage by viewModel.logMessage.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(ShR.string.interstitial))
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
                modifier = Modifier.fillMaxWidth(),
                onClick = (viewModel::requestAd)
            ) {
                Text(text = stringResource(ShR.string.request))
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = viewModel.isShowButtonEnabled,
                onClick = { viewModel.showAd(activity) }
            ) {
                Text(text = stringResource(ShR.string.show))
            }

            LogText(logMessage)
        }
    }


}

@Preview(showBackground = true)
@Composable
fun InterstitialScreenPreview() {
    InterstitialScreen()
}
