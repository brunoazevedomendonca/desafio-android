package com.picpay.desafio.android.data.mappers

import com.picpay.desafio.android.data.cache.model.UserCM
import org.junit.Test
import org.junit.Assert.*

class CacheToDomainMappersTest {

    @Test
    fun toDomainModel_userCM_returnUser() {
        //Given a UserCM
        val userCM = UserCM(1, "username", "name", "imageUrl")

        //When toDomainModel is called
        val result = userCM.toDomainModel()

        //Then return a User
        assertEquals(result.id, userCM.uid)
        assertEquals(result.username, userCM.username)
        assertEquals(result.name, userCM.name)
        assertEquals(result.imageUrl, userCM.imageUrl)
    }
}