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
package org.vaadin.spring.samples.security.managed

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

    private var userName: TextField

    private var passwordField: PasswordField

    private var login: Button

    private var loginFailedLabel: Label

    private var loggedOutLabel: Label

    init {
        userName = TextField("Username")
        passwordField = PasswordField("Password")
        login = Button("Login")
        loginFailedLabel = Label()
        loggedOutLabel = Label("Good bye!")

        initLayout()
    }

    internal fun setLoggedOut(loggedOut: Boolean) {
        loggedOutLabel.isVisible = loggedOut
    }

    private fun initLayout() {
        val loginForm = FormLayout()
        loginForm.setSizeUndefined()
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
        loginFailedLabel.value = String.format("Login failed: %s", ex.message)
        loginFailedLabel.isVisible = true
    } catch (ex: Exception) {
        Notification.show("An unexpected error occurred", ex.message, Notification.Type.ERROR_MESSAGE)
        LoggerFactory.getLogger(javaClass).error("Unexpected error while logging in", ex)
    } finally {
        login.isEnabled = true
    }
}
