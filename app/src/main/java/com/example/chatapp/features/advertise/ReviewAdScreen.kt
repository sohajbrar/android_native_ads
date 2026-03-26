@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chatapp.features.advertise

import androidx.compose.foundation.background
import com.example.chatapp.wds.components.clickableWithSound
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatapp.wds.components.WDSButton
import com.example.chatapp.wds.components.WDSButtonVariant
import com.example.chatapp.wds.components.WDSDivider
import com.example.chatapp.wds.components.WDSTopBar
import com.example.chatapp.wds.theme.WdsTheme
import android.view.SoundEffectConstants
import androidx.compose.ui.platform.LocalView

@Composable
fun ReviewAdScreen(
    onNavigateBack: () -> Unit,
    onCreateAdClick: () -> Unit,
    onEditAdPreview: () -> Unit = {},
    onEditAudience: () -> Unit = {},
    onEditBudget: () -> Unit = {},
    adViewModel: AdCreationViewModel
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val view = LocalView.current

    Scaffold(
        containerColor = colors.colorSurfaceDefault,
        topBar = {
            WDSTopBar(
                title = "Review ad",
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(onClick = { view.playSoundEffect(SoundEffectConstants.CLICK) }) {
                        Icon(
                            imageVector = Icons.Outlined.HelpOutline,
                            contentDescription = "Help",
                            tint = colors.colorContentDefault
                        )
                    }
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.colorSurfaceDefault)
                    .navigationBarsPadding()
            ) {
                // Budget row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = dimensions.wdsSpacingDouble,
                            vertical = dimensions.wdsSpacingSingle
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Ad budget",
                        style = typography.body2,
                        color = colors.colorContentDeemphasized
                    )
                    Text(
                        text = "₹${formatNumber(adViewModel.totalBudget)}",
                        style = typography.body2,
                        color = colors.colorContentDefault
                    )
                }

                // Estimated reach
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = dimensions.wdsSpacingDouble,
                            vertical = dimensions.wdsSpacingHalf
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Estimated daily reach",
                        style = typography.body2,
                        color = colors.colorContentDeemphasized
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimensions.wdsSpacingHalf)
                    ) {
                        Text(
                            text = "${adViewModel.estimatedReachLow}–${adViewModel.estimatedReachHigh} accounts",
                            style = typography.body2,
                            color = colors.colorContentDefault
                        )
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "Info",
                            modifier = Modifier.size(16.dp),
                            tint = colors.colorContentDeemphasized
                        )
                    }
                }

                // Create ad button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = dimensions.wdsSpacingDouble,
                            vertical = dimensions.wdsSpacingSinglePlus
                        )
                ) {
                    WDSButton(
                        onClick = onCreateAdClick,
                        text = "Create ad",
                        variant = WDSButtonVariant.FILLED,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Terms
                Text(
                    text = buildAnnotatedString {
                        append("By creating this ad, you agree to ")
                        withStyle(SpanStyle(color = colors.colorPositive)) {
                            append("Meta's Terms and Conditions")
                        }
                        append(".")
                    },
                    style = typography.body3,
                    color = colors.colorContentDeemphasized,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(
                        horizontal = dimensions.wdsSpacingDouble,
                        vertical = dimensions.wdsSpacingSingle
                    )
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            AdCreationProgressBar(toProgress = 1f, adViewModel = adViewModel)

            // Headline
            Column(
                modifier = Modifier.padding(
                    horizontal = dimensions.wdsSpacingDouble,
                    vertical = dimensions.wdsSpacingDouble
                )
            ) {
                Text(
                    text = "Your ad is ready to go",
                    style = typography.headline1,
                    color = colors.colorContentDefault
                )
                Spacer(modifier = Modifier.height(dimensions.wdsSpacingHalf))
                Text(
                    text = "Check you're happy with everything, then add your payment method.",
                    style = typography.body2,
                    color = colors.colorContentDeemphasized
                )
            }

            // Ad preview row
            ReviewRow(
                avatarContent = {
                    AsyncImage(
                        model = "android.resource://com.example.chatapp/drawable/avatar_my_status",
                        contentDescription = "Business profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                },
                title = "Ad preview",
                subtitle = "Lucky Shrub",
                trailingIcon = Icons.Outlined.Edit,
                onTrailingClick = onEditAdPreview
            )

            WDSDivider(
                modifier = Modifier.padding(horizontal = dimensions.wdsSpacingDouble)
            )

            // Audience row
            ReviewRow(
                avatarContent = {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(colors.colorSurfaceEmphasized),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.People,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = colors.colorContentDefault
                        )
                    }
                },
                title = adViewModel.audienceLocation,
                subtitle = "${adViewModel.audienceAgeRange} · ${adViewModel.audienceGender}",
                trailingIcon = Icons.Outlined.Edit,
                onTrailingClick = onEditAudience
            )

            WDSDivider(
                modifier = Modifier.padding(horizontal = dimensions.wdsSpacingDouble)
            )

            // Budget & duration row
            ReviewRow(
                avatarContent = {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(colors.colorSurfaceEmphasized),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Payments,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = colors.colorContentDefault
                        )
                    }
                },
                title = if (adViewModel.isSetDuration)
                    "₹${adViewModel.dailyBudget} per day for ${adViewModel.durationDays} days"
                else
                    "₹${adViewModel.dailyBudget} per day, until paused",
                subtitle = null,
                trailingIcon = Icons.Outlined.Edit,
                onTrailingClick = onEditBudget
            )

            WDSDivider(
                modifier = Modifier.padding(horizontal = dimensions.wdsSpacingDouble)
            )

            // Facebook account row
            ReviewRow(
                avatarContent = {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(colors.colorSurfaceEmphasized),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = colors.colorContentDefault
                        )
                    }
                },
                title = "Parveen Shama",
                subtitle = "Facebook account",
                trailingIcon = Icons.Outlined.MoreVert,
                onTrailingClick = { }
            )

            WDSDivider(
                modifier = Modifier.padding(horizontal = dimensions.wdsSpacingDouble)
            )

            // Ad account row
            ReviewRow(
                avatarContent = {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(colors.colorSurfaceEmphasized),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CreditCard,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = colors.colorContentDefault
                        )
                    }
                },
                title = "Lucky Shrub ad account",
                subtitle = "1234567890112345",
                trailingIcon = Icons.Outlined.Edit,
                onTrailingClick = { }
            )

            WDSDivider(
                modifier = Modifier.padding(horizontal = dimensions.wdsSpacingDouble)
            )

            // Payment method row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickableWithSound { }
                    .padding(
                        horizontal = dimensions.wdsSpacingDouble,
                        vertical = dimensions.wdsSpacingSinglePlus
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(colors.colorSurfaceEmphasized),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "VISA",
                        style = typography.body3,
                        color = colors.colorContentDefault
                    )
                }

                Spacer(modifier = Modifier.width(dimensions.wdsSpacingSinglePlus))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Visa · 3685",
                        style = typography.body1,
                        color = colors.colorContentDefault
                    )
                }

                Text(
                    text = "Change",
                    style = typography.body2Emphasized,
                    color = colors.colorPositive
                )
            }

            Spacer(modifier = Modifier.height(dimensions.wdsSpacingQuad))
        }
    }
}

