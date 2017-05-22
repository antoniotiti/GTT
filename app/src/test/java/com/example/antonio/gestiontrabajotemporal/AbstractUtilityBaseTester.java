package com.example.antonio.gestiontrabajotemporal;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;

/**
 * Esta clase puede utilizarse como una base para clases de pruebas unitarias que prueban las
 * clases de utilidad típicas en Java.
 * <p>
 * Incluye una prueba que comprueba estas condiciones fundamentales en una clase Utility:
 * 1. Deben tener un constructor privado.
 * 2. El constructor lanza una UnsupportedOperationException cuando es instanciado.
 * 3. Deben ser clases finales.
 * 4. Los métodos deben ser estáticos.
 * 5. Los atributos deben ser estáticos.
 * <p>
 * Para usarlo, simplemente se crea una clase de prueba unitaria que extienda esta clase, indicando
 * en el parámetro genérico la clase bajo prueba.
 * <p>
 * Con esto, obtendrá automáticamente los test para la 5 condiciones anteriores.
 *
 * @param <T> La clase de utilidad que desea probar.
 */
public abstract class AbstractUtilityBaseTester<T> {

    @SuppressWarnings("unchecked")
    @Test
    public void shouldTestPrivateConstructor() {
        Constructor<T> constructor;
        Class<T> persistentClass = null;
        try {
            persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                    .getActualTypeArguments()[0];

            checkFinalClass(persistentClass);

            constructor = checkConstructor(persistentClass);

            checkMethodsAreStatic(persistentClass);
            checkFieldsAreStatic(persistentClass);
            checkInstanciation(constructor, persistentClass);
        } catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            Assert.assertEquals(
                    "The cause of the inner exception must be UnsupportedOperationException when trying to instanciate the class "
                            + persistentClass.getName() + e.getClass().getName(),
                    UnsupportedOperationException.class.getName(), e.getCause().getClass().getName());
        }
    }

    /**
     * Método que comprueba si la clase es final.
     *
     * @param persistentClass Clase ha comprobar
     */
    private void checkFinalClass(final Class<T> persistentClass) {
        Assert.assertTrue("La clase " + persistentClass.getName() + " debe ser final",
                Modifier.isFinal(persistentClass.getModifiers()));
    }

    /**
     * Método que comprueba si el constructor de la clase es privado o no y lo devuelve.
     *
     * @param persistentClass Clase ha comprobar
     * @return el constructor de la clase.
     */
    @SuppressWarnings("unchecked")
    private Constructor<T> checkConstructor(final Class<T> persistentClass) {
        Constructor<T> constructor;
        constructor = (Constructor<T>) persistentClass.getDeclaredConstructors()[0];
        Assert.assertTrue("EL constructor de la clase " + persistentClass.getName() + ", debe ser privado",
                Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        return constructor;
    }

    /**
     * Método que comprueba si al intentar instanciar el constructor de la clase lanza una excepción
     * UnsupportedOperationException.
     *
     * @param constructor     Constructor de la clase a comprobar
     * @param persistentClass Clase ha comprobar
     */
    private void checkInstanciation(final Constructor<T> constructor, final Class<T> persistentClass)
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
        final T instance = constructor.newInstance();
        Assert.assertTrue("No se produce ninguna excepción al instanciar " + persistentClass.getName()
                + ", se debe lanzar una excepción UnsupportedOperationException", false);
    }

    /**
     * Método que comprueba si los métodos de la clase son Static.
     *
     * @param persistentClass Clase ha comprobar
     */
    private void checkMethodsAreStatic(final Class<T> persistentClass) {
        final Method[] methods = persistentClass.getDeclaredMethods();
        for (Method method : methods) {
            Assert.assertTrue(
                    "El método " + method.getName() + " de la clase " + persistentClass.getName()
                            + " no es Static. Todos los métodos deben ser Static",
                    Modifier.isStatic(method.getModifiers()));
        }
    }

    /**
     * Método que comprueba si los atributos de la clase son Static.
     *
     * @param persistentClass Clase ha comprobar
     */
    private void checkFieldsAreStatic(final Class<T> persistentClass) {
        final Field[] fields = persistentClass.getDeclaredFields();
        for (Field field : fields) {
            Assert.assertTrue(
                    "EL atributo " + field.getName() + " de la clase " + persistentClass.getName()
                            + " no es Static. Todos los atributos deben ser Static",
                    Modifier.isStatic(field.getModifiers()));
        }
    }
}