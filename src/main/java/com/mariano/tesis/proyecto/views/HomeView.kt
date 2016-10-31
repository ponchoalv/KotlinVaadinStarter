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
import com.vaadin.shared.ui.label.ContentMode
import com.vaadin.spring.annotation.SpringView
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme
import com.mariano.tesis.proyecto.Sections
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon
import org.vaadin.spring.sidebar.annotation.SideBarItem

/**
 * When the user logs in and there is no view to navigate to, this view will be shown.

 * @author Petter Holmström (petter@vaadin.com)
 */
@SpringView(name = "")
@SideBarItem(sectionId = Sections.VIEWS, caption = "Inicio", order = 0)
@FontAwesomeIcon(FontAwesome.HOME)
class HomeView : VerticalLayout(), View {

    init {
        isSpacing = true
        setMargin(true)

        val header = Label("Bienvenido al portal de autogestión de Banca Empresa")
        header.addStyleName(ValoTheme.LABEL_H1)
        addComponent(header)

        val body = Label("<p>En este portal usted podra: </p>" +
                        "<ul><li>Solicitar Préstamos</li>" +
                        "<li>Solicitar Tajretas de credito</li>" +
                        "<li>Solicitar la venta de valores diferidos o al dia</li></ul>")
        body.contentMode = ContentMode.HTML
        addComponent(body)
    }

    override fun enter(event: ViewChangeListener.ViewChangeEvent) {
    }
}
