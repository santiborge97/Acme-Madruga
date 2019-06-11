
package controllers.brotherhood;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;

import services.AreaService;
import services.BrotherhoodService;
import services.ConfigurationService;
import domain.Area;
import domain.Brotherhood;

@Controller
@RequestMapping("/area/brotherhood")
public class AreaBrotherhoodController extends AbstractController {

	// Constructors -----------------------------------------------------------

	public AreaBrotherhoodController() {
		super();
	}


	// Services ---------------------------------------------------------------
	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private AreaService				areaService;


	@RequestMapping(value = "/listAreas", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		Collection<Area> areas;
		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();

		if (brotherhood.getArea() == null)
			areas = this.areaService.findAll();
		else {
			areas = new ArrayList<>();
			areas.add(brotherhood.getArea());
		}

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("brotherhood/listAreas");
		result.addObject("areas", areas);
		result.addObject("banner", banner);
		result.addObject("requestURI", "area/brotherhood/listAreas.do");

		return result;
	}

	@RequestMapping(value = "/select", method = RequestMethod.GET)
	public ModelAndView select(@RequestParam final int areaId) {
		final ModelAndView result;
		final Area area;

		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();

		area = this.areaService.findOne(areaId);
		Assert.notNull(area);

		if (brotherhood.getArea() != null)
			result = new ModelAndView("redirect:listAreas.do");
		else {
			area.getBrotherhoods().add(brotherhood);
			brotherhood.setArea(area);
			this.brotherhoodService.save(brotherhood);
			this.areaService.save(area);
			result = new ModelAndView("redirect:listAreas.do");
		}
		return result;
	}

	/*
	 * protected ModelAndView createListView(final Collection<Area> areas, final Area selectedArea, final String messageCode) {
	 * ModelAndView result;
	 * 
	 * final String banner = this.configurationService.findConfiguration().getBanner();
	 * 
	 * result = new ModelAndView("administrator/listAreas");
	 * result.addObject("areas", areas);
	 * result.addObject("selectedArea", selectedArea);
	 * result.addObject("banner", banner);
	 * result.addObject("messageError", messageCode);
	 * final String countryCode = this.configurationService.findConfiguration().getCountryCode();
	 * 
	 * return result;
	 * }
	 */
}
