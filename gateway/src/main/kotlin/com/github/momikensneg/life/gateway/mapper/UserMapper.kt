package com.github.momikensneg.life.gateway.mapper

import com.github.momikensneg.life.gateway.dao.User
import com.github.momikensneg.life.gateway.dto.api.AuthRequest
import com.github.momikensneg.life.gateway.dto.user.UserDto
import com.github.momikensneg.life.gateway.dto.user.UserResponseDto
import com.github.momikensneg.life.gateway.dto.user.UserUpdateRequest
import com.github.momikensneg.lifelib.mapper.BaseMapper
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface UserMapper: BaseMapper<User, UserDto, UserResponseDto, UserUpdateRequest> {

    fun convertAuthToDto(user: AuthRequest): UserDto
}