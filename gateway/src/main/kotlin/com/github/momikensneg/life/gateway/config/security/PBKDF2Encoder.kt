package com.github.momikensneg.life.gateway.config.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec


@Component
class PBKDF2Encoder(
    @Value("\${spring-webflux-jjwt.password.encoder.secret}")
    private val secret: String,
    @Value("\${spring-webflux-jjwt.password.encoder.iteration}")
    private val iteration: Int,
    @Value("\${spring-webflux-jjwt.password.encoder.key-length}")
    private val keyLength: Int,
    @Value("\${spring-webflux-jjwt.password.encoder.algorithm}")
    private val algorithm: String): PasswordEncoder {

    override fun encode(cs: CharSequence?): String {
        try {
            val result = SecretKeyFactory.getInstance(algorithm)
                .generateSecret(PBEKeySpec(cs.toString().toCharArray(), secret.toByteArray(), iteration, keyLength))
                .encoded
            return Base64.getEncoder().encodeToString(result)
        } catch (ex: NoSuchAlgorithmException) { // TODO:
            throw RuntimeException(ex)
        } catch (ex: InvalidKeySpecException) {
            throw RuntimeException(ex)
        }
    }

    @Override
    override fun matches(cs: CharSequence, string: String): Boolean {
        return encode(cs) == string;
    }

}