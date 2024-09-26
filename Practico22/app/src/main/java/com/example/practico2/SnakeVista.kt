package com.example.practico2

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class SnakeVista(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val paint = Paint()
    private val snakeMotor = SnakeMotor(20, 35)
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval = 300L

    init {
        paint.color = Color.GREEN
        startGame()
    }


    private fun startGame() {

        handler.postDelayed(object : Runnable {
            override fun run() {
                snakeMotor.actJuego()

                if (snakeMotor.isGameOver) {
                    showGameOverDialog()
                } else {
                    invalidate()
                    handler.postDelayed(this, updateInterval)
                }
            }
        }, updateInterval)
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val cellWidth = width / snakeMotor.width
        val cellHeight = height / snakeMotor.height


        paint.color = Color.BLACK  // Color de la cuadrícula
        paint.strokeWidth = 2f      // Grosor de las líneas de la cuadrícula


        for (i in 0..snakeMotor.width) {
            canvas.drawLine(
                (i * cellWidth).toFloat(), 0f,
                (i * cellWidth).toFloat(), height.toFloat(),
                paint
            )
        }


        for (i in 0..snakeMotor.height) {
            canvas.drawLine(
                0f, (i * cellHeight).toFloat(),
                width.toFloat(), (i * cellHeight).toFloat(),
                paint
            )
        }
        paint.color = Color.GREEN
        for (part in snakeMotor.snake) {
            canvas.drawRect(
                part.x * cellWidth.toFloat(),
                part.y * cellHeight.toFloat(),
                (part.x + 1) * cellWidth.toFloat(),
                (part.y + 1) * cellHeight.toFloat(),
                paint
            )
        }

        paint.color = Color.RED
        canvas.drawRect(
            snakeMotor.comida.x * cellWidth.toFloat(),
            snakeMotor.comida.y * cellHeight.toFloat(),
            (snakeMotor.comida.x + 1) * cellWidth.toFloat(),
            (snakeMotor.comida.y + 1) * cellHeight.toFloat(),
            paint
        )
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val x = event.x
            val y = event.y
            val screenWidth = width
            val screenHeight = height

            if (x < screenWidth / 3) {
                snakeMotor.setDireccion(Direccion.LEFT)
            } else if (x > 2 * screenWidth / 3) {
                snakeMotor.setDireccion(Direccion.RIGHT)
            } else if (y < screenHeight / 3) {
                snakeMotor.setDireccion(Direccion.UP)
            } else if (y > 2 * screenHeight / 3) {
                snakeMotor.setDireccion(Direccion.DOWN)
            }
        }
        return true
    }


    private fun showGameOverDialog() {
        AlertDialog.Builder(context).apply {
            setTitle("Perdiste")
            setPositiveButton("Intentar de nuevo ") { _, _ ->
                restartGame()
            }
            setNegativeButton("Salir") { _, _ ->
                (context as MainActivity).finish()
            }
            setCancelable(false)
            show()
        }
    }


    private fun restartGame() {
        snakeMotor.Reinicio()
        startGame()
        invalidate()
    }
}