package com.previo.p2.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SortBar(
    current: SortOrder,
    onSortChange: (SortOrder) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Ordenar:",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(end = 4.dp)
        )
        SortOrder.entries.forEach { order ->
            FilterChip(
                selected = current == order,
                onClick = { onSortChange(order) },
                label = { Text(order.label) }
            )
        }
    }
}