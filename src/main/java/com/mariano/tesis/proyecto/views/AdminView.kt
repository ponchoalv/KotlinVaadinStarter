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
import com.vaadin.spring.annotation.SpringComponent
import com.vaadin.spring.annotation.SpringView
import com.vaadin.spring.annotation.UIScope
import com.vaadin.ui.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.vaadin.spring.events.EventBus
import org.vaadin.spring.events.EventScope
import com.mariano.tesis.proyecto.entidades.Mensaje
import com.mariano.tesis.proyecto.Sections
import org.vaadin.spring.security.VaadinSecurity
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon
import org.vaadin.spring.sidebar.annotation.SideBarItem

import java.util.Date

/**
 * View that is available to administrators only.

 * @author Petter Holmström (petter@vaadin.com)
 */
@Secured("ROLE_ADMIN")
@SpringView(name = "admin")
@SpringComponent
@UIScope
@SideBarItem(sectionId = Sections.VIEWS, caption = "Administración")
@FontAwesomeIcon(FontAwesome.COGS)
open class AdminView
@Autowired
constructor(
        val vaadinSecurity: VaadinSecurity,
        val eventBus: EventBus.SessionEventBus) : VerticalLayout(), View {

    private val inputChatText: TextField
    private val inputUserDestination: ComboBox
    private val verticalLayout: VerticalLayout
    private val presenter = Presenter()

    init {

        inputChatText = TextField("Escribir Mesaje")
        inputUserDestination = ComboBox()
        verticalLayout = VerticalLayout()

        inputUserDestination.inputPrompt = "Usuario Destino del mensaje:"
        inputUserDestination.addItem("user")
        inputUserDestination.addItem("admin")

        val button = Button("Enviar")


        button.addClickListener({ presenter.enviarMensajeAUsuario() })

        verticalLayout.addComponent(inputChatText)
        verticalLayout.addComponent(inputUserDestination)
        verticalLayout.addComponent(button)

        addComponent(verticalLayout)
    }

    override fun enter(viewChangeEvent: ViewChangeListener.ViewChangeEvent) {
    }

    internal inner class Presenter {

        fun enviarMensajeAUsuario() {
            val mensaje = Mensaje(inputChatText.value, vaadinSecurity.authentication.name, inputUserDestination.value as String, Date())

            eventBus.publish(EventScope.APPLICATION,
                    mensaje.para, this,
                    mensaje)

        }

    }

}
