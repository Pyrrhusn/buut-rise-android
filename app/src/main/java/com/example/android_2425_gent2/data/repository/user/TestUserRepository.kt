package com.example.android_2425_gent2.data.repository.user

import com.example.android_2425_gent2.data.model.UserSurface
import com.example.android_2425_gent2.data.network.model.UserDetailsDto
import com.example.android_2425_gent2.data.network.model.AddressDto
import com.example.android_2425_gent2.data.network.users.UserNameDto
import com.example.android_2425_gent2.data.repository.APIResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

class TestUserRepository : UserRepository {
    private var _stateFlow = MutableStateFlow<APIResource<List<UserSurface>>>(APIResource.Loading())
    val stateFlow: StateFlow<APIResource<List<UserSurface>>> get() = _stateFlow

    private var _userDetailsStateFlow = MutableStateFlow<APIResource<UserDetailsDto>>(APIResource.Loading())
    val userDetailsStateFlow: StateFlow<APIResource<UserDetailsDto>> get() = _userDetailsStateFlow

    private var _updateRoleStateFlow = MutableStateFlow<APIResource<Unit>>(APIResource.Loading())
    val updateRoleStateFlow: StateFlow<APIResource<Unit>> get() = _updateRoleStateFlow

    override suspend fun getUsers(): Flow<APIResource<List<UserSurface>>> = stateFlow

    override suspend fun getUserDetails(userId: String): Flow<APIResource<UserDetailsDto>> = userDetailsStateFlow
    override suspend fun getUserDetails(): Flow<APIResource<UserDetailsDto>> = userDetailsStateFlow

    override suspend fun updateUserRole(userId: Int, role: String): Flow<APIResource<Unit>> = updateRoleStateFlow

    override suspend fun getUserNames(): Flow<APIResource<List<UserNameDto>>> = flow {
        emit(APIResource.Success(listOf(
            UserNameDto(
                id = 1,
                fullName = "Her De Gaver, Patrick",
                firstName = "Patrick",
                familyName = "Her De Gaver"
            ),
            UserNameDto(
                id = 2,
                fullName = "de Clerk, Bram",
                firstName = "Bram",
                familyName = "de Clerk"
            ),
            UserNameDto(
                id = 3,
                fullName = "Piatti, Simon",
                firstName = "Simon",
                familyName = "Piatti"
            )
        )))
    }

    fun triggerLoading() {
        _stateFlow.value = APIResource.Loading()
    }

    fun triggerError(errorMessage: String) {
        _stateFlow.value = APIResource.Error(errorMessage)
    }

    fun triggerSuccessWithCustomUsers(users: List<UserSurface>) {
        _stateFlow.value = APIResource.Success(users)
    }

    fun triggerUserDetailsLoading() {
        _userDetailsStateFlow.value = APIResource.Loading()
    }

    fun triggerUserDetailsError(errorMessage: String) {
        _userDetailsStateFlow.value = APIResource.Error(errorMessage)
    }

    fun triggerUserDetailsSuccess(userDetails: UserDetailsDto) {
        _userDetailsStateFlow.value = APIResource.Success(userDetails)
    }

    fun triggerUpdateRoleLoading() {
        _updateRoleStateFlow.value = APIResource.Loading()
    }

    fun triggerUpdateRoleError(errorMessage: String) {
        _updateRoleStateFlow.value = APIResource.Error(errorMessage)
    }

    fun triggerUpdateRoleSuccess() {
        _updateRoleStateFlow.value = APIResource.Success(Unit)
    }
}