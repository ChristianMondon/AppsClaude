package com.example.blocmon.game

/** Source de hasard : renvoie un nombre dans [0, 1). Injectée pour rester testable. */
fun interface Dice { fun roll(): Double }

data class Encounter(val species: Species)

data class GameState(
    val world: World,
    val pos: Pos = world.spawn,
    val balls: Int = MAX_BALLS,
    val steps: Int = 0,
    val caught: List<Species> = emptyList(),
    val encounter: Encounter? = null,
    val message: String = "",
) {
    fun move(dir: Dir, dice: Dice): GameState {
        if (encounter != null) return this
        val target = pos + dir
        val block = world[target] ?: return this
        if (!block.walkable) return this

        val newSteps = steps + 1
        val newBalls = if (newSteps % STEPS_PER_BALL == 0) minOf(MAX_BALLS, balls + 1) else balls
        val moved = copy(pos = target, steps = newSteps, balls = newBalls, message = "")
        if (block == Block.TALL_GRASS && dice.roll() < ENCOUNTER_CHANCE) {
            val species = Bestiary.pick(dice.roll())
            return moved.copy(encounter = Encounter(species), message = "Un ${species.name} sauvage apparaît !")
        }
        return moved
    }

    fun throwBall(dice: Dice): GameState {
        val enc = encounter ?: return this
        if (balls == 0) return copy(message = "Plus de balle !")
        val name = enc.species.name
        return when {
            dice.roll() < enc.species.catchRate ->
                copy(balls = balls - 1, caught = caught + enc.species, encounter = null, message = "$name capturé !")
            dice.roll() < FLEE_CHANCE ->
                copy(balls = balls - 1, encounter = null, message = "$name s'est enfui…")
            else ->
                copy(balls = balls - 1, message = "$name s'est échappé de la balle !")
        }
    }

    fun flee(): GameState =
        if (encounter == null) this else copy(encounter = null, message = "Vous avez fui.")

    companion object {
        const val MAX_BALLS = 10
        const val STEPS_PER_BALL = 20
        const val ENCOUNTER_CHANCE = 0.25
        const val FLEE_CHANCE = 0.30

        fun new(seed: Long) = GameState(World.generate(seed))
    }
}
