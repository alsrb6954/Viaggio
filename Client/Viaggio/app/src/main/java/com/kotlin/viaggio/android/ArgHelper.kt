package com.kotlin.viaggio.android

sealed class ComponentArg

enum class ArgName {
    OCR_IMAGE_URI, EXTRA_TRANSITION_NAME
}

enum class WorkerName {
    TRAVELING_OF_DAY_CHECK, COMPRESS_IMAGE
}
