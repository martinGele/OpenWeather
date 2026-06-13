package com.sporty.openweather.core.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sporty.openweather.core.ui.theme.OnPrimary
import com.sporty.openweather.core.ui.theme.Radius
import com.sporty.openweather.core.ui.theme.Rausch
import com.sporty.openweather.core.ui.theme.RauschDisabled
import com.sporty.openweather.core.ui.theme.Size
import com.sporty.openweather.core.ui.theme.Spacing


@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier.heightIn(min = Size.buttonMinHeight),
        enabled = enabled,
        shape = RoundedCornerShape(Radius.sm),
        colors = ButtonDefaults.buttonColors(
            containerColor = Rausch,
            contentColor = OnPrimary,
            disabledContainerColor = RauschDisabled,
            disabledContentColor = OnPrimary,
        ),
        contentPadding = PaddingValues(horizontal = Spacing.lg, vertical = Size.buttonVerticalPadding),
    ) {
        Text(text = text)
    }
}