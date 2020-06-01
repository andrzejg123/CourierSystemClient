package pl.polsl.couriersystemclient.models

import java.io.Serializable

data class ClientGet(
    var id: Long,
    var name: String,
    var surname: String,
    var email: String,
    var phoneNumber: String
) : Serializable