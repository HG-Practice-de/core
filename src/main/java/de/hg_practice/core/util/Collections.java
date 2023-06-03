package de.hg_practice.core.util;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * @author earomc
 * Created on Februar 21, 2022 | 13:44:56
 * ʕっ•ᴥ•ʔっ
 */

public class Collections {

    public static final Random RANDOM = new Random();

    @Nullable
    @SuppressWarnings("unchecked")
    public static <E> E getRandom(Collection<E> collection) {
        Object[] objects = collection.toArray();
        return (E) getRandom(objects);
    }


    @Nullable
    public static <E> E getRandom(E[] array) {
        int size = array.length;
        try {
            switch (array.length) {
                case 0:
                    return null;
                case 1:
                    return array[0];
                default:
                    //noinspection unchecked
                    return (E) getRandomFromArray(array);
            }
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Nullable
    public static <E> E getRandom(List<E> list) {
        try {
            switch (list.size()) {
                case 0:
                    return null;
                case 1:
                    return list.get(0);
                default:
                    //noinspection unchecked
                    return (E) getRandomFromList(list);
            }
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    private static Object getRandomFromArray(Object[] array) {
        return array[RANDOM.nextInt(array.length)];
    }

    private static Object getRandomFromList(List<?> list) {
        return list.get(RANDOM.nextInt(list.size()));
    }

}