@Composable
private fun ReviewRow(
    avatarContent: @Composable () -> Unit,
    title: String,
    subtitle: String?,
    trailingIcon: ImageVector,
    onTrailingClick: () -> Unit
) {
    val colors = WdsTheme.colors
    val dimensions = WdsTheme.dimensions
    val typography = WdsTheme.typography
    val view = LocalView.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickableWithSound(onClick = onTrailingClick)
            .padding(
                horizontal = dimensions.wdsSpacingDouble,
                vertical = dimensions.wdsSpacingSinglePlus
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        avatarContent()

        Spacer(modifier = Modifier.width(dimensions.wdsSpacingSinglePlus))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = typography.body1,
                color = colors.colorContentDefault
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = typography.body2,
                    color = colors.colorContentDeemphasized
                )
            }
        }

        IconButton(
            onClick = { view.playSoundEffect(SoundEffectConstants.CLICK); onTrailingClick() },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = trailingIcon,
                contentDescription = "Edit",
                modifier = Modifier.size(20.dp),
                tint = colors.colorContentDeemphasized
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewAdScreenPreview() {
    WdsTheme(darkTheme = false) {
        ReviewAdScreen(
            onNavigateBack = {},
            onCreateAdClick = {},
            adViewModel = AdCreationViewModel(CreatedAdsStore())
        )
    }
}
