package pl.polsl.couriersystemclient.datasource

interface RequestReadyCallback {
    fun onRequestReady(message: String, data: Any?)
}