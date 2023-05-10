package com.github.momikensneg.life.gateway.dao

import com.github.momikensneg.life.gateway.dao.enums.PetState
import com.github.momikensneg.life.gateway.dao.enums.RoleName
import com.github.momikensneg.lifelib.annotation_processor.dto_annotations.DtoField
import com.github.momikensneg.lifelib.annotation_processor.dto_annotations.ResponseApiField
import com.github.momikensneg.lifelib.annotation_processor.dto_annotations.UpdateApiField
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@Table(name = "life_user")
class User(
    @Id
    @DtoField(true)
    @ResponseApiField(false)
    var id: UUID? = null,

    @DtoField(false)
    @ResponseApiField(false)
    @UpdateApiField(false)
    var login: String,

    @JvmField
    @DtoField(false)
    @UpdateApiField(false)
    var password: String,

    @DtoField(true)
    @ResponseApiField(false)
    var health: Double,

    @DtoField(true)
    @ResponseApiField(false)
    @UpdateApiField(true)
    var money: Int,

    @DtoField(true)
    @ResponseApiField(false)
    var state: PetState,

    @DtoField(true)
    @ResponseApiField(false)
    @UpdateApiField(false)
    @Column("role_name")
    var roleName: RoleName
) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        mutableListOf(SimpleGrantedAuthority(roleName.name))

    override fun getPassword(): String = password

    override fun getUsername(): String = login

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}
//{
//    @PersistenceCreator
//    constructor(id: UUID) : this(id, "unknown")
//}