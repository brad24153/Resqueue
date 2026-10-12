package com.blsoftware.resqueue.utils

import com.blsoftware.resqueue.R

class StringUtils {

    val Boolean.toHasDoneWellNoResId: Int
        get() = if (this) R.string.has_done_well else R.string.No
}