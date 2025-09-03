package com.example.android_2425_gent2.ui.screens.profile_page.partials

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android_2425_gent2.data.model.UserSurface


@Composable
fun GuestUsersList(
    modifier: Modifier = Modifier,
    onUserClick: (String) -> Unit,
    users: List<UserSurface>
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(users) { user ->
                UserListItemCard(
                    user = user,
                    onClick = onUserClick,
                    modifier = modifier
                )
            }
        }
    }
}