/*
 Copyright (c) 2013-2018, Stephen Gold
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright
 notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 notice, this list of conditions and the following disclaimer in the
 documentation and/or other materials provided with the distribution.
 * Neither the name of the copyright holder nor the names of its contributors
 may be used to endorse or promote products derived from this software without
 specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jme3utilities;

import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.SceneProcessor;
import com.jme3.renderer.ViewPort;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.imageio.ImageIO;

/**
 * Miscellaneous utility methods in the jme3-utilities-heart library.
 *
 * @author Stephen Gold sgold@sonic.net
 */
public class Misc {
    // *************************************************************************
    // constants and loggers

    /**
     * message logger for this class
     */
    final private static Logger logger
            = Logger.getLogger(Misc.class.getName());
    // *************************************************************************
    // constructors

    /**
     * A private constructor to inhibit instantiation of this class.
     */
    private Misc() {
    }
    // *************************************************************************
    // new methods exposed

    /**
     * Test whether assertions are enabled.
     *
     * @return true if enabled, otherwise false
     */
    public static boolean areAssertionsEnabled() {
        boolean enabled = false;
        assert enabled = true; // Note: intentional side effect.

        return enabled;
    }

    /**
     * Open the specified web page in a new browser or browser tab.
     *
     * @param startUriString URI of the web page (not null)
     * @return true if successful, otherwise false
     */
    public static boolean browseWeb(String startUriString) {
        Validate.nonNull(startUriString, "start uri");

        boolean success = false;
        if (Desktop.isDesktopSupported()
                && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                URI startUri = new URI(startUriString);
                Desktop.getDesktop().browse(startUri);
                success = true;
            } catch (IOException | URISyntaxException exception) {
            }
        }

