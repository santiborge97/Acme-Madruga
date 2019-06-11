
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
import services.MemberService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Member;

@Controller
@RequestMapping("/member")
public class MemberController extends AbstractController {

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private MemberService			memberService;


	@RequestMapping(value = "/brotherhood/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int memberId) {
		ModelAndView result;

		final String banner = this.configurationService.findConfiguration().getBanner();

		if (this.memberService.existId(memberId)) {

			final Member member = this.memberService.findOne(memberId);

			if (this.brotherhoodService.findByPrincipal().getMembers().contains(member)) {

				result = new ModelAndView("member/display");
				result.addObject("member", member);
				result.addObject("banner", banner);
			} else {

				result = new ModelAndView("redirect:/welcome/index.do");
				result.addObject("banner", banner);
			}

		} else {

			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);

		}

		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int brotherhoodId) {

		final ModelAndView result;
		final Collection<Member> members;
		final String banner = this.configurationService.findConfiguration().getBanner();

		final Brotherhood notFound = this.brotherhoodService.findOne(brotherhoodId);

		if (notFound == null) {
			result = new ModelAndView("misc/notExist");
			result.addObject("banner", banner);
		} else {

			members = this.brotherhoodService.findOne(brotherhoodId).getMembers();

			result = new ModelAndView("member/list");
			result.addObject("members", members);
			result.addObject("requestURI", "member/list.do");
			result.addObject("pagesize", 5);
			result.addObject("banner", banner);
			result.addObject("language", LocaleContextHolder.getLocale().getLanguage());
			result.addObject("autoridad", "");
		}
		return result;

	}
}
