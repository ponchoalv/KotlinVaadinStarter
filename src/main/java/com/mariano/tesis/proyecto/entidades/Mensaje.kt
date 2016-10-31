package com.mariano.tesis.proyecto.entidades

import java.util.Date


/**
 * Created by ponch on 3/6/2016.
 * Clase que se utiliza para los mensajes de los chats
 */

data class Mensaje(val mensaje: String, val de: String, val para: String, val timestamp: Date)