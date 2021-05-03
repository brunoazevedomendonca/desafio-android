package com.picpay.desafio.android.data.mappers

import com.picpay.desafio.android.data.remote.model.UserRM
import org.junit.Assert.assertEquals
import org.junit.Test

class RemoteToCacheMappersTest {

    @Test
    fun toCacheModel_userCM_returnUserCM() {
        //Given a UserRM
        val userRM = UserRM(1, "username", "name", "imageUrl")

        //When toCacheModel is called
        val result = userRM.toCacheModel()

        //Then return a UserCM
        assertEquals(result.uid, userRM.id)
        assertEquals(result.username, userRM.username)
        assertEquals(result.name, userRM.name)
        assertEquals(result.imageUrl, userRM.img)
    }
}