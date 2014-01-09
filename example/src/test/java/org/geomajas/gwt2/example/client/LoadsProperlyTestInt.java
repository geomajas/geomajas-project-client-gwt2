/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.example.client;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Verify that the application loads properly.
 * 
 * @author Jan De Moerloose
 */
public class LoadsProperlyTestInt {

	private WebDriver driver;

	@Before
	public void setUp() {
		driver = new FirefoxDriver();
	}

	@After
	public void tearDown() {
		driver.quit();
	}

	/**
	 * Simple test which verifies that the demo starts.
	 * 
	 * @throws Exception oops
	 */
	@Test
	public void testDemoLoadsProperly() throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.pollingEvery(500, TimeUnit.MILLISECONDS);

		driver.get("http://localhost:9080/");

		// the login window should appear
		wait.until(new ExpectedCondition<Boolean>() {

			public Boolean apply(WebDriver d) {
				return d.findElements(By.className("gwt-ListBox")).size() > 0;
			}
		});
	}
}
