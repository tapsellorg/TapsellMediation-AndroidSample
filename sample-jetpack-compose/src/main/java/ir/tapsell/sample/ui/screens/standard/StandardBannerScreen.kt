package ir.tapsell.sample.ui.screens.standard

import android.app.Activity
import android.view.LayoutInflater
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.tapsell.mediation.ad.views.banner.BannerContainer
import ir.tapsell.sample.R
import ir.tapsell.sample.ui.components.LogText
import ir.tapsell.shared.R as ShR


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandardBannerScreen(
    modifier: Modifier = Modifier,
    viewModel: StandardBannerViewModel = viewModel(),
) {
    val context = LocalActivity.current as Activity
    val logMessage by viewModel.logMessage.collectAsState()

    DisposableEffect(Unit) {
        onDispose(viewModel::destroyAd)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(ShR.string.standard_banner))
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
                onClick = { viewModel.showAd(context) }
            ) {
                Text(text = stringResource(ShR.string.show))
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = viewModel.isDestroyButtonEnabled,
                onClick = viewModel::destroyAd
            ) {
                Text(text = stringResource(ShR.string.destroy))
            }

            LogText(logMessage)

            TapsellStandardBannerView(
                modifier = modifier.wrapContentSize(),
                onUpdate = viewModel::updateContainer
            )
        }
    }
}

@Composable
private fun TapsellStandardBannerView(
    modifier: Modifier = Modifier,
    onUpdate: (BannerContainer) -> Unit = {},
) {
    val activity = LocalActivity.current as Activity
    val container = BannerContainer(activity)
    AndroidView(
        modifier = modifier,
        factory = {
            val view =
                LayoutInflater.from(activity)
                    .inflate(R.layout.banner_container, container, true)
            onUpdate(view as BannerContainer)
            view
        }
    )
}

@Preview(showBackground = true)
@Composable
fun StandardBannerScreenPreview() {
    StandardBannerScreen()
}
