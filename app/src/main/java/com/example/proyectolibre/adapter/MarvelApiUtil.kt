package com.example.proyectolibre.adapter

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object MarvelApiUtil {

    private const val privateKey = "ba41a2c4a5898d7d6de2057044f1f30c" // Coloca tu clave privada aquí
    private const val publicKey = "ba41a2c4a5898d7d6de2057044f1f30c" // Tu clave pública

    fun generateHash(timestamp: String): String {
        val input = "$timestamp$privateKey$publicKey"
        return input.md5() // Asumiendo que has implementado una función de extensión md5
    }

    private fun String.md5(): String {
        val md = MessageDigest.getInstance("MD5")
        val messageDigest = md.digest(this.toByteArray())
        return messageDigest.joinToString("") { "%02x".format(it) }
    }
}
