/**
 * @author phellinger
 */
package com.rapidminer.extension.indbother.vertica;

import java.util.Locale;
import java.util.ResourceBundle;

import com.rapidminer.gui.MainFrame;
import com.rapidminer.tools.I18N;


/**
 * This class provides hooks for initialization and its methods are called via reflection by
 * RapidMiner Studio. Without this class and its predefined methods, an extension will not be
 * loaded.
 *
 * @author phellinger
 */
public final class PluginInitInDbOther {

	private PluginInitInDbOther() {
		// Utility class constructor
	}

	/**
	 * Initializes the plugin. Triggers the loading of the VerticaProvider class.
	 */
	public static void initPlugin() {
		VerticaProvider.INSTANCE.getClass();
	}

	/**
	 * Registers the GUI bundle for the Vertica database provider.
	 *
	 * @param mainframe
	 *            the RapidMiner Studio {@link MainFrame}.
	 */
	public static void initGui(MainFrame mainframe) {

		// register db-specific function descriptions
		I18N.registerGUIBundle(
				ResourceBundle.getBundle(
						String.format("com.rapidminer.extension.resources.i18n.db_%s_GUIInDatabaseProcessing",
								VerticaProvider.INSTANCE.getId()),
						Locale.getDefault(), PluginInitInDbOther.class.getClassLoader()));
	}

	/**
	 * Empty
	 */
	public static void initFinalChecks() {}

	/**
	 * Empty
	 */
	public static void initPluginManager() {}
}
