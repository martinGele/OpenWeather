package com.sporty.openweather.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import com.sporty.openweather.core.ui.theme.BorderWidth
import com.sporty.openweather.core.ui.theme.Elevation
import com.sporty.openweather.core.ui.theme.GlassBorder
import com.sporty.openweather.core.ui.theme.GlassFill
import com.sporty.openweather.core.ui.theme.IconSize
import com.sporty.openweather.core.ui.theme.OnPrimary
import com.sporty.openweather.core.ui.theme.OnSky
import com.sporty.openweather.core.ui.theme.OnSkyMuted
import com.sporty.openweather.core.ui.theme.Radius
import com.sporty.openweather.core.ui.theme.Rausch
import com.sporty.openweather.core.ui.theme.Size
import com.sporty.openweather.core.ui.theme.Spacing

@Composable
fun SearchBarPill(
    placeholder: String,
    searchIcon: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .height(Size.control)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(Radius.full),
        color = GlassFill,
        contentColor = OnSky,
        border = BorderStroke(BorderWidth.hairline, GlassBorder),
        shadowElevation = Elevation.none,
    ) {
        Row(
            modifier = Modifier.padding(start = Spacing.lg, end = Spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.titleSmall,
                color = OnSkyMuted,
                modifier = Modifier.weight(1f),
            )
            Box(
                modifier = Modifier
                    .size(Size.touchTarget)
                    .clip(CircleShape)
                    .background(Rausch),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = searchIcon,
                    contentDescription = "Search",
                    tint = OnPrimary,
                    modifier = Modifier.size(IconSize.sm),
                )
            }
        }
    }
}
