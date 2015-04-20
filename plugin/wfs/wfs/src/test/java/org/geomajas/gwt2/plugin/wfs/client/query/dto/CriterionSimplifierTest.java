
/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt2.plugin.wfs.client.query.dto;

import junit.framework.Assert;

import org.geomajas.gwt2.plugin.wfs.client.query.dto.CriterionDto;
import org.geomajas.gwt2.plugin.wfs.client.query.dto.CriterionSimplifier;
import org.geomajas.gwt2.plugin.wfs.client.query.dto.LogicalCriterionDto;
import org.geomajas.gwt2.plugin.wfs.client.query.dto.LogicalCriterionDto.Operator;
import org.geomajas.gwt2.plugin.wfs.client.query.dto.StringAttributeCriterionDto;
import org.junit.Test;

public class CriterionSimplifierTest {

	@Test
	public void testIncludeExclude() {
		CriterionSimplifier simplifier = new CriterionSimplifier();

		Assert.assertEquals(CriterionDto.EXCLUDE, simplifier.simplify(CriterionDto.EXCLUDE));
		Assert.assertEquals(CriterionDto.INCLUDE, simplifier.simplify(CriterionDto.INCLUDE));
		Assert.assertEquals(CriterionDto.INCLUDE,
				simplifier.simplify(new LogicalCriterionDto(Operator.OR, CriterionDto.INCLUDE, getSomeCriterionDto())));
		Assert.assertEquals(CriterionDto.EXCLUDE,
				simplifier.simplify(new LogicalCriterionDto(Operator.AND, CriterionDto.EXCLUDE, getSomeCriterionDto())));
		Assert.assertEquals(getSomeCriterionDto(),
				simplifier.simplify(new LogicalCriterionDto(Operator.OR, CriterionDto.EXCLUDE, getSomeCriterionDto())));
		Assert.assertEquals(getSomeCriterionDto(),
				simplifier.simplify(new LogicalCriterionDto(Operator.AND, CriterionDto.INCLUDE, getSomeCriterionDto())));
	}

	private CriterionDto getSomeCriterionDto() {
		return new StringAttributeCriterionDto("name", CriterionDto.EQ, "test");
	}
}
