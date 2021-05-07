package com.picpay.desafio.android.data.mappers

import com.google.common.truth.Truth.assertThat
import com.picpay.desafio.android.data.cache.model.UserCM
import org.junit.Test

class CacheToDomainMappersTest {

    @Test
    fun toDomainModel_userCM_returnUser() {
        //Given a UserCM
        val userCM = UserCM(1, "username", "name", "imageUrl")

        //When toDomainModel is called
        val result = userCM.toDomainModel()

        //Then return a User
        assertThat(result.id).isEqualTo(userCM.uid)
        assertThat(result.username).isEqualTo(userCM.username)
        assertThat(result.name).isEqualTo(userCM.name)
        assertThat(result.imageUrl).isEqualTo(userCM.imageUrl)
    }
}