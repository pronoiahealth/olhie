/*******************************************************************************
 * Copyright (c) 2013 Pronoia Health LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pronoia Health LLC - initial API and implementation
 *******************************************************************************/
package com.pronoiahealth.olhie.client.clientfactories;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.jboss.errai.ui.nav.client.local.DefaultPage;
import org.jboss.errai.ui.nav.client.local.Navigation;
import org.jboss.errai.ui.nav.client.local.spi.PageNode;

import com.pronoiahealth.olhie.client.navigation.AdminRole;
import com.pronoiahealth.olhie.client.navigation.AnonymousRole;
import com.pronoiahealth.olhie.client.navigation.AuthorRole;
import com.pronoiahealth.olhie.client.navigation.RegisteredRole;
import com.pronoiahealth.olhie.client.shared.constants.AppConstants;
import com.pronoiahealth.olhie.client.shared.constants.SecurityRoleEnum;

/**
 * Contains producer methods for client side injection<br/>
 * Responsibilities:<br/>
 * 1. Produces a screen timer
 * 
 * @author John DeStefano
 * @version 1.0
 * @since 5/25/2013
 * 
 */
@ApplicationScoped
public class ClientFactory {
	private Map<String, String> fileTypeViewableContentLst;

	public ClientFactory() {
	}

	/**
	 * Return the screen timeout value
	 * 
	 * @return
	 */
	@Produces
	@ScreenTimeout()
	public Integer getScreenTimeout() {
		return AppConstants.SCREEN_TIMEOUT;
	}

	/**
	 * Return the news fade interval
	 * 
	 * @return
	 */
	@Produces
	@NewsFadeInterval
	public Integer getNewsFadeInterval() {
		return AppConstants.NEWS_FADE_INTERVAL;
	}

	/**
	 * Return the ping fire interval
	 * 
	 * @return
	 */
	@Produces
	@PingFireTime
	public Integer getPingFireTime() {
		return AppConstants.PING_FIRE_INTERVAL;
	}

	/**
	 * Produces a map with the security role as the key and the pages that
	 * correspond to that role. This will be used in securing pages. The Page
	 * roles are hierarchical (Anonymous -> Registered -> Author -> Admin).
	 * Pages should be secured with the minimum role required to access them.
	 * 
	 * @param nav
	 * @return
	 */
	@Produces
	@PageRoleMap
	@Singleton
	public Map<String, Set<String>> getPageRolesMap(Navigation nav) {
		// Return object
		Map<String, Set<String>> pageRolesMap = new HashMap<String, Set<String>>();

		// Collect pages
		Set<String> anonPageNames = collectPageNames(nav
				.getPagesByRole(AnonymousRole.class));
		Set<String> registeredPageNames = collectPageNames(nav
				.getPagesByRole(RegisteredRole.class));
		Set<String> authorPageNames = collectPageNames(nav
				.getPagesByRole(AuthorRole.class));
		Set<String> adminPageNames = collectPageNames(nav
				.getPagesByRole(AdminRole.class));
		Set<String> defPages = collectPageNames(nav
				.getPagesByRole(DefaultPage.class));

		// Put in map based on role hierarchy default -> anonymous -> registered
		// -> author -> admin
		pageRolesMap.put(SecurityRoleEnum.ANONYMOUS.getName(),
				addCollections(anonPageNames, defPages));
		pageRolesMap.put(SecurityRoleEnum.REGISTERED.getName(),
				addCollections(registeredPageNames, defPages, anonPageNames));
		pageRolesMap.put(
				SecurityRoleEnum.AUTHOR.getName(),
				addCollections(authorPageNames, defPages, anonPageNames,
						registeredPageNames));
		pageRolesMap.put(
				SecurityRoleEnum.ADMIN.getName(),
				addCollections(adminPageNames, defPages, anonPageNames,
						registeredPageNames, authorPageNames));

		// Return
		return pageRolesMap;
	}

	/**
	 * Gets the page name from a PageNode object
	 * 
	 * @param pageNodes
	 * @return
	 */
	private Set<String> collectPageNames(Collection<PageNode<?>> pageNodes) {
		Set<String> retVal = new TreeSet<String>();
		if (pageNodes != null && pageNodes.size() > 0) {
			for (PageNode<?> node : pageNodes) {
				retVal.add(node.name());
			}
		}
		return retVal;
	}

	/**
	 * Adds collections to a starting collection
	 * 
	 * @param base
	 * @param otherSets
	 * @return
	 */
	private Set<String> addCollections(Set<String> base,
			Set<String>... otherSets) {
		if (otherSets != null) {
			for (Set<String> strSet : otherSets) {
				base.addAll(strSet);
			}
		}
		return base;
	}

	/**
	 * Get the name of the page
	 * 
	 * @param nav
	 * @return
	 */
	@Produces
	@DefaultAppPage
	public String getDefaultAnnotatedPageName(Navigation nav) {
		return collectPageNames(nav.getPagesByRole(DefaultPage.class))
				.iterator().next();
	}

	/**
	 * Content types that can be viewed in an iFrame. The value for the content
	 * type matchs the Get annotation suffix in the BookassetDownloadService
	 * 
	 * @return
	 */
	@Produces
	@FileTypeViewableContent
	public Map<String, String> getFileTypeViewableContentMap() {
		if (fileTypeViewableContentLst == null) {
			fileTypeViewableContentLst = new HashMap<String, String>();
			fileTypeViewableContentLst.put("application/pdf", "PDF");
			fileTypeViewableContentLst.put("text/html", "HTML");
			fileTypeViewableContentLst.put("text/plain", "TEXT");
		}
		return fileTypeViewableContentLst;
	}

}
