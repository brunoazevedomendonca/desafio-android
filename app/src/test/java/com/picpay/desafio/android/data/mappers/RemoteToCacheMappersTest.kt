package com.picpay.desafio.android.data.mappers

import com.google.common.truth.Truth.assertThat
import com.picpay.desafio.android.data.remote.model.UserRM
import org.junit.Test

class RemoteToCacheMappersTest {

    @Test
    fun toCacheModel_userCM_returnUserCM() {
        //Given a UserRM
        val userRM = UserRM(1, "username", "name", "imageUrl")

        //When toCacheModel is called
        val result = userRM.toCacheModel()

        //Then return a UserCM
        assertThat(result.uid).isEqualTo(userRM.id)
        assertThat(result.username).isEqualTo(userRM.username)
        assertThat(result.name).isEqualTo(userRM.name)
        assertThat(result.imageUrl).isEqualTo(userRM.img)
    }
}