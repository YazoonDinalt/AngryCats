package view

/**

 This class contains a list of all windows

*/

sealed class Screen {
    object FirstScreen : Screen()

    object SecondScreen : Screen()

    object ThirdScreen : Screen()
}
