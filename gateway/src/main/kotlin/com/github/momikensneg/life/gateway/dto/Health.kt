package com.github.momikensneg.life.gateway.dto

import org.springframework.boot.actuate.health.Status

data class Health(val status: Status)
