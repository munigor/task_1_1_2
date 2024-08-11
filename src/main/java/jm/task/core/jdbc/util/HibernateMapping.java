package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;

public final class HibernateMapping {
    private HibernateMapping() {
    }

    public static Class<?>[] getClasses() {
        return new Class[] {
            User.class
        };
    }
}
