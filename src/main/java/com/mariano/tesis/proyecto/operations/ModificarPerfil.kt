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
package com.mariano.tesis.proyecto.operations

import com.vaadin.server.FontAwesome
import com.vaadin.spring.annotation.SpringComponent
import com.vaadin.ui.Notification
import org.springframework.beans.factory.annotation.Autowired
import com.mariano.tesis.proyecto.Sections
import com.mariano.tesis.proyecto.repositorios.MyBackend
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon
import org.vaadin.spring.sidebar.annotation.SideBarItem


@SpringComponent
@SideBarItem(sectionId = Sections.OPERATIONS, caption = "Modificar perfil", order = 0)
@FontAwesomeIcon(FontAwesome.USER)
class ModificarPerfil
@Autowired
constructor(private val backend: MyBackend) : Runnable {

    override fun run() {
        Notification.show(backend.echo("Modificación de Perfil"))

    }
}
