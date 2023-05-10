package com.github.momikensneg.life.gateway.services

import com.github.momikensneg.life.gateway.config.security.PBKDF2Encoder
import com.github.momikensneg.life.gateway.dao.User
import com.github.momikensneg.life.gateway.dao.enums.PetState
import com.github.momikensneg.life.gateway.dao.enums.RoleName
import com.github.momikensneg.life.gateway.dto.user.UserDto
import com.github.momikensneg.life.gateway.exceptions.ForbiddenException
import com.github.momikensneg.life.gateway.exceptions.UserAlreadyExistsException
import com.github.momikensneg.life.gateway.exceptions.UserNotFoundException
import com.github.momikensneg.life.gateway.mapper.UserMapper
import com.github.momikensneg.life.gateway.repository.UserRepository
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PBKDF2Encoder,
    private val userMapper: UserMapper
): BaseService<UserDto, UUID> {

    override fun findById(id: UUID): Mono<UserDto> =
        userRepository.findById(id)
            .switchIfEmpty(
                Mono.error(UserNotFoundException("User with id $id not found"))
            )
            .map(userMapper::convertEntityToDto)

    fun findByUsername(userName: String): Mono<UserDto> =
        userRepository.findByLogin(userName)
            .switchIfEmpty(
                Mono.error(UsernameNotFoundException("User with login $userName not found"))
            ).map(userMapper::convertEntityToDto)

    fun listUsers(userName: String): Flux<UserDto> {
        return userRepository.findLikeUsername(userName)
            .map(userMapper::convertEntityToDto)
    }

    @Transactional
    override fun create(dto: UserDto): Mono<UserDto> {
        dto.roleName = RoleName.ROLE_USER
        dto.health = 100.0
        dto.money = 100
        dto.state = PetState.NORMAL
        dto.password = passwordEncoder.encode(dto.password)
        return userRepository.findByLogin(dto.login)
            .flatMap {
                Mono.error<User>(UserAlreadyExistsException(dto.login))
            }.switchIfEmpty(
                userRepository.save(userMapper.convertDtoToEntity(dto))
            ).map(userMapper::convertEntityToDto)
    }

    @Transactional
    override fun update(id: UUID, user: UserDto): Mono<UserDto> =
        Mono.zip(ReactiveSecurityContextHolder.getContext(), userRepository.findById(id))
            .filter { tuple ->
                tuple.t1.authentication.name == "admin" || tuple.t1.authentication.name == tuple.t2.login
            }
            .switchIfEmpty(Mono.error(ForbiddenException("You can not update user with id $id")))
            .map { tuple -> tuple.t2 }
            .flatMap { u ->
                val newUser = userMapper.convertToEntityFromDto(user, u)
                userRepository.update(newUser)
            }.map(userMapper::convertEntityToDto)

    override fun delete(user: UserDto): Mono<Void> =
        Mono.just(user)
            .map(userMapper::convertDtoToEntity)
            .flatMap(userRepository::delete)

}

