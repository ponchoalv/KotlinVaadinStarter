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

import com.vaadin.server.CustomizedSystemMessages
import com.vaadin.server.SystemMessagesProvider
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.vaadin.spring.security.annotation.EnableVaadinManagedSecurity
import org.vaadin.spring.security.config.AuthenticationManagerConfigurer


@SpringBootApplication(exclude = arrayOf(SecurityAutoConfiguration::class))
@EnableVaadinManagedSecurity
open class Application {

    /**
     * Mensages personalizados para el error de sesión.
     */
    @Bean
    internal open fun systemMessagesProvider(): SystemMessagesProvider {
        return SystemMessagesProvider {
            val systemMessages = CustomizedSystemMessages()
            systemMessages.isSessionExpiredNotificationEnabled = false
            systemMessages
        }
    }

    /**
     * configurar el administrador de credenciales (por el momento solo en memoria se utilizan)
     */
       @Configuration
    internal open class AuthenticationConfiguration : AuthenticationManagerConfigurer {

        override fun configure(auth: AuthenticationManagerBuilder) {
            auth.inMemoryAuthentication().withUser("cliente_empresa").password("cliente_empresa").roles("USER").and().withUser("admin").password("admin").roles("ADMIN")
        }
    }

    companion object {

        @JvmStatic fun main(args: Array<String>) {
            SpringApplication.run(Application::class.java, *args)
        }
    }
}
