package org.prashant.oac.util

import android.os.Build

actual object PlatformUtils {
    actual val isDynamicColorSupported: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
}