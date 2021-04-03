package it.units.listeners;


import com.googlecode.objectify.ObjectifyService;
import it.units.entities.storage.Attore;
import it.units.entities.storage.Files;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ObjectifyStarter implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        ObjectifyService.register(Attore.class);
        ObjectifyService.register(Files.class);
    }

    public void contextDestroyed(ServletContextEvent sce) {

    }
}
