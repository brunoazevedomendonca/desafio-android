package com.picpay.desafio.android.presentation.scenes.userlist

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.picpay.domain.model.User
import org.junit.Test

class UserListDiffCallbackTest {

    private val user1 = User(1, "username1", "name1", "imageUrl1")
    private val user2 = User(2, "username2", "name2", "imageUrl2")

    //System under test
    private lateinit var userListDiffCallback: UserListDiffCallback

    @Test
    fun getOldListSize_withOldList_returnCorrectSize() {
        //Given a userListDiffCall with two items old list
        userListDiffCallback = UserListDiffCallback(listOf(user1, user2), mock())

        //When getOldSize is called
        //Then return 2
        assertThat(userListDiffCallback.oldListSize).isEqualTo(2)
    }

    @Test
    fun getOldListSize_withEmptyList_returnZero() {
        //Given a userListDiffCall with an empty list
        userListDiffCallback = UserListDiffCallback(emptyList(), mock())

        //When getOldSize is called
        //Then return zero
        assertThat(userListDiffCallback.oldListSize).isEqualTo(0)
    }

    @Test
    fun getNewListSize_withNewList_returnCorrectSize() {
        //Given a userListDiffCall with two items old list
        userListDiffCallback = UserListDiffCallback(mock(), listOf(user1, user2))

        //When getNewSize is called
        //Then return 2
        assertThat(userListDiffCallback.newListSize).isEqualTo(2)
    }

    @Test
    fun getNewsListSize_withEmptyList_returnZero() {
        //Given a userListDiffCall with an empty list
        userListDiffCallback = UserListDiffCallback(mock(), emptyList())

        //When getNewsSize is called
        //Then return zero
        assertThat(userListDiffCallback.oldListSize).isEqualTo(0)
    }

    @Test
    fun areItemsTheSame_withSameItems_returnTrue() {
        //Given a userListDiffCall with same items lists
        userListDiffCallback = UserListDiffCallback(listOf(user1), listOf(user1))

        //When areItemsTheSame is called
        //Then return true
        assert(userListDiffCallback.areItemsTheSame(0, 0))
    }

    @Test
    fun areItemsTheSame_withDiffItems_returnFalse() {
        //Given a userListDiffCall with diff items lists
        userListDiffCallback = UserListDiffCallback(listOf(user1), listOf(user2))

        //When areItemsTheSame is called
        //Then return false
        assert(!userListDiffCallback.areItemsTheSame(0, 0))
    }

    @Test
    fun areItemsTheSame_withEmptyList_returnFalse() {
        //Given a userListDiffCall with empty lists
        userListDiffCallback = UserListDiffCallback(emptyList(), emptyList())

        //When areItemsTheSame is called
        //Then return false
        assert(!userListDiffCallback.areItemsTheSame(0, 0))
    }

    @Test
    fun areContentsTheSame_withSameItems_returnTrue() {
        //Given a userListDiffCall with same items lists
        userListDiffCallback = UserListDiffCallback(listOf(user1), listOf(user1))

        //When areContentsTheSame is called
        //Then return true
        assert(userListDiffCallback.areContentsTheSame(0, 0))
    }

    @Test
    fun areContentsTheSame_withDiffItems_returnFalse() {
        //Given a userListDiffCall with diff items lists
        userListDiffCallback = UserListDiffCallback(listOf(user1), listOf(user2))

        //When areContentsTheSame is called
        //Then return false
        assert(!userListDiffCallback.areContentsTheSame(0, 0))
    }

    @Test
    fun areContentsTheSame_withEmptyList_returnFalse() {
        //Given a userListDiffCall with empty lists
        userListDiffCallback = UserListDiffCallback(emptyList(), emptyList())

        //When areContentsTheSame is called
        //Then return false
        assert(!userListDiffCallback.areContentsTheSame(0, 0))
    }
}