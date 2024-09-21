package org.prashant.oac.util

import androidx.compose.runtime.Composable
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import oac.composeapp.generated.resources.Res
import oac.composeapp.generated.resources.app_not_installed
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorSpace
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.Image
import org.jetbrains.skia.ImageInfo
import org.koin.core.component.KoinComponent
import org.prashant.oac.domain.model.App
import org.prashant.oac.domain.model.AppDetails
import org.prashant.oac.util.AppHelperUtils.fetchAppLists
import org.prashant.oac.util.AppHelperUtils.getAppButtons
import org.prashant.oac.util.AppHelperUtils.getScriptLinks
import org.prashant.oac.util.AppHelperUtils.releaseType
import platform.CoreFoundation.CFDataGetBytePtr
import platform.CoreFoundation.CFDataGetLength
import platform.CoreFoundation.CFRelease
import platform.CoreGraphics.CGDataProviderCopyData
import platform.CoreGraphics.CGImageAlphaInfo
import platform.CoreGraphics.CGImageGetAlphaInfo
import platform.CoreGraphics.CGImageGetBytesPerRow
import platform.CoreGraphics.CGImageGetDataProvider
import platform.CoreGraphics.CGImageGetHeight
import platform.CoreGraphics.CGImageGetWidth
import platform.CoreGraphics.CGImageRelease
import platform.Foundation.NSBundle
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIImage


@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object AppPackageUtils : KoinComponent {
//    private val mainScope = CoroutineScope(Dispatchers.Main)

    actual suspend fun getAppPackages(): List<AppDetails> {
        return (fetchAppLists("iOSPackage.json")?.mapNotNull { getAppDetails(it) }
            ?: emptyList()).toMutableList()
    }

    @Composable
    actual fun launchApp(scriptLink: String) {
        launchAppOnIOS(scriptLink, stringResource(resource = Res.string.app_not_installed))
    }

    actual fun getAppDetails(app: App): AppDetails? {
        try {

            val bundle = NSBundle.bundleWithIdentifier(app.packageName) //?: NSBundle.mainBundle
            val appName = bundle?.objectForInfoDictionaryKey("CFBundleDisplayName") as? String
                ?: bundle?.objectForInfoDictionaryKey("CFBundleName") as? String
            val version =
                bundle?.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String
            val versionCode = bundle?.objectForInfoDictionaryKey("CFBundleVersion") as? String
            val isDebug = bundle?.objectForInfoDictionaryKey("Debug") as? Boolean ?: false

            // Return null if any of the essential fields are missing
            if (appName == null || version == null || versionCode == null) {
                return null
            }

            return AppDetails(
                appControlButtons = app.getAppButtons(),
                deepLinkScript = app.getScriptLinks(),
//                icon = appIcon.toSkiaImage()?.toComposeImageBitmap(),
                isDebug = isDebug,
                name = appName,
                packageName = app.packageName,
                releaseType = app.packageName.releaseType,
                version = version,
                versionCode = versionCode.toLongOrNull() ?: 1
            )
        } catch (exception: Exception) {
            println("Package getDetails error: ${exception.message}, ${exception.cause}")
            return null
        }
    }


    private fun launchAppOnIOS(scriptLink: String, message: String) {
        val url = NSURL(string = scriptLink)
        print("LaunchApp launchAppOnIOS: $scriptLink")
        url.let {
            if (UIApplication.sharedApplication.canOpenURL(it)) {
                UIApplication.sharedApplication.openURL(it)
            } else {
                println(message)
            }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun UIImage.toSkiaImage(): Image? {
        val imageRef = this.CGImage ?: return null

        val width = CGImageGetWidth(imageRef).toInt()
        val height = CGImageGetHeight(imageRef).toInt()

        val bytesPerRow = CGImageGetBytesPerRow(imageRef)
        val data = CGDataProviderCopyData(CGImageGetDataProvider(imageRef))
        val bytePointer = CFDataGetBytePtr(data)
        val length = CFDataGetLength(data)

        val alphaType = when (CGImageGetAlphaInfo(imageRef)) {
            CGImageAlphaInfo.kCGImageAlphaPremultipliedFirst,
            CGImageAlphaInfo.kCGImageAlphaPremultipliedLast -> ColorAlphaType.PREMUL

            CGImageAlphaInfo.kCGImageAlphaFirst,
            CGImageAlphaInfo.kCGImageAlphaLast -> ColorAlphaType.UNPREMUL

            CGImageAlphaInfo.kCGImageAlphaNone,
            CGImageAlphaInfo.kCGImageAlphaNoneSkipFirst,
            CGImageAlphaInfo.kCGImageAlphaNoneSkipLast -> ColorAlphaType.OPAQUE

            else -> ColorAlphaType.UNKNOWN
        }

        val byteArray = ByteArray(length.toInt()) { index ->
            bytePointer?.get(index)?.toByte()!!
        }

        CFRelease(data)
        CGImageRelease(imageRef)

        val skiaColorSpace = ColorSpace.sRGB
        val colorType = ColorType.RGBA_8888

        // Convert RGBA to BGRA
        for (i in byteArray.indices step 4) {
            val r = byteArray[i]
            val g = byteArray[i + 1]
            val b = byteArray[i + 2]
            val a = byteArray[i + 3]

            byteArray[i] = b
            byteArray[i + 2] = r
        }

        return Image.makeRaster(
            imageInfo = ImageInfo(
                width = width,
                height = height,
                colorType = colorType,
                alphaType = alphaType,
                colorSpace = skiaColorSpace
            ),
            bytes = byteArray,
            rowBytes = bytesPerRow.toInt(),
        )
    }
}