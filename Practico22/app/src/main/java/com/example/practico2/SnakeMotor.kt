package com.example.practico2
import kotlin.random.Random

enum class Direccion { UP, DOWN, LEFT, RIGHT }
data class Punto(val x: Int, val y: Int)

class SnakeMotor(val width: Int, val height: Int) {
    var snake = mutableListOf(Punto(width / 2, height / 2))
    private var currentDireccion = Direccion.RIGHT
    var comida: Punto = generarComida() //
    var isGameOver = false


    fun setDireccion(newDireccion: Direccion) {
        if (puedeCambiarD(newDireccion)) {
            currentDireccion = newDireccion
        }
    }


    private fun puedeCambiarD(newDireccion: Direccion): Boolean {
        return when (newDireccion) {
            Direccion.UP -> currentDireccion != Direccion.DOWN
            Direccion.DOWN -> currentDireccion != Direccion.UP
            Direccion.LEFT -> currentDireccion != Direccion.RIGHT
            Direccion.RIGHT -> currentDireccion != Direccion.LEFT
        }
    }


    fun actJuego() {
        if (isGameOver) return

        val cabeza = snake.first()

        val newcabeza = when (currentDireccion) {
            Direccion.UP -> Punto(cabeza.x, (cabeza.y - 1 + height) % height)
            Direccion.DOWN -> Punto(cabeza.x, (cabeza.y + 1) % height)
            Direccion.LEFT -> Punto((cabeza.x - 1 + width) % width, cabeza.y)
            Direccion.RIGHT -> Punto((cabeza.x + 1) % width, cabeza.y)
        }


        if (snake.contains(newcabeza)) {
            isGameOver = true
            return
        }

        snake.add(0, newcabeza)

        if (newcabeza == comida) {
            comida = generarComida()
        } else {

            snake.removeAt(snake.size - 1)
        }

        if (snake.size == width * height) {
            isGameOver = true
        }
    }

    private fun generarComida(): Punto {
        var newComida: Punto
        do {
            newComida = Punto(Random.nextInt(width), Random.nextInt(height))
        } while (snake.contains(newComida))
        return newComida
    }

    fun Reinicio() {
        snake = mutableListOf(Punto(width / 2, height / 2))
        currentDireccion = Direccion.RIGHT
        comida = generarComida()
        isGameOver = false
    }
}