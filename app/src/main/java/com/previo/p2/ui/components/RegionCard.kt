package com.previo.p2.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.previo.p2.domain.model.Region

@Composable
fun RegionCard(region: Region, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val color = when (region) {
        Region.ANDINA -> Color(0xFF5C8A3C)
        Region.CARIBE -> Color(0xFF1A6BAD)
        Region.PACIFICO -> Color(0xFF2E7D5E)
        Region.ORINOQUIA -> Color(0xFFB8860B)
        Region.AMAZONIA -> Color(0xFF1B5E20)
    }
    Card(
        modifier = modifier
            .height(90.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${region.emoji}  ${region.displayName}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}