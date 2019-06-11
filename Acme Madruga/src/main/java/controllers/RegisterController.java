
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.Credentials;
import services.BrotherhoodService;
import services.ConfigurationService;
import services.MemberService;
import domain.Brotherhood;
import domain.Member;
import forms.RegisterBrotherhoodForm;
import forms.RegisterMemberForm;

@Controller
@RequestMapping("/register")
public class RegisterController extends AbstractController {

	// Services

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private MemberService			memberService;

	@Autowired
	private ConfigurationService	configurationService;


	//Brotherhood

	@RequestMapping(value = "/createBrotherhood", method = RequestMethod.GET)
	public ModelAndView createBrotherhood() {
		final ModelAndView result;
		final RegisterBrotherhoodForm brotherhood = new RegisterBrotherhoodForm();

		result = this.createEditModelAndViewBrotherhood(brotherhood);

		return result;
	}

	@RequestMapping(value = "/editBrotherhood", method = RequestMethod.POST, params = "save")
	public ModelAndView saveBrotherhood(@ModelAttribute("brotherhood") final RegisterBrotherhoodForm form, final BindingResult binding) {
		ModelAndView result;
		final Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.reconstruct(form, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndViewBrotherhood(form);
		else
			try {
				Assert.isTrue(form.getCheckbox());
				Assert.isTrue(form.checkPassword());
				this.brotherhoodService.save(brotherhood);
				final Credentials credentials = new Credentials();
				credentials.setJ_username(brotherhood.getUserAccount().getUsername());
				credentials.setPassword(brotherhood.getUserAccount().getPassword());
				result = new ModelAndView("redirect:/security/login.do");
				result.addObject("credentials", credentials);
			} catch (final Throwable oops) {
				result = this.createEditModelAndViewBrotherhood(form, "brotherhood.commit.error");
			}
		return result;
	}
	protected ModelAndView createEditModelAndViewBrotherhood(final RegisterBrotherhoodForm brotherhood) {
		ModelAndView result;

		result = this.createEditModelAndViewBrotherhood(brotherhood, null);

		return result;
	}

	protected ModelAndView createEditModelAndViewBrotherhood(final RegisterBrotherhoodForm brotherhood, final String messageCode) {
		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("security/signUpBrotherhood");
		result.addObject("brotherhood", brotherhood);
		result.addObject("banner", banner);
		result.addObject("messageError", messageCode);
		final String countryCode = this.configurationService.findConfiguration().getCountryCode();
		result.addObject("defaultCountry", countryCode);

		return result;
	}

	// Member
	@RequestMapping(value = "/createMember", method = RequestMethod.GET)
	public ModelAndView createMember() {
		final ModelAndView result;

		final RegisterMemberForm member = new RegisterMemberForm();

		result = this.createEditModelAndViewMember(member);

		return result;
	}

	@RequestMapping(value = "/editMember", method = RequestMethod.POST, params = "save")
	public ModelAndView saveMember(@ModelAttribute("member") final RegisterMemberForm form, final BindingResult binding) {
		ModelAndView result;
		Member member;

		member = this.memberService.reconstruct(form, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndViewMember(form);
		else
			try {
				Assert.isTrue(form.getCheckbox());
				Assert.isTrue(form.checkPassword());
				this.memberService.save(member);
				final Credentials credentials = new Credentials();
				credentials.setJ_username(member.getUserAccount().getUsername());
				credentials.setPassword(member.getUserAccount().getPassword());
				result = new ModelAndView("redirect:/security/login.do");
				result.addObject("credentials", credentials);
			} catch (final Throwable oops) {
				result = this.createEditModelAndViewMember(form, "member.commit.error");
			}
		return result;
	}
	protected ModelAndView createEditModelAndViewMember(final RegisterMemberForm member) {
		ModelAndView result;

		result = this.createEditModelAndViewMember(member, null);

		return result;
	}

	protected ModelAndView createEditModelAndViewMember(final RegisterMemberForm member, final String messageCode) {
		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		result = new ModelAndView("security/signUpMember");
		result.addObject("member", member);
		result.addObject("banner", banner);
		result.addObject("messageError", messageCode);
		final String countryCode = this.configurationService.findConfiguration().getCountryCode();
		result.addObject("defaultCountry", countryCode);

		return result;
	}

}
