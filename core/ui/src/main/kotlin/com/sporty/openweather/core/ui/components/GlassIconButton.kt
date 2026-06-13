package com.sporty.openweather.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import com.sporty.openweather.core.ui.theme.BorderWidth
import com.sporty.openweather.core.ui.theme.GlassBorder
import com.sporty.openweather.core.ui.theme.GlassFill
import com.sporty.openweather.core.ui.theme.IconSize
import com.sporty.openweather.core.ui.theme.OnSky
import com.sporty.openweather.core.ui.theme.Size


@Composable
fun GlassIconButton(
    icon: Painter,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = Size.control,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.size(size),
        shape = CircleShape,
        color = GlassFill,
        contentColor = OnSky,
        border = BorderStroke(BorderWidth.hairline, GlassBorder),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                painter = icon,
                contentDescription = contentDescription,
                tint = OnSky,
                modifier = Modifier.size(IconSize.md),
            )
        }
    }
}
