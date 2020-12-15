package cc.hyperium.utils;

import cc.hyperium.Hyperium;

/**
 * A utility for working with the client's version.
 *
 * @see Hyperium#HYPERIUMJB_VERSION
 */
public class VersionUtil {
    /**
     * Returns if the client's version matches the passed version exactly.
     *
     * @param version The version to check against.
     * @return If the client is the exact version.
     */
    public static boolean isExactVersion(final int version) {
        return version == Hyperium.HYPERIUMJB_VERSION;
    }

    /**
     * Returns if the client is newer than the passed version.
     *
     * @param version The version to check against.
     * @return If the client is newer than the passed version.
     */
    public static boolean isNewerThan(final int version) {
        return version > Hyperium.HYPERIUMJB_VERSION;
    }
}
