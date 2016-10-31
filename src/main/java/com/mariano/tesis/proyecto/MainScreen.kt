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

import com.vaadin.navigator.Navigator
import com.vaadin.spring.annotation.SpringComponent
import com.vaadin.spring.annotation.UIScope
import com.vaadin.spring.navigator.SpringViewProvider
import com.vaadin.ui.*
import org.springframework.beans.factory.annotation.Autowired
import com.mariano.tesis.proyecto.views.AccessDeniedView
import com.mariano.tesis.proyecto.views.ErrorView
import org.vaadin.spring.security.VaadinSecurity
import org.vaadin.spring.sidebar.components.ValoSideBar
import org.vaadin.spring.sidebar.security.VaadinSecurityItemFilter


@UIScope
@SpringComponent
class MainScreen
@Autowired
constructor(vaadinSecurity: VaadinSecurity, springViewProvider: SpringViewProvider, sideBar: ValoSideBar) : CustomComponent() {
    init {
        val layout = HorizontalLayout()
        layout.setSizeFull()
        compositionRoot = layout
        setSizeFull()

        // By adding a security item filter, only views that are accessible to the user will show up in the side bar.
        sideBar.itemFilter = VaadinSecurityItemFilter(vaadinSecurity)
        layout.addComponent(sideBar)

        val viewContainer = CssLayout()
        viewContainer.setSizeFull()
        layout.addComponent(viewContainer)
        layout.setExpandRatio(viewContainer, 1f)

        val navigator = Navigator(UI.getCurrent(), viewContainer)

        // Without an AccessDeniedView, the view provider would act like the restricted views did not exist at all.
        springViewProvider.accessDeniedViewClass = AccessDeniedView::class.java
        navigator.addProvider(springViewProvider)
        navigator.setErrorView(ErrorView::class.java)
        navigator.navigateTo(navigator.state)
    }
}
