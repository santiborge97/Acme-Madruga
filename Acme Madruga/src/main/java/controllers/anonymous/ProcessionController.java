
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
import services.ProcessionService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Procession;

@Controller
@RequestMapping("/procession")
public class ProcessionController extends AbstractController {

	@Autowired
	private ProcessionService		processionService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private BrotherhoodService		brotherhoodService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int brotherhoodId) {
		final ModelAndView result;
		final String banner = this.configurationService.findConfiguration().getBanner();

		final Brotherhood notFound = this.brotherhoodService.findOne(brotherhoodId);

		if (notFound == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {

			final Collection<Procession> processions;

			processions = this.processionService.findProcessionCanBeSeenOfBrotherhoodId(brotherhoodId);
			final int finderResult = this.configurationService.findConfiguration().getFinderResult();

			result = new ModelAndView("procession/listAnonimo");
			result.addObject("processions", processions);
			result.addObject("requestURI", "procession/list.do");
			result.addObject("pagesize", finderResult);
			result.addObject("AreInFinder", false);
			result.addObject("banner", banner);
			result.addObject("language", LocaleContextHolder.getLocale().getLanguage());
			result.addObject("autoridad", "");
		}
		return result;

	}
}
