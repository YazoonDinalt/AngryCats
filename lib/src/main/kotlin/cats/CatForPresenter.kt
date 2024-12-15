package cats

/**

 A class to facilitate the interaction of logic and rendering

 */

class CatForPresenter(
    var x: Int,
    var y: Int,
    var status: Status,
    var animationStartTime: Long = 0
)
