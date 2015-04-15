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

/**
 * {@link AttributeCriterionDto} of type {@link String}, combined with a diepte-interval.
 * 
 * @author Patrick De Baets
 * 
 */
public class DiepteAttributeCriterionDto extends AttributeCriterionDto<String> {

    private Double diepte1;
    
    private Double diepte2;
    
	private DiepteAttributeCriterionDto() {
		super();
	}
	
    @Override
    public DiepteAttributeCriterionDto clone() {
        return new DiepteAttributeCriterionDto(getAttributeName(), getOperation(), getValue(), getDiepte1(),
                getDiepte2());
    }

	public DiepteAttributeCriterionDto(String attributeName, String operation, String value) {
		super(attributeName, operation, value);
	}

    public DiepteAttributeCriterionDto(String attributeName, String operation, String value, Double van, Double tot) {
        super(attributeName, operation, value);
        this.diepte1 = van;
        this.diepte2 = tot;
    }

    public Double getDiepte1() {
        return diepte1;
    }

    public void setDiepte1(Double van) {
        this.diepte1 = van;
    }

    public Double getDiepte2() {
        return diepte2;
    }

    public void setDiepte2(Double tot) {
        this.diepte2 = tot;
    }
}
