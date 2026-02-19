//DataTypes.kt
package com.example.annamstudyroomapp

import kotlinx.serialization.Serializable

@Serializable
object MainRoute

@Serializable
object StudyCardRoute

@Serializable
object AddCardRoute

@Serializable
object SearchCardRoute

@Serializable
object LogInRoute

@Serializable
data class TokenRoute(val email: String)

@Serializable
data class ShowCardRoute(val english: String, val vietnamese: String)

@Serializable
data class EditCardRoute(val engOld: String, val vietOld: String)

@Serializable
data class UserCredential (val email: String)

@Serializable
data class AudioCredential (val word: String, val email: String, val token: String)


//@Serializable
//data class UserToken (val token: String)

@Serializable
data class Response(val code: Int, val message: String)