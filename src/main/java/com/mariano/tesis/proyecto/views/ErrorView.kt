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
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme


class ErrorView : VerticalLayout(), View {

    private val message: Label

    init {
        setMargin(true)
        message = Label()
        addComponent(message)
        message.setSizeUndefined()
        message.addStyleName(ValoTheme.LABEL_FAILURE)
    }

    override fun enter(event: ViewChangeListener.ViewChangeEvent) {
        message.value = String.format("No existe la vista solicitada: %s", event.viewName)
    }
}
