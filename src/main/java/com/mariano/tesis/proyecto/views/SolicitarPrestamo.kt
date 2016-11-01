/*
 * Copyright 2015 The original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mariano.tesis.proyecto.views


import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.server.FontAwesome
import com.vaadin.server.Sizeable
import com.vaadin.spring.annotation.SpringComponent
import com.vaadin.spring.annotation.SpringView
import com.vaadin.spring.annotation.UIScope
import com.vaadin.ui.*
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.security.access.annotation.Secured
import org.vaadin.spring.events.EventBus
import org.vaadin.spring.events.EventScope
import org.vaadin.spring.events.annotation.EventBusListenerMethod

import com.mariano.tesis.proyecto.Sections
import com.vaadin.event.ShortcutAction
import com.vaadin.server.Page
import com.vaadin.ui.themes.ValoTheme
import org.vaadin.spring.events.Event

import org.vaadin.spring.security.VaadinSecurity
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon
import org.vaadin.spring.sidebar.annotation.SideBarItem
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.OutputStream


@Secured("ROLE_USER", "ROLE_ADMIN")
@SpringView(name = "solicitarPrestamo")
@SideBarItem(sectionId = Sections.VIEWS, caption = "Solicitar préstamo")
@FontAwesomeIcon(FontAwesome.DOLLAR)
@SpringComponent
@UIScope
open class SolicitarPrestamo
@Autowired
constructor(
        private val vaadinSecurity: VaadinSecurity,
        private val eventBus: EventBus.SessionEventBus) : CustomComponent(), View {
    //private final MyBackend backend;
    private val formLayout: FormLayout

    private val userName: String

    private val nombreDeUsuario: Label

    private val button: Button

    private val topicListener = EventsSubcriber()

    private val presenter = Presenter()

    private val receiver: BalanceReceiver

    private val upload: Upload


    init {

        userName = this.vaadinSecurity.authentication.name
        eventBus.subscribe(topicListener, userName)

        formLayout = FormLayout()
        formLayout.defaultComponentAlignment = Alignment.TOP_LEFT
        nombreDeUsuario = Label("Nombre de usuario conectado: " + userName)
        nombreDeUsuario.setStyleName(ValoTheme.LABEL_BOLD)

        button = Button("Solicitar préstamo")
        button.setStyleName(ValoTheme.BUTTON_PRIMARY)
        button.setClickShortcut(ShortcutAction.KeyCode.ENTER)
        button.addClickListener({
            presenter.enviarNotificacion()
        })

        receiver = BalanceReceiver(userName)
        upload = Upload("Seleccionar el archivo del balance firmado digitalmente", receiver)
        upload.buttonCaption = "Subir Archivo"
        upload.isImmediate = true
        upload.addSucceededListener(receiver)
        upload.setStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT)

        formLayout.addComponent(nombreDeUsuario)
        formLayout.addComponent(upload)
        formLayout.addComponent(button)

        compositionRoot = formLayout

    }

    override fun detach() {
        eventBus.unsubscribe(this)
        eventBus.unsubscribe(topicListener)
        super.detach()
    }

    override fun enter(viewChangeEvent: ViewChangeListener.ViewChangeEvent) {
    }

    internal inner class EventsSubcriber {

        @EventBusListenerMethod
        fun mostrarMensajeRecibido(mensaje: String) {

            ui.access {

                Notification.show("Solicitud de Préstamo Enviada")
            }
        }
    }

    internal inner class Presenter {

        fun enviarNotificacion() {
            eventBus.publish(EventScope.SESSION, userName, this, "")
        }

    }

    inner class BalanceReceiver(userName: String) : Upload.Receiver, Upload.SucceededListener {
        val baseUrl = "C:\\Temp\\"
        val separetor: String = java.io.File.separator

        override fun receiveUpload(filename: String?, mimeType: String?): OutputStream {
            val fos: FileOutputStream

            try{
                val dir = File(baseUrl + userName)
                dir.mkdir()
                val file = File(baseUrl + userName + separetor + filename)
                fos = FileOutputStream(file)
            } catch (e: FileNotFoundException) {
                Notification("No se pudo abrir el archivo",
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
}
