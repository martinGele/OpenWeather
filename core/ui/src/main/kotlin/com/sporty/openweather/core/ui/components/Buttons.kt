package com.sporty.openweather.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sporty.openweather.core.ui.theme.OnPrimary
import com.sporty.openweather.core.ui.theme.OnSky
import com.sporty.openweather.core.ui.theme.Radius
import com.sporty.openweather.core.ui.theme.Rausch
import com.sporty.openweather.core.ui.theme.RauschDisabled
import com.sporty.openweather.core.ui.theme.Spacing

/**
 * The one accent action — Rausch fill, white label, 8dp radius, ≥48dp tall. Reads
 * on any sky. See airbnb/DESIGN.md > button-primary.
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier.heightIn(min = 48.dp),
        enabled = enabled,
        shape = RoundedCornerShape(Radius.sm),
        colors = ButtonDefaults.buttonColors(
            containerColor = Rausch,
            contentColor = OnPrimary,
            disabledContainerColor = RauschDisabled,
            disabledContentColor = OnPrimary,
        ),
        contentPadding = PaddingValues(horizontal = Spacing.lg, vertical = 14.dp),
    ) {
        Text(text = text)
    }
}

/**
 * A glass secondary action — transparent fill, white label, 1px white outline.
 * See airbnb/DESIGN.md > button-secondary-glass.
 */
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.heightIn(min = 48.dp),
        enabled = enabled,
        shape = RoundedCornerShape(Radius.sm),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = OnSky,
        ),
        border = BorderStroke(1.dp, OnSky),
        contentPadding = PaddingValues(horizontal = Spacing.lg, vertical = 13.dp),
    ) {
        Text(text = text)
    }
}
