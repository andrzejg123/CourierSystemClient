package pl.polsl.couriersystemclient.models

data class CarPatch(
    var name: String,
    
    var inUse: Boolean,

    var latLng: LatLng
)