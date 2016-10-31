package com.mariano.tesis.proyecto.entidades

import com.vaadin.server.Page
import com.vaadin.ui.Notification
import com.vaadin.ui.Upload
import java.io.*


/**
 * Created by ponch on 31/10/2016.
 */
class BalanceReceiver : Upload.Receiver, Upload.SucceededListener {

    var file = File("")

    override fun receiveUpload(filename: String?, mimeType: String?): OutputStream {
        var fos: FileOutputStream

        try{
             file = File("C:\\Temp\\" + filename)
             fos = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            Notification("No se pudo abrir el archivo</br>",
                    e.message,
                    Notification.Type.ERROR_MESSAGE)
            .show(Page.getCurrent())
            return null!!
        }
        return fos
    }

    override fun uploadSucceeded(p0: Upload.SucceededEvent?) {
        Notification("Se subio el archivo satisfactoriamente").show(Page.getCurrent())
    }
}