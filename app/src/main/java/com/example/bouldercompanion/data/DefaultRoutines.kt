// Default routines to populate the finger training timers section
object DefaultRoutines {

    fun get(): List<TimerRoutine> = listOf(

        TimerRoutine(
            name = "Max Hangs",
            sets = 5,
            repsPerSet = 1,
            hangTime = 10,
            restBetweenReps = 0,
            restBetweenSets = 180
        ),

        TimerRoutine(
            name = "Repeaters 7/3",
            sets = 6,
            repsPerSet = 6,
            hangTime = 7,
            restBetweenReps = 3,
            restBetweenSets = 120
        ),

        TimerRoutine(
            name = "Endurance Hangs",
            sets = 4,
            repsPerSet = 4,
            hangTime = 20,
            restBetweenReps = 10,
            restBetweenSets = 180
        ),

        TimerRoutine(
            name = "Density Hangs",
            sets = 3,
            repsPerSet = 10,
            hangTime = 10,
            restBetweenReps = 10,
            restBetweenSets = 240
        ),

        TimerRoutine(
            name = "Recruitment Hangs",
            sets = 5,
            repsPerSet = 1,
            hangTime = 5,
            restBetweenReps = 0,
            restBetweenSets = 120
        )
    )
}
