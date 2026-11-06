package net.asere.heightwander.server.shared.profile.domain.model

data class ProfileAttributesShared(
    val profile: Profile = Profile.SPECTATOR,
    val rank: Rank = Rank.RECRUIT,
) {
    enum class Profile {
        SPECTATOR,
        MOTORIST,
        OFF_ROADER,
        VERSATILE,
    }

    enum class Rank {
        RECRUIT,
        ROOKIE,
        NOMAD,
        VETERAN,
        ELITE,
        CHAMPION,
        PATHFINDER,
        CARTOGRAPHER,
        ARCHITECT,
        PIONEER,
        SCOUT,
        RANGER,
        LEGEND,
    }
}
