package com.previo.p2.domain.model

enum class Region(val displayName: String, val apiName: String, val emoji: String) {
    ANDINA("Andina", "Colombian", "🏔️"),
    CARIBE("Caribe", "Colombian", "🌊"),
    PACIFICO("Pacífico", "Colombian", "🌿"),
    ORINOQUIA("Orinoquía", "Colombian", "🦅"),
    AMAZONIA("Amazonía", "Colombian", "🌳");

    companion object {
        fun all(): List<Region> = values().toList()
    }
}