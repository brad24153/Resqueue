package com.blsoftware.resqueue.data

sealed class DataException(cause: Throwable? = null) : Exception(cause) {
    class NetworkError(cause: Throwable) : DataException(cause = cause)
    class UnknownError(cause: Throwable) : DataException(cause = cause)
}