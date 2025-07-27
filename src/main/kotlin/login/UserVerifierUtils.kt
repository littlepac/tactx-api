package org.example.login

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory

val verifier = GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
    .setAudience(listOf("636074117081-gfhjtuf0ie9fes2a1tdug4afsumd3j2m.apps.googleusercontent.com"))
    .build()

fun verifyIdToken(idTokenString: String): GoogleIdToken.Payload? {
    val idToken: GoogleIdToken? = verifier.verify(idTokenString)
    return idToken?.payload
}