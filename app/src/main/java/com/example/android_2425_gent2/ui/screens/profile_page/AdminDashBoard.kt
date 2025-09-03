package com.example.android_2425_gent2.ui.screens.profile_page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.android_2425_gent2.R
import com.example.android_2425_gent2.ui.screens.profile_page.partials.AppTopBar
import com.example.android_2425_gent2.ui.screens.profile_page.partials.QuickActionsSection

data class QuickAction(
    val text: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@Composable
fun AdminDashBoard(
    onNavigateToUsers: () -> Unit,
    onNavigateToBatteries: () -> Unit,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit
) {
    Column{
        AppTopBar(
            title = stringResource(R.string.admin_dashboard),
                    canNavigateBack = true,
            onNavigateBack = onNavigateBack
        )

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.welcome_admin_message),
                style = MaterialTheme.typography.headlineMedium,
                color = colorResource(R.color.secondary_contrast_text)
            )

            val quickActions = listOf(
                QuickAction(
                    text = stringResource(R.string.manage_guests),
                    icon = Icons.Default.Face,
                    onClick = onNavigateToUsers
                ),
                QuickAction(
                    text = stringResource(R.string.manage_batteries),
                    icon = Icons.Default.Build,
                    onClick = onNavigateToBatteries
                )
            )

            QuickActionsSection(
                title = stringResource(R.string.quick_actions),
                actions = quickActions
            )
        }
    }
}