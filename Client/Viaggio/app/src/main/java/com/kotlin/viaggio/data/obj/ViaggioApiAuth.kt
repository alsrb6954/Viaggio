package com.kotlin.viaggio.data.obj

data class ViaggioApiAuth(
    var email: String = "",
    var name: String = "",
    val token: String
)
data class ViaggioApiAWSAuth(
    val AWS_IdentityId: String, val AWS_Token: String
)

data class ViaggioResult(
    var result: String = ""
)
data class ViaggioTravelResult(
    var id: Int= 0
)

