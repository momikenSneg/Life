package com.github.momikensneg.life.gateway.config.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JWTUtil(
    @Value("\${spring-webflux-jjwt.jjwt.secret}")
    private val secret: String,
    @Value("\${spring-webflux-jjwt.jjwt.expiration}")
    private val expirationTime: String,
    @Value("\${spring-webflux-jjwt.role}")
    private val role: String) {

    private val key: Key = Keys.hmacShaKeyFor(secret.toByteArray())

    fun getAllClaimsFromToken(token: String): Claims =
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body

    fun getUsernameFromToken(token: String): String =
        getAllClaimsFromToken(token).subject

    fun getExpirationDateFromToken(token: String): Date =
        getAllClaimsFromToken(token).expiration

    private fun isTokenExpired(token: String): Boolean =
        getExpirationDateFromToken(token).before(Date())

    fun generateToken(login: String, roleName: String): String {
        val claims: Map<String, Any> = mutableMapOf(role to mutableListOf(roleName))
        return doGenerateToken(claims, login)
    }

    private fun doGenerateToken(claims: Map<String, Any>, userName: String?): String {
        val expirationTimeLong = expirationTime.toLong() //in second
        val createdDate = Date()
        val expirationDate = Date(createdDate.time + expirationTimeLong * 1000)
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userName)
            .setIssuedAt(createdDate)
            .setExpiration(expirationDate)
            .signWith(key)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return !isTokenExpired(token)
    }

}