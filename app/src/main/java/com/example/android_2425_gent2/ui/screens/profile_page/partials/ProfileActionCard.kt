package com.example.android_2425_gent2.ui.screens.profile_page.partials

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.android_2425_gent2.R
import com.example.android_2425_gent2.ui.screens.profile_page.ProfileAction

@Composable
fun ProfileActionCard(
    actions: List<ProfileAction>,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = colorResource(R.color.secondary),
        )
    ) {
        Column(modifier = modifier.padding(16.dp)) {
            actions.forEach { action ->
                ProfileActionItem(
                    icon = action.icon,
                    text = action.text,
                    onClick = action.onClick
                )
            }
        }
    }
}