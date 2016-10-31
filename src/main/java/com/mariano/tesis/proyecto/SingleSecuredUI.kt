package com.mariano.tesis.proyecto

import com.vaadin.annotations.Push
import com.vaadin.annotations.Theme
import com.vaadin.server.DefaultErrorHandler
import com.vaadin.server.ErrorEvent
import com.vaadin.server.VaadinRequest
import com.vaadin.shared.communication.PushMode
import com.vaadin.shared.ui.ui.Transport
import com.vaadin.spring.annotation.SpringUI
import com.vaadin.ui.Notification
import com.vaadin.ui.UI
import com.vaadin.ui.themes.ValoTheme
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.vaadin.spring.events.EventBus
import org.vaadin.spring.events.annotation.EventBusListenerMethod
import org.vaadin.spring.security.VaadinSecurity
import org.vaadin.spring.security.util.SuccessfulLoginEvent
import org.vaadin.spring.security.util.SecurityExceptionUtils

@SpringUI
@Theme(ValoTheme.THEME_NAME)
@Push
class SingleSecuredUI : UI() {
    @Autowired
    lateinit var applicationContext: ApplicationContext
    @Autowired
    lateinit var vaadinSecurity: VaadinSecurity
    @Autowired
    lateinit var eventBus: EventBus.SessionEventBus

    override fun init(request: VaadinRequest) {
        page.setTitle("Portal de autogestión de Banca Empresa")

        // Let's register a custom error handler to make the 'access denied' messages a bit friendlier.
        errorHandler = object : DefaultErrorHandler() {
            override fun error(event: ErrorEvent) {
                if (SecurityExceptionUtils.isAccessDeniedException(event.throwable)) {
                    Notification.show("Acceso restringido.")
                } else {
                    super.error(event)
                }
            }
        }

        if (vaadinSecurity.isAuthenticated) {
            showMainScreen()
        } else {
            showLoginScreen(request.getParameter("goodbye") != null)
            request.removeAttribute("goodbye")
        }
    }

     override fun attach() {
        super.attach()
        eventBus.subscribe(this)
    }

    override fun detach() {
        eventBus.unsubscribe(this)
    }

    private fun showLoginScreen(loggedOut: Boolean) {
        val loginScreen = applicationContext.getBean<LoginScreen>(LoginScreen::class.java)
        loginScreen.setLoggedOut(loggedOut)
        content = loginScreen
    }

    private fun showMainScreen() {
        content = applicationContext.getBean<MainScreen>(MainScreen::class.java)
    }

    @EventBusListenerMethod
    internal fun onLogin(loginEvent: SuccessfulLoginEvent) {
        pushConfiguration.transport = Transport.WEBSOCKET
        pushConfiguration.pushMode = PushMode.AUTOMATIC

        if (loginEvent.source == this) {
            access { showMainScreen() }
        } else {
            // We cannot inject the Main Screen if the event was fired from another UI, since that UI's scope would be active
            // and the main screen for that UI would be injected. Instead, we just reload the page and let the init(...) method
            // do the work for us.
            access { page.reload() }
        }
    }

}
