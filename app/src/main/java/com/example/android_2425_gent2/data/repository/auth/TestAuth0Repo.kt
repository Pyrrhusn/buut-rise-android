package com.example.android_2425_gent2.data.repository.auth

import com.auth0.android.result.Credentials
import com.example.android_2425_gent2.data.model.UserRole
import com.example.android_2425_gent2.data.repository.APIResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

class TestAuth0Repo: IAuthRepo {
    private val credentials: Credentials = Credentials(
        idToken =  "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IlU4Rkw5Q1pyWkxJRjdHek9yQVZDWSJ9.eyJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3JvbGUiOltdLCJuaWNrbmFtZSI6ImFkbWluIiwibmFtZSI6ImFkbWluQGFkbWluLmNvbSIsInBpY3R1cmUiOiJodHRwczovL3MuZ3JhdmF0YXIuY29tL2F2YXRhci82NGUxYjhkMzRmNDI1ZDE5ZTFlZTJlYTcyMzZkMzAyOD9zPTQ4MCZyPXBnJmQ9aHR0cHMlM0ElMkYlMkZjZG4uYXV0aDAuY29tJTJGYXZhdGFycyUyRmFkLnBuZyIsInVwZGF0ZWRfYXQiOiIyMDI0LTExLTE4VDE1OjI3OjQ2LjI0NFoiLCJlbWFpbCI6ImFkbWluQGFkbWluLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiaXNzIjoiaHR0cHM6Ly9yaXNlLWdlbnQyLmV1LmF1dGgwLmNvbS8iLCJhdWQiOiI4dkp0YlhnMkZwdEhHbUtycEZsMXRad2hpWE9KWjU3bCIsImlhdCI6MTczMTk0MzY2NywiZXhwIjoxNzMxOTc5NjY3LCJzdWIiOiJhdXRoMHw2NzJiNzVjYjRjYTU0MjhmMzVkZDVhNmQifQ.H5MMUJovGHoAkhvu0F3gV4ehMwOrPsHc6_r6GG-yVSS6i-Pq5geSzILXyafFhqa-SCIbPHWmlk3S5Tt5dMXC8gxhmueEXy28LC9jTxngiPePKi9YlQu3l4cb2VkGN_CfVs-tMIVPb_MB4GGk850sq1Ihw9sNzRbOC6-4PhyRB3V4XrpRkPPgZ4HDWBOyuGxralpfelEC2epSk4ID168HbrLruwvg62kOkyWh7sg5XjKUv2__ZplkUpukbDcvWM8Zo-p5GQnn4EQh48Jmyv9urPZMv0-G3KrXo3NlKnZdOFkFK8Ch5EV1BVYVkQM56OfPTjQ2vTEQq_IH-Sr1X3jArg",
        accessToken = "eyJhbGciOiJkaXIiLCJlbmMiOiJBMjU2R0NNIiwiaXNzIjoiaHR0cHM6Ly9yaXNlLWdlbnQyLmV1LmF1dGgwLmNvbS8ifQ..S8Zc-zNWh4Dg_P0e.vt8Lgwlt-cvILfFIQhe2-5s6dZov-gTrSPiiXg4BL4JfdxjQlpVlh42JJkUIeEJ7spmyxrcGmSeJzxobXBeSIc12UQamFprRkwu7YdgwNHOXG6GAcN32XpUbzEjRMD4C5xmFsFNijoSbDmF1bRdMDRaYZqk33sTipKLksWgikV1cF0vvP4OailswsgwWuvFsLddKAnFkO6iQ-wH3JoQc-CoHDdRMSzM-R0spcAF3257QKxbBqE8hG8vH3P7KCowem0WmAnBzXo0ByD_40r4BNwttaBWhpv-VQgLU85V_Gz3Ce07sEB1LS3FlrMwKoaabp2SicklfXvyXZDVhE7TbWhz7Yd3C0inkwk67j0f-Xs7-T5fAid4v-PBoZwyUhSF_gzinfiz3Z8nVzm7ufpYyZpZ6U0MUaft28NYnYmMTZUJ0.I4CbqVPKqxBNSyxfQ8vFnw",
        type = "Bearer",
        scope = "openid profile email",
        expiresAt = Date.from(LocalDateTime.now().plus(Duration.ofSeconds(86400)).atZone(ZoneId.systemDefault()).toInstant()),
        refreshToken = null,
        )


    private var loggedIn: Boolean = false
    private var hasRole : Boolean = false

    private var _stateFlow = MutableStateFlow<APIResource<Credentials>>(APIResource.Loading())
    val stateFlow: StateFlow<APIResource<Credentials>> get() = _stateFlow

    private fun getCredentials():  Flow<APIResource<Credentials>> =
    stateFlow

    override suspend fun getStoredCredentials(): Flow<APIResource<Credentials>> = getCredentials()

    override suspend fun login(userName: String, password: String): Flow<APIResource<Credentials>> =
        getCredentials()

    fun login(){
        loggedIn = true
    }

    suspend override fun logout() {
        loggedIn = false
    }

    override suspend fun hasRole(role: UserRole): Boolean {
        return hasRole
    }

    override fun isLoggedIn(): Boolean {
        return loggedIn
    }

    fun triggerLoading(){
        _stateFlow.value = APIResource.Loading()
    }

    fun triggerLoginFailure(errorMessage: String) {
        _stateFlow.value = APIResource.Error(errorMessage ?: "Unknown error")
    }

    fun triggerLoginSuccess() {
        _stateFlow.value = APIResource.Success(credentials)
    }
}