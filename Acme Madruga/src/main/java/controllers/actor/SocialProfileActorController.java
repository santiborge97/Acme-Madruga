
package controllers.actor;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;

import services.ActorService;
import services.ConfigurationService;
import services.SocialProfileService;
import domain.Actor;
import domain.SocialProfile;

@Controller
@RequestMapping("/socialProfile/administrator,brotherhood,member")
public class SocialProfileActorController extends AbstractController {

	@Autowired
	SocialProfileService			socialProfileService;

	@Autowired
	ActorService					actorService;

	@Autowired
	private ConfigurationService	configurationService;


	//List-------------------------------------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<SocialProfile> socialProfiles;
		Actor actor;

		final String banner = this.configurationService.findConfiguration().getBanner();

		actor = this.actorService.findOne(this.actorService.findByPrincipal().getId());
		Assert.notNull(actor);

		socialProfiles = this.socialProfileService.findAllByActor(actor.getId());

		result = new ModelAndView("socialProfile/list");
		result.addObject("socialProfiles", socialProfiles);
		result.addObject("banner", banner);
		result.addObject("requestURI", "socialProfile/administrator,brotherhood,member/list.do");

		return result;

	}

	//Create-------------------------------------------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		final SocialProfile socialProfile;

		socialProfile = this.socialProfileService.create();
		result = this.createEditModelAndView(socialProfile);

		return result;

	}

	//Display----------------------------------------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int socialProfileId) {
		final ModelAndView result;
		final SocialProfile socialProfile;

		final String banner = this.configurationService.findConfiguration().getBanner();
		final SocialProfile socialProfileNotFound = this.socialProfileService.findOne(socialProfileId);

		if (socialProfileNotFound == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {

			final Boolean security = this.socialProfileService.socialProfileSecurity(socialProfileId);

			if (security) {
				socialProfile = this.socialProfileService.findOne(socialProfileId);

				result = new ModelAndView("socialProfile/display");
				result.addObject("socialProfile", socialProfile);
				result.addObject("banner", banner);
			} else
				result = new ModelAndView("redirect:/welcome/index.do");
		}
		return result;
	}
	//Edit--------------------------------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int socialProfileId) {
		ModelAndView result;
		SocialProfile socialProfile;
		Boolean security;

		final String banner = this.configurationService.findConfiguration().getBanner();
		final SocialProfile socialProfileNotFound = this.socialProfileService.findOne(socialProfileId);

		if (socialProfileNotFound == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {
			security = this.socialProfileService.socialProfileSecurity(socialProfileId);

			if (security) {
				socialProfile = this.socialProfileService.findOne(socialProfileId);
				result = this.createEditModelAndView(socialProfile);
			} else
				result = new ModelAndView("redirect:/welcome/index.do");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute(value = "socialProfile") SocialProfile socialProfile, final BindingResult binding) {
		ModelAndView result;

		socialProfile = this.socialProfileService.reconstruct(socialProfile, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(socialProfile, null);
		else
			try {
				this.socialProfileService.save(socialProfile);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(socialProfile, "socialProfile.commit.error");

			}

		return result;
	}

	//Delete--------------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final SocialProfile socialProfile) {
		ModelAndView result;

		final SocialProfile socialProfileFind = this.socialProfileService.findOne(socialProfile.getId());
		final String banner = this.configurationService.findConfiguration().getBanner();

		if (socialProfileFind == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else
			try {
				this.socialProfileService.delete(socialProfileFind);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(socialProfileFind, "float.commit.error");
			}

		return result;
	}

	//Other business methods-------------------------------------------------
	protected ModelAndView createEditModelAndView(final SocialProfile socialProfile) {
		ModelAndView result;

		result = this.createEditModelAndView(socialProfile, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final SocialProfile socialProfile, final String messageCode) {
		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("socialProfile/edit");
		result.addObject("socialProfile", socialProfile);
		result.addObject("messageError", messageCode);
		result.addObject("banner", banner);

		return result;
	}

}
