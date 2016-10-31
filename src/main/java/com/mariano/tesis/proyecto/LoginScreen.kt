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
package com.mariano.tesis.proyecto

import com.vaadin.event.ShortcutAction
import com.vaadin.spring.annotation.SpringComponent
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.CustomComponent
import com.vaadin.ui.FormLayout
import com.vaadin.ui.Label
import com.vaadin.ui.Notification
import com.vaadin.ui.PasswordField
import com.vaadin.ui.TextField
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.AuthenticationException
import org.vaadin.spring.annotation.PrototypeScope
import org.vaadin.spring.events.EventBus
import org.vaadin.spring.security.VaadinSecurity
import org.vaadin.spring.security.util.SuccessfulLoginEvent

/**
 * Full-screen UI component that allows the user to login.

 * @author Petter Holmström (petter@vaadin.com)
 */
@PrototypeScope
@SpringComponent
class LoginScreen
@Autowired
constructor(private val vaadinSecurity: VaadinSecurity, private val eventBus: EventBus.SessionEventBus) : CustomComponent() {

    private val userName: TextField

    private val passwordField: PasswordField

    private val login: Button

    private val loginFailedLabel: Label

    private val loggedOutLabel: Label
    private val header: Label

    init {
        header = Label("Banca Empresa")
        header.addStyleName(ValoTheme.LABEL_H1)
        userName = TextField("Nombre de Usuario")
        passwordField = PasswordField("Contraseña")
        login = Button("Entrar")
        loginFailedLabel = Label()
        loggedOutLabel = Label("¡Tenga un buen día!")

        initLayout()
    }

    internal fun setLoggedOut(loggedOut: Boolean) {
        loggedOutLabel.isVisible = loggedOut
    }

    private fun initLayout() {
        val loginForm = FormLayout()
        loginForm.setSizeUndefined()
        loginForm.addComponent(header)
        loginForm.addComponent(userName)
        loginForm.addComponent(passwordField)
        loginForm.addComponent(login)


        login.addStyleName(ValoTheme.BUTTON_PRIMARY)
        login.isDisableOnClick = true
        login.setClickShortcut(ShortcutAction.KeyCode.ENTER)
        login.addClickListener { login() }

        val loginLayout = VerticalLayout()
        loginLayout.setSizeUndefined()
        loginLayout.addComponent(loginFailedLabel)
        loginLayout.setComponentAlignment(loginFailedLabel, Alignment.BOTTOM_CENTER)


        loginFailedLabel.setSizeUndefined()
        loginFailedLabel.addStyleName(ValoTheme.LABEL_FAILURE)
        loginFailedLabel.isVisible = false


        loginLayout.addComponent(loggedOutLabel)
        loginLayout.setComponentAlignment(loggedOutLabel, Alignment.BOTTOM_CENTER)

        loggedOutLabel.setSizeUndefined()
        loggedOutLabel.addStyleName(ValoTheme.LABEL_SUCCESS)
        loggedOutLabel.isVisible = false

        loginLayout.addComponent(loginForm)
        loginLayout.setComponentAlignment(loginForm, Alignment.TOP_CENTER)

        val rootLayout = VerticalLayout(loginLayout)
        rootLayout.setSizeFull()
        rootLayout.setComponentAlignment(loginLayout, Alignment.MIDDLE_CENTER)
        compositionRoot = rootLayout
        setSizeFull()
    }

    private fun login() = try {
        loggedOutLabel.isVisible = false

        val password = passwordField.value
        passwordField.value = ""

        val authentication = vaadinSecurity.login(userName.value, password)
        eventBus.publish(this, SuccessfulLoginEvent(ui, authentication))
    } catch (ex: AuthenticationException) {
        userName.focus()
        userName.selectAll()
        loginFailedLabel.value = String.format("Ha fallado el inicio de sesión: %s", ex.message)
        loginFailedLabel.isVisible = true
    } catch (ex: Exception) {
        Notification.show("Un error inesperado ha ocurrido: ", ex.message, Notification.Type.ERROR_MESSAGE)
        LoggerFactory.getLogger(javaClass).error("Un error inesperado a ocurrido en la pantalla de Login", ex)
    } finally {
        login.isEnabled = true
    }
}
