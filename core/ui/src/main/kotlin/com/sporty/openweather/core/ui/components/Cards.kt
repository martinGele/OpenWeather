package com.sporty.openweather.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.sporty.openweather.core.ui.theme.BorderWidth
import com.sporty.openweather.core.ui.theme.Elevation
import com.sporty.openweather.core.ui.theme.GlassBorder
import com.sporty.openweather.core.ui.theme.GlassFill
import com.sporty.openweather.core.ui.theme.OnSky
import com.sporty.openweather.core.ui.theme.OnSkyMuted
import com.sporty.openweather.core.ui.theme.Radius
import com.sporty.openweather.core.ui.theme.Spacing

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = Radius.lg,
    padding: Dp = Spacing.lg,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(cornerRadius),
        color = GlassFill,
        contentColor = OnSky,
        border = BorderStroke(BorderWidth.hairline, GlassBorder),
        shadowElevation = Elevation.none,
        tonalElevation = Elevation.none,
    ) {
        Column(
            modifier = Modifier.padding(padding),
            content = content,
        )
    }
}


@Composable
fun StatTile(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    GlassCard(modifier = modifier, cornerRadius = Radius.md, padding = Spacing.base) {
        Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = OnSkyMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = OnSky,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
