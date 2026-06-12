package com.sporty.openweather.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.sporty.openweather.core.ui.theme.GlassBorder
import com.sporty.openweather.core.ui.theme.GlassFill
import com.sporty.openweather.core.ui.theme.OnPrimary
import com.sporty.openweather.core.ui.theme.OnSky
import com.sporty.openweather.core.ui.theme.OnSkyMuted
import com.sporty.openweather.core.ui.theme.Radius
import com.sporty.openweather.core.ui.theme.Rausch
import com.sporty.openweather.core.ui.theme.Spacing

@Composable
fun SearchFieldPill(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    searchIcon: Painter,
    modifier: Modifier = Modifier,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        singleLine = true,
        textStyle = MaterialTheme.typography.titleSmall.copy(color = OnSky),
        cursorBrush = SolidColor(Rausch),
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(imeAction = ImeAction.Search),
        decorationBox = { innerTextField ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(Radius.full),
                color = GlassFill,
                contentColor = OnSky,
                border = BorderStroke(1.dp, GlassBorder),
            ) {
                Row(
                    modifier = Modifier.padding(start = Spacing.lg, end = Spacing.sm),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.typography.titleSmall,
                                color = OnSkyMuted,
                            )
                        }
                        innerTextField()
                    }
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Rausch),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = searchIcon,
                            contentDescription = "Search",
                            tint = OnPrimary,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
            }
        },
    )
}
