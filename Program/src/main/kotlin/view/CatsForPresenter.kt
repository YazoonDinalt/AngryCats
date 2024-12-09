package view

import cats.Status

/**

 A class to facilitate the interaction of logic and rendering

 */

class CatForPresenter(
    var x: Int,
    var y: Int,
    var status: Status,
)