        return success;
    }

    /**
     * Detach all app states which are subclasses of a specified class.
     *
     * @param <T> class to scan for
     * @param stateManager (not null)
     * @param whichClass (not null)
     */
    public static <T extends AppState> void detachAll(
            AppStateManager stateManager, Class<T> whichClass) {
        Validate.nonNull(whichClass, "class");

        AppState state = stateManager.getState(whichClass);
        while (state != null) {
            stateManager.detach(state);
            state = stateManager.getState(whichClass);
        }
    }

    /**
     * Construct a map from drive paths (roots) to file objects.
     *
     * @return a new map of absolute filesystem paths to files
     */
    public static Map<String, File> driveMap() {
        Map<String, File> result = new TreeMap<>();
        File[] roots = File.listRoots();
        for (File root : roots) {
            if (root.isDirectory()) {
                String absoluteDirPath = root.getAbsolutePath();
                absoluteDirPath = absoluteDirPath.replaceAll("\\\\", "/");
                File oldFile = result.put(absoluteDirPath, root);
                assert oldFile == null : oldFile;
            }
        }

        return result;
    }

    /**
     * Access the pre-existing filter post processor of the specified viewport,
     * or if it has none, add a new FPP and use that.
     *
     * @param viewPort which view port (not null)
     * @param assetManager (not null)
     * @return not null
     * @deprecated use
     * {@link #getFpp(com.jme3.renderer.ViewPort, com.jme3.asset.AssetManager, int)}
     */
    @Deprecated
    public static FilterPostProcessor getFpp(ViewPort viewPort,
            AssetManager assetManager) {
        Validate.nonNull(viewPort, "viewport");
        Validate.nonNull(assetManager, "asset manager");

        FilterPostProcessor fpp = getFpp(viewPort, assetManager, 0);

        return fpp;
    }

    /**
     * Access the pre-existing filter post processor of the specified view port,
     * or if it has none, add a new FPP and use that.
     *
     * @param viewPort which view port (not null)
     * @param assetManager (not null)
     * @param numSamples number of samples for anti-aliasing (&ge;1, &le;16) or
     * 0 for the FPP default
     * @return not null
     */
    public static FilterPostProcessor getFpp(ViewPort viewPort,
            AssetManager assetManager, int numSamples) {
        Validate.nonNull(viewPort, "viewport");
        Validate.nonNull(assetManager, "asset manager");
        Validate.inRange(numSamples, "number of samples", 0, 16);

        for (SceneProcessor processor : viewPort.getProcessors()) {
            if (processor instanceof FilterPostProcessor) {
                return (FilterPostProcessor) processor;
            }
        }
        /*
         * Add a new filter post-processor.
         */
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        if (numSamples > 0) {
            fpp.setNumSamples(numSamples);
        }
        viewPort.addProcessor(fpp);

        return fpp;
    }

    /**
     * Read the verbose version string for this library.
     *
     * @return project name, library name, branch, and revision
     */
    public static String getVersion() {
        return "jme3-utilities jme3-utilities-heart master $Rev: 2.3.0+1 $";
    }

    /**
     * Read the terse version string for this library.
     *
     * @return branch and revision (not null, not empty)
     */
    public static String getVersionShort() {
        String verbose = getVersion();
        String[] words = verbose.split("\\s+");
        assert words.length == 6 : words.length;
        String result = String.format("%s %s", words[2], words[4]);

        assert !result.isEmpty();
        return result;
    }

    /**
     * Generate an absolute filesystem path to the named file in the user's home
     * directory.
     *
     * @param fileName file name to use (not null, not empty)
     * @return filesystem path (not null, not empty)
     */
    public static String homePath(String fileName) {
        Validate.nonEmpty(fileName, "file name");

        String homePath = System.getProperty("user.home");
        File file = new File(homePath, fileName);
        String result = file.getAbsolutePath();
        result = result.replaceAll("\\\\", "/");

        assert !result.isEmpty();
        return result;
    }

    /**
     * Enumerate all entries (in the specified JAR or ZIP) whose names begin
     * with the specified prefix.
     *
     * @param zipPath filesystem path to the JAR or ZIP (not null, not empty)
     * @param namePrefix (not null)
     * @return a new list of entry names
     */
    public static List<String> listZipEntries(String zipPath,
            String namePrefix) {
        Validate.nonEmpty(zipPath, "zip path");
        Validate.nonNull(namePrefix, "name prefix");

        List<String> result = new ArrayList<>(90);
        try (FileInputStream fileIn = new FileInputStream(zipPath);
                ZipInputStream zipIn = new ZipInputStream(fileIn)) {
            for (ZipEntry entry = zipIn.getNextEntry();
                    entry != null;
                    entry = zipIn.getNextEntry()) {
                String entryName = "/" + entry.getName();
                if (entryName.startsWith(namePrefix)) {
                    result.add(entryName);
                }
            }
        } catch (IOException e) {
            // quit reading entries
        }

        return result;
    }

    /**
     * Set the specified pixel to the specified brightness and opacity.
     *
     * @param graphics rendering context of the pixel (not null)
     * @param x pixel's 1st coordinate (&lt;width, &ge;0)
     * @param y pixel's 2nd coordinate (&lt;height, &ge;0)
     * @param brightness (&le;1, &ge;0, 0 &rarr; black, 1 &rarr; white)
     * @param opacity (&le;1, &ge;0, 0 &rarr; transparent, 1 &rarr; opaque)
     */
    public static void setGrayPixel(Graphics2D graphics, int x, int y,
            float brightness, float opacity) {
        GraphicsConfiguration configuration = graphics.getDeviceConfiguration();
        Rectangle bounds = configuration.getBounds();
        Validate.inRange(x, "X coordinate", 0, bounds.width - 1);
        Validate.inRange(y, "Y coordinate", 0, bounds.height - 1);
        Validate.fraction(brightness, "brightness");
        Validate.fraction(opacity, "opacity");

        Color color = new Color(brightness, brightness, brightness, opacity);
        graphics.setColor(color);
        graphics.fillRect(x, y, 1, 1);
    }

    /**
     * Alter the logging levels of all known loggers.
     *
     * @param newLevel (not null)
     */
    public static void setLoggingLevels(Level newLevel) {
        Validate.nonNull(newLevel, "level");
        Logger.getLogger("").setLevel(newLevel);
    }

    /**
     * Write an image to a PNG file, attempting to overwrite any pre-existing
     * file.
     *
     * @param filePath path to the output file (not null, not empty)
     * @param image image to be written (not null)
     * @throws IOException if the image cannot be written
     */
    public static void writeMap(String filePath, RenderedImage image)
            throws IOException {
        Validate.nonEmpty(filePath, "path");
        Validate.nonNull(image, "image");

        File textureFile = new File(filePath);
        try {
            /*
             * If a parent directory/folder is needed, create it.
             */
            File parentDirectory = textureFile.getParentFile();
            if (parentDirectory != null && !parentDirectory.exists()) {
                boolean success = parentDirectory.mkdirs();
                if (!success) {
                    throw new IOException();
                }
            }

            ImageIO.write(image, "png", textureFile);
            logger.log(Level.INFO, "wrote texture to {0}",
                    MyString.quote(filePath));

        } catch (IOException exception) {
            logger.log(Level.SEVERE, "write to {0} failed",
                    MyString.quote(filePath));
            boolean success = textureFile.delete();
            if (success) {
                logger.log(Level.INFO, "deleted file {0}",
                        MyString.quote(filePath));
            } else {
                logger.log(Level.SEVERE, "delete of {0} failed",
                        MyString.quote(filePath));
            }
            throw exception;
        }
    }
}
