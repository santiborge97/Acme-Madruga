
package controllers.anonymous;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.ConfigurationService;
import services.FloatService;
import services.ProcessionService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Float;
import domain.Procession;

@Controller
@RequestMapping("/float")
public class FloatController extends AbstractController {

	@Autowired
	private FloatService			floatService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private ProcessionService		processionService;

	@Autowired
	private BrotherhoodService		brotherhoodService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int brotherhoodId) {

		final ModelAndView result;
		final Collection<Float> floats;

		final String banner = this.configurationService.findConfiguration().getBanner();

		final Brotherhood notFound = this.brotherhoodService.findOne(brotherhoodId);

		if (notFound == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {

			floats = this.floatService.findFloatsByBrotherhoodId(brotherhoodId);

			result = new ModelAndView("float/listAnonimo");
			result.addObject("floats", floats);
			result.addObject("requestURI", "float/list.do");
			result.addObject("pagesize", 5);
			result.addObject("banner", banner);
			result.addObject("language", LocaleContextHolder.getLocale().getLanguage());
			result.addObject("autoridad", "");

		}
		return result;

	}

	@RequestMapping(value = "/procession/list", method = RequestMethod.GET)
	public ModelAndView listByProcession(@RequestParam final int processionId) {

		final ModelAndView result;
		final Collection<Float> floats;
		final String banner = this.configurationService.findConfiguration().getBanner();

		final Procession procession = this.processionService.findOne(processionId);

		if (procession == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {

			floats = procession.getFloats();

			result = new ModelAndView("float/listAnonimo");
			result.addObject("floats", floats);
			result.addObject("requestURI", "float/procession/list.do");
			result.addObject("pagesize", 5);
			result.addObject("banner", banner);
			result.addObject("language", LocaleContextHolder.getLocale().getLanguage());
			result.addObject("autoridad", "");

		}
		return result;

	}
}
