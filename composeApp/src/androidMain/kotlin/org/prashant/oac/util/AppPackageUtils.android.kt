package org.prashant.oac.util

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.pm.PackageInfoCompat.getLongVersionCode
import oac.composeapp.generated.resources.Res
import oac.composeapp.generated.resources.app_not_installed
import org.jetbrains.compose.resources.stringResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.prashant.oac.domain.model.App
import org.prashant.oac.domain.model.AppDetails
import org.prashant.oac.util.AppHelperUtils.fetchAppLists
import org.prashant.oac.util.AppHelperUtils.getAppButtons
import org.prashant.oac.util.AppHelperUtils.getScriptLinks
import org.prashant.oac.util.AppHelperUtils.releaseType

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object AppPackageUtils : KoinComponent {
    private val context: Context by inject()

    actual suspend fun getAppPackages(): List<AppDetails> {
        return (fetchAppLists("AndroidPackage.json") {
            Log.d("AppPackageUtils", "readFromAssets: ${it.localizedMessage}")
        }?.mapNotNull { getAppDetails(it) } ?: emptyList()).toMutableList()
    }


    actual fun getAppDetails(app: App): AppDetails? {
        return try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(app.packageName, 0)
            val appName = packageManager.getApplicationLabel(
                packageManager.getApplicationInfo(
                    app.packageName, 0
                )
            )
            val appIcon: Drawable = packageManager.getApplicationIcon(app.packageName)
            val versionName = packageInfo.versionName
            val versionCode = getLongVersionCode(packageInfo)
            val isDebug =
                (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

            AppDetails(
                appControlButtons = app.getAppButtons(),
                deepLinkScript = app.getScriptLinks(),
                icon = drawableToImageBitmap(appIcon),
                isDebug = isDebug,
                packageName = app.packageName,
                name = appName.toString(),
                releaseType = app.packageName.releaseType,
                version = versionName,
                versionCode = versionCode

            )
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

    private fun String.packageInfo(): PackageInfo? {
        return try {
            val packageManager = context.packageManager
            packageManager.getPackageInfo(this, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

    private fun String.isPackageInstalled(): Boolean {
        return packageInfo() != null
    }

    private fun String.appName(): String {
        if (!isPackageInstalled()) {
            return ""
        }
        return context.packageManager.getApplicationLabel(
            context.packageManager.getApplicationInfo(
                this, 0
            )
        ).toString()
    }

    @SuppressLint("ComposableNaming")
    @Composable
    actual fun launchApp(scriptLink: String) {
        val context = LocalContext.current
        context.launch(scriptLink, stringResource(resource = Res.string.app_not_installed))
    }

    private fun Context.launch(scriptLink: String, message: String) {
        val url: Uri = Uri.parse(scriptLink)
        val packageName = url.host ?: return

        Log.d(
            "AppPackageUtils",
            "launch: url: $url, host: ${url.host}, scriptLink: $scriptLink, packageName: $packageName"
        )

        try {
            val intent = when {
                url.pathSegments.isNullOrEmpty() -> {
                    // If the URL has no path, launch the app directly
                    packageManager.getLaunchIntentForPackage(packageName)?.apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    }
                }

                else -> {
                    // Launch the app with the deep link
                    Intent(Intent.ACTION_VIEW, url).apply {
                        setPackage(packageName)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    }
                }
            }

            if (intent != null) {
                this.startActivity(intent)
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT)
                    .show()
                Log.e(
                    "AppPackageUtils",
                    "App not installed or intent could not be created: $packageName"
                )
            }
        } catch (exc: ActivityNotFoundException) {
            Toast.makeText(this, "App doesn't support this action!", Toast.LENGTH_SHORT)
                .show()
            Log.e("AppPackageUtils", "launchException: ${exc.localizedMessage}")
        } catch (exception: Exception) {
            Log.e("AppPackageUtils", "launchException2: ${exception.localizedMessage}")
        }
    }

    private fun drawableToImageBitmap(drawable: Drawable): ImageBitmap {
        val bitmap = (drawable as? BitmapDrawable)?.bitmap ?: run {
            // Convert Drawable to Bitmap
            val bitmapDrawable = drawable.toBitmap()
            bitmapDrawable
        }
        return bitmap.asImageBitmap()
    }

    private fun Drawable.toBitmap(): Bitmap {
        if (this is BitmapDrawable) {
            return this.bitmap
        }

        // Create a Bitmap of the drawable
        val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        setBounds(0, 0, canvas.width, canvas.height)
        draw(canvas)
        return bitmap
    }


}