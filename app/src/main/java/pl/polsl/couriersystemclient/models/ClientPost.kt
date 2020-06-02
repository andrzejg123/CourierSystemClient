package pl.polsl.couriersystemclient.models

data class ClientPost(
    val name: String,
    val surname: String,
    val email: String?,
    val phoneNumber: String
)