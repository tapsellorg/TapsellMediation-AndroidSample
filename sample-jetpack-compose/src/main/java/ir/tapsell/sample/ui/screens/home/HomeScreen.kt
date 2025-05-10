package ir.tapsell.sample.ui.screens.home

import android.app.Activity
import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ir.tapsell.mediation.Tapsell
import ir.tapsell.sample.R
import ir.tapsell.sample.navigation.Routes
import ir.tapsell.shared.TestTags
import ir.tapsell.shared.R as ShR

private const val TAG = "HomeScreen"


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val activity = LocalActivity.current

    LaunchedEffect(Unit) {
        Tapsell.setInitializationListener {
            Log.d(TAG, "onInitializationComplete")
            Tapsell.setUserConsent(activity as Activity, true)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
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
                    .testTag(TestTags.REWARDED),
                onClick = {
                    navController.navigate(Routes.REWARDED_VIDEO)
                }
            ) {
                Text(text = stringResource(ShR.string.rewarded_video))
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(TestTags.INTERSTITIAL),
                onClick = {
                    navController.navigate(Routes.INTERSTITIAL)
                }
            ) {
                Text(text = stringResource(ShR.string.interstitial))
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(TestTags.BANNER),
                onClick = {
                    navController.navigate(Routes.STANDARD_BANNER)
                }
            ) {
                Text(text = stringResource(ShR.string.standard_banner))
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(TestTags.NATIVE),
                onClick = {
                    navController.navigate(Routes.NATIVE_BANNER)
                }
            ) {
                Text(text = stringResource(ShR.string.native_banner))
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(TestTags.PreRoll),
                onClick = {
                    navController.navigate(Routes.PRE_ROLL)
                }
            ) {
                Text(text = stringResource(ShR.string.preroll))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
