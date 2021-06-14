import io.nacular.doodle.application.Application
import io.nacular.doodle.application.application
import io.nacular.doodle.core.Display
import io.nacular.doodle.core.plusAssign
import io.nacular.doodle.core.view
import io.nacular.doodle.drawing.Color.Companion.Red
import io.nacular.doodle.drawing.ColorFill
import io.nacular.doodle.geometry.Rectangle
import org.kodein.di.erased.instance

class TestApp(display: Display): Application {
    init {
        display += view {
            bounds = Rectangle(10, 10, 100, 100)

            render = {
                rect(bounds.atOrigin, ColorFill(Red))
            }
        }
    }

    override fun shutdown() {}
}

fun main() {
    application {
        TestApp(
            instance() // Display
        )
    }
}