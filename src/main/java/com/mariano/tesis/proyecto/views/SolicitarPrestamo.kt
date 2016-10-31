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

import com.mariano.tesis.proyecto.entidades.Mensaje
import com.mariano.tesis.proyecto.Sections

import org.vaadin.spring.security.VaadinSecurity
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon
import org.vaadin.spring.sidebar.annotation.SideBarItem

import java.util.Date

/**
 * View that is available for all users.

 * @author Petter Holmström (petter@vaadin.com)
 */
@Secured("ROLE_USER", "ROLE_ADMIN")
@SpringView(name = "user")
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
    private val verticalLayout: VerticalLayout

    private val userName: String

    private val topicListener = EventsSubcriber()

    private val presenter = Presenter()

    init {
        userName = this.vaadinSecurity.authentication.name
        eventBus.subscribe(topicListener, userName)
        verticalLayout = VerticalLayout()

        val button = Button("Enviar notificación usando el EventBus")
        button.addClickListener( {presenter.enviarNotificacion()})

        verticalLayout.addComponent(button)

        compositionRoot = verticalLayout

    }//this.backend = backend;

    override fun detach() {
        eventBus.unsubscribe(this)
        eventBus.unsubscribe(topicListener)
        super.detach()
    }

    override fun enter(viewChangeEvent: ViewChangeListener.ViewChangeEvent) {
    }

    internal inner class EventsSubcriber {

        @EventBusListenerMethod
        fun mostrarMensajeRecibido(mensaje: Mensaje) {

            ui.access {

/*                msg.getItemProperty("Recibido").value = mensaje.timestamp
                msg.getItemProperty("De").value = mensaje.de
                val value = TextArea()
                value.value = mensaje.mensaje
                value.isReadOnly = true
                value.isWordwrap = true
                value.setWidth(200f, Sizeable.Unit.PIXELS)
                msg.getItemProperty("Mensaje").setValue(value)*/
            }
        }
    }

    internal inner class Presenter {

        fun enviarNotificacion() {
            //eventBus.publish(EventScope.APPLICATION, this@SolicitarPrestamo, mensajeAnotificar)
        }

    }
}
